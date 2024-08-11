import java.io.IOException;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonValue;
import modele.Session;
import controller.facade;

@WebServlet("/create_sessionServlet")
public class create_sessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB
    private facade facade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("<html><body>Hello World !</body></html>");
    }

    // Méthode pour gérer les requêtes POST afin de creer des sessions
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Récupérer les informations de la session à partir de la requête JSON
            JsonObject jsonBody = Json.createReader(request.getReader()).readObject();
            String title = jsonBody.getString("title");
            String startDate = jsonBody.getString("start").replace("T", " ").replace("Z", "");
            String endDate = jsonBody.getString("end").replace("T", " ").replace("Z", "");
            String subject = jsonBody.getString("subject");
            String tutor = jsonBody.getString("tutor");
            String lessonTitle = jsonBody.getString("lessonTitle");
            String maxStudentsString = jsonBody.getString("maxStudents");
            int maxStudents = Integer.parseInt(maxStudentsString);
            JsonArray sessionPointsArray = jsonBody.getJsonArray("sessionPoints");
            String additionalInfo = jsonBody.getString("additionalInfo");
            String email = jsonBody.getString("email");
            
            // Convertir les dates de String à LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
            start = start.plusHours(2); // Ajuster le fuseau horaire 
            end = end.plusHours(2); // Ajuster le fuseau horaire 

            // Diviser les points de session en une liste de points de session
            List<String> sessionPointsList = new ArrayList<>();
            for (JsonValue value : sessionPointsArray) {
                sessionPointsList.add(value.toString());
            }

            // Créer une nouvelle session
            Session session = new Session();
            session.setTitle(title);
            session.setLessonTitle(lessonTitle);
            session.setStart(start);
            session.setEnd(end);
            session.setSubject(subject);
            session.setTutor(tutor);
            session.setLessonTitle(lessonTitle);
            session.setSessionPoints(sessionPointsList);
            session.setAdditionalInfo(additionalInfo);
            session.setTuteur(facade.getTuteurByEmail(email));
            session.setMatiere(facade.findSubjectByName(subject));
            session.setMaxStudents(maxStudents);
            facade.saveSession(session);
            
            // Réponse indiquant une création de session réussie
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            // Envoyer une réponse d'erreur si quelque chose ne va pas lors de la création de la session
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
