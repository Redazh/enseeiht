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
import javax.servlet.http.HttpSession;

import modele.Etudiant;
import modele.Tuteur;
import controller.facade;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
    private facade facade;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("<html><body>Hello World !</body></html>");
	}

	// Méthode pour gerer la connexiond'un utilisateur
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		    // Lecture des données JSON de la requête
			JsonObject jsonBody = Json.createReader(request.getReader()).readObject();
			
		    String email = jsonBody.getString("username");
		    String motDePasse = jsonBody.getString("password");
		    String userType = jsonBody.getString("user");
	
		    // Vérifie si l'utilisateur est un étudiant ou un tuteur
		    if (userType.equals("student")) {
		        // Authentification de l'étudiant
		        Etudiant etudiant = facade.loginEtudiant(email, motDePasse);
	
		        // Vérification du résultat de l'authentification de l'étudiant
		        if (etudiant != null) {
		            // Authentification réussie
		            HttpSession session = request.getSession(true);
		            session.setAttribute("userId", etudiant.getId());
	
		            // Réponse indiquant une authentification réussie
		            response.setStatus(HttpServletResponse.SC_OK);
		        } else {
		            // Réponse indiquant une erreur d'authentification
		            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        }
		    } else if (userType.equals("tutor")) {
		        // Authentification du tuteur
		        Tuteur tutor = facade.loginTuteur(email, motDePasse);
	
		        // Vérification du résultat de l'authentification du tuteur
		        if (tutor != null) {
		            // Authentification réussie
		            HttpSession session = request.getSession(true);
		            session.setAttribute("userId", tutor.getId());
	
		            // Réponse indiquant une authentification réussie
		            response.setStatus(HttpServletResponse.SC_OK);
		        } else {
		            // Réponse indiquant une erreur d'authentification
		            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        }
	
		    } 
		}catch (Exception e) {
	         response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    }
	}
		 
}
	
