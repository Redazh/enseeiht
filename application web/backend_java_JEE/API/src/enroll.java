import java.io.IOException;
import java.io.PrintWriter;

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
import java.util.Arrays;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonArray;
import javax.json.JsonValue;

import modele.Etudiant;
import modele.Tuteur;
import modele.Session;
import modele.Subject;
import controller.facade;

@WebServlet("/enroll")
public class enroll extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private facade facade;
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().println("<html><body>Hello World !</body></html>");
	}

    // Méthode pour gérer les requêtes POST pour l'inscription à une session
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
		// Lire le corps de la requête JSON
		
		JsonObject jsonBody = Json.createReader(request.getReader()).readObject();
		String userEmail = jsonBody.getString("userEmail");
		JsonObject sessionDetails = jsonBody.getJsonObject("sessionDetails");
		String title = sessionDetails.getString("title");
		String startStr = sessionDetails.getString("start");
		String endStr = sessionDetails.getString("end");
		String subject = sessionDetails.getString("subject");
		String tutor = sessionDetails.getString("tutor");
		String lessonTitle = sessionDetails.getString("lessonTitle");
		String additionalInfo = sessionDetails.getString("additionalInfo");
        
		// Convertir les chaînes de caractères en objets LocalDateTime
		
		LocalDateTime start = LocalDateTime.parse(startStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		LocalDateTime end = LocalDateTime.parse(endStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		start = start.plusHours(4);
		end = end.plusHours(4);

		
        // Rechercher la session dans la base de données selon les détails
		Session session = facade.findSessionByDetails(title, start, end, subject, tutor, lessonTitle, additionalInfo);
		if (session != null) {
            // Rechercher l'étudiant par email
			Etudiant student = facade.getEtudiantByEmail(userEmail);

			if (student != null) {

                // Inscrire l'étudiant à la session
				facade.enrollStudentInSession(session, student);

				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Étudiant non trouvé");
			}
		}
	}

}
