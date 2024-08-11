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
import controller.facade;
import modele.Tuteur;

@WebServlet("/support")
public class SupportServlet extends HttpServlet {
	@EJB
	private facade facade;

	// Gerer les requettes de support
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			JsonObject jsonObject = Json.createReader(request.getReader()).readObject();
			String name = jsonObject.getString("name");
			String email = jsonObject.getString("email");
			String message = jsonObject.getString("message");

			JsonObject jsonResponse = Json.createObjectBuilder()
					.add("message", "Demande d'assistance soumise avec succès.").build();

			out.print(jsonResponse.toString());
			
			Tuteur tuteur = facade.getTuteurByEmail(email);
			
			// Si l utilisateur n est pas un tuteur on initialise un tuteur avec le meme nom et Email
			// pour utiliser la fonction envoyerMessageToStudent
			if (tuteur == null) {
				tuteur = new Tuteur();
				tuteur.setNom(name);
				tuteur.setEmail(email);
				facade.registerTuteur(tuteur);
			}
			
			// On utilise la fonction envoyerMessageToStudent pour envoyer un message à l admin a@a.com
			// On a considere l admin comme un etudiant
			facade.envoyerMessageToStudent(tuteur, facade.getEtudiantByEmail("a@a.com"), message);
			tuteur = null;
		} catch (Exception e) {
			e.printStackTrace();
			JsonObject jsonResponse = Json.createObjectBuilder()
					.add("message", "erreur dans support.").build();
			out.print(jsonResponse.toString());
		}
	}
}
