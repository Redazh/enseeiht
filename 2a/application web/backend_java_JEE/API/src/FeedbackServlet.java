import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.json.Json;
import javax.json.JsonObject;
import controller.facade;
import modele.Tuteur;

@WebServlet("/submit-feedback")
public class FeedbackServlet extends HttpServlet {
	@EJB
	private facade facade;

	// Méthode pour gérer les requêtes POST pour ajouter des feedback
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			// Lire le JSON
			JsonObject jsonObject = Json.createReader(request.getReader()).readObject();
			String name = jsonObject.getString("name");
			String email = jsonObject.getString("email", "");
			String message = jsonObject.getString("message");
			int rating = jsonObject.getInt("rating");
			long sessionId = jsonObject.getJsonNumber("sessionId").longValue();

			// Récupérer tutorId comme une chaîne et le convertir en long
			String tutorIdStr = jsonObject.getString("tutorId");
			long tutorId = Long.parseLong(tutorIdStr);

			// Récupérer le tuteur par son ID
			Tuteur tutor = facade.getTuteur(tutorId); 

			// Sauvegarder le feedback
			facade.saveFeedback(name, email, message, rating, sessionId, tutor);

			JsonObject jsonResponse = Json.createObjectBuilder().add("message", "Feedback envoyé avec succès.")
					.build();

			out.print(jsonResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
			JsonObject jsonResponse = Json.createObjectBuilder()
					.add("message", "erreur lors feedback.").build();
			out.print(jsonResponse.toString());
		}
	}
}
