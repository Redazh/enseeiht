import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Etudiant;
import modele.Tuteur;
import modele.MessageToStudent;
import modele.MessageToTutor;
import controller.facade;

@WebServlet("/messages")

// Cette servlet est utilisée pour envoyer des messages et récupérer les messages envoyés.
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private facade facade;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonObject jsonBody = Json.createReader(request.getReader()).readObject();
            // Lecture du contenu JSON de la requête

            //afficher le JSON reçu
            StringWriter stringWriter = new StringWriter();
            try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
                jsonWriter.write(jsonBody);
            }

            // Vérifier si les clés existent dans le JSON
            if (!jsonBody.containsKey("sender_email") ||
                !jsonBody.containsKey("receiver_email") ||
                !jsonBody.containsKey("usertype") ||
                !jsonBody.containsKey("message")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing required fields\"}");
                return;
            }

            String senderEmail = jsonBody.getString("sender_email");
            String receiverEmail = jsonBody.getString("receiver_email");
            String userType = jsonBody.getString("usertype");
            String messageContent = jsonBody.getString("message");

            // Vérification de l'utilisateur
            if (userType.equals("student")) {
                Etudiant sender = facade.getEtudiantByEmail(senderEmail);
                Tuteur receiver = facade.getTuteurByEmail(receiverEmail);
                if (sender != null && receiver != null) {
                    facade.envoyerMessageToTutor(sender, receiver, messageContent);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else if (userType.equals("tutor")) {
                Tuteur sender = facade.getTuteurByEmail(senderEmail);
                Etudiant receiver = facade.getEtudiantByEmail(receiverEmail);
                if (sender != null && receiver != null) {
                    facade.envoyerMessageToStudent(sender, receiver, messageContent);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                // Type d'utilisateur non reconnu
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            // En cas d'erreur, réponse avec un code de statut 400
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Récupérer un message qu'on a envoyé à une personne cible
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            String userEmail = request.getParameter("user_email");
            String userType = request.getParameter("user_type");

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            if (userType.equals("student")) {
                Etudiant user = facade.getEtudiantByEmail(userEmail);
                if (user != null) {
                    List<MessageToTutor> messages = facade.getMessagesFromStudentToTutor(user);
                    for (MessageToTutor message : messages) {
                        arrayBuilder.add(Json.createObjectBuilder()
                            .add("sender", message.getSender().getEmail())
                            .add("receiver", message.getReceiver().getEmail())
                            .add("content", message.getContent()));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else if (userType.equals("tutor")) {
                Tuteur user = facade.getTuteurByEmail(userEmail);
                if (user != null) {
                    List<MessageToStudent> messages = facade.getMessagesFromTutorToStudent(user);
                    for (MessageToStudent message : messages) {
                        arrayBuilder.add(Json.createObjectBuilder()
                            .add("sender", message.getSender().getEmail())
                            .add("receiver", message.getReceiver().getEmail())
                            .add("content", message.getContent()));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                // Type d'utilisateur non reconnu
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            // Construire le tableau JSON et l'écrire dans la réponse
            javax.json.JsonArray jsonArray = arrayBuilder.build();
            out.print(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.print("{\"error\":\"server error\"}");
        }
    }
}
