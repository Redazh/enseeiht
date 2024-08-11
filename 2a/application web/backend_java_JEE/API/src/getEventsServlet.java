import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.facade;
import modele.Session;
import modele.Tuteur;

@WebServlet("/get_events")
public class getEventsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB
	private facade facade;

	// Méthode pour récupérer les sessions disponibles
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// Récupérer les sessions
		List<Session> eventsList = facade.getAllEvents();

		// Construire un tableau JSON pour les sessions
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		for (Session session : eventsList) {
			// Construire un tableau JSON pour les points de session
			JsonArrayBuilder sessionPointsArrayBuilder = Json.createArrayBuilder();
			for (String point : session.getSessionPoints()) {
				sessionPointsArrayBuilder.add(point);
			}
			JsonArray sessionPointsArray = sessionPointsArrayBuilder.build();
			
			// Créer un objet JSON pour chaque session
			JsonObject jsonSession = Json.createObjectBuilder()
					.add("title", session.getTitle())
					.add("start", session.getStart().toString())
					.add("end", session.getEnd().toString())
					.add("subject", session.getSubject())
					.add("tutor", session.getTutor())
					.add("lessonTitle", session.getLessonTitle())
					.add("sessionPoints", sessionPointsArray)
					.add("additionalInfo", session.getAdditionalInfo())
					.build();
			jsonArrayBuilder.add(jsonSession);
		}
		JsonArray jsonArray = jsonArrayBuilder.build();

		// Convertir la liste en JSON et envoyer la réponse
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonArray);
		out.flush();
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
