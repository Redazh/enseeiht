import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import java.util.*;
import com.google.gson.Gson;


/**
 * Servlet implementation class UpdateTutorServlet
 */
@WebServlet("/UpdateInfoServlet")
public class UpdateInfoServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @EJB
    private facade facade;

   // Méthode pour gerer la modification des informations d'un utilisateur
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lecture des données JSON de la requête
            JsonObject jsonBody = Json.createReader(request.getReader()).readObject();
            String userType = jsonBody.getString("userType");
            String email = jsonBody.getString("email");
            String nom = jsonBody.getString("nom");
            String prenom = jsonBody.getString("prenom");
            String motDePasse = jsonBody.getString("motDePasse");
            String telephone = jsonBody.getString("telephone");
            String adresse = jsonBody.getString("adresse");
            
            if(userType.equals("student")) {
            	facade.updateStudent(email, nom, prenom, motDePasse, telephone, adresse);
            	
            } else if(userType.equals("tutor")) {
            	// Si c'est un tuteur ajouter les matieres choisies en front a ce tuteur
            	List<String> subjectNames = Arrays.asList(jsonBody.getString("subjectNames").split(","));

                // Mise à jour du tuteur
                facade.updateTuteur(email, nom, prenom, motDePasse, telephone, adresse, subjectNames);
            	
            }
            
            // Réponse indiquant une mise à jour réussie
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            // Réponse indiquant une erreur de mise à jour
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
