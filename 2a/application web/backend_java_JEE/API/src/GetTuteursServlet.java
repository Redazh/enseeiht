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

import com.google.gson.JsonArray;

import controller.facade;
import modele.Tuteur;

@WebServlet("/get_tuteurs")
public class GetTuteursServlet extends HttpServlet {
	@EJB
	private facade facade;

	// Méthode pour récupérer la liste des tuteurs
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			List<Tuteur> tuteurs = facade.getAllTuteurs();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Tuteur tuteur : tuteurs) {
				arrayBuilder.add(Json.createObjectBuilder().add("id", tuteur.getId()).add("nom", tuteur.getNom())
						.add("prenom", tuteur.getPrenom()).add("Email", tuteur.getEmail()));
			}

			javax.json.JsonArray jsonArray = arrayBuilder.build();
			out.print(jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
