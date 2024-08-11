import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/messages2")

//Cette servlet est destinée à afficher les messages reçus par un utilisateur.
public class MessageServlet2 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private facade facade;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        try {
            // Lecture des paramètres
            String userEmail = request.getParameter("user_email");
            String userType = request.getParameter("user_type");


            if (userEmail == null || userType == null) {
                System.out.println("erreur dans user type ou email");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"erreur dans user type ou email\"}");
                return;
            }

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            if (userType.equals("student")) {
                Etudiant user = facade.getEtudiantByEmail(userEmail);

                if (user != null) {
                    System.out.println("Student: " + userEmail);
                    List<MessageToStudent> messages = facade.getMessagesToStudent(user);

                    for (MessageToStudent message : messages) {
                        arrayBuilder.add(Json.createObjectBuilder()
                            .add("sender", message.getSender().getEmail())
                            .add("receiver", message.getReceiver().getEmail())
                            .add("content", message.getContent()));
                    }
                } else {
                    System.out.println("Student not found: " + userEmail);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Student not found\"}");
                    return;
                }

            } else if (userType.equals("tutor")) {
                Tuteur user = facade.getTuteurByEmail(userEmail);

                if (user != null) {
                    System.out.println("Tutor: " + userEmail);
                    List<MessageToTutor> messages = facade.getMessagesToTutor(user);

                    for (MessageToTutor message : messages) {
                        arrayBuilder.add(Json.createObjectBuilder()
                            .add("sender", message.getSender().getEmail())
                            .add("receiver", message.getReceiver().getEmail())
                            .add("content", message.getContent()));
                    }
                } else {
                    System.out.println("Tutor not found: " + userEmail);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"Tutor not found\"}");
                    return;
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"erreur dans user type\"}");
                return;
            }

            // Construire le tableau JSON et l'écrire dans la réponse
            javax.json.JsonArray jsonArray = arrayBuilder.build();
            out.print(jsonArray.toString());
            System.out.println("Response JSON: " + jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter outError = response.getWriter()) {
                outError.print("{\"error\":\" server error: " + e.getMessage() + "\"}");
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
