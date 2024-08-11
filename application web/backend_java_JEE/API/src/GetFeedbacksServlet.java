import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.facade;
import modele.Feedback;

@WebServlet("/get_feedbacks")
public class GetFeedbacksServlet extends HttpServlet {
	@EJB
	private facade facade;

	// Méthode pour récupérer les feedbacks disponibles dun tuteur
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			// Récupérer le paramètre tutorId
			String tutorIdParam = request.getParameter("tutorId");
			if (tutorIdParam == null || tutorIdParam.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print("{\"message\": \"tutorId non mentionne.\"}");
				return;
			}
			System.out.print(tutorIdParam);
			// Récupérer les feedbacks pour le tuteur spécifié
			List<Feedback> feedbacks = facade.getFeedbacksByTutorId(tutorIdParam);
			System.out.print(feedbacks.size());
			
			// Construire un tableau JSON à partir des feedbacks
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Feedback feedback : feedbacks) {
				arrayBuilder.add(Json.createObjectBuilder().add("id", feedback.getId()).add("name", feedback.getName())
						.add("email", feedback.getEmail()).add("message", feedback.getMessage())
						.add("rating", feedback.getRating()));
			}

			 // Envoyer la réponse JSON
			JsonArray jsonArray = arrayBuilder.build();
			out.print(jsonArray.toString());
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.print("{\"message\": \"tutorId Invalide.\"}");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
