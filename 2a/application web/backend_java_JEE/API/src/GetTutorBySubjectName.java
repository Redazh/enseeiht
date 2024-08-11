import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.facade;
import modele.Tuteur;

@WebServlet("/GetTutorBySubjectName")
public class GetTutorBySubjectName extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private facade facade;

	public GetTutorBySubjectName() {
		super();
	}
	
	
	// Méthode pour récupérer la liste des tuteurs de la matiere envoye par le front
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Définir le type de contenu de la réponse
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			// Lire le paramètre "matiere" depuis la requête
			String matiere = request.getParameter("matiere");
			if (matiere == null || matiere.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.print("{\"error\":\"ereur dans matiere\"}");
				return;
			}

			// Utiliser la méthode de la facade pour obtenir les tuteurs par matière
			List<Tuteur> tuteurs = facade.getTuteursBySubjectName(matiere);
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Tuteur tuteur : tuteurs) {
				arrayBuilder.add(Json.createObjectBuilder().add("nom", tuteur.getNom())
						.add("prenom", tuteur.getPrenom()).add("email", tuteur.getEmail()));
			}

			// Construire le tableau JSON et l'écrire dans la réponse
			javax.json.JsonArray jsonArray = arrayBuilder.build();
			out.print(jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.print("{\"error\":\"Internal server error\"}");
		}
	}

}