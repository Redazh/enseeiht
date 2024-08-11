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

import modele.Etudiant;
import modele.Tuteur;
import controller.facade;



@WebServlet("/register")
public class InscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
    private facade facade;

    // Méthode pour gerer l'inscription d'un utilisateur
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonObject jsonBody = Json.createReader(request.getReader()).readObject();

            // Extraction des données de l'utilisateur à partir du JSON
            String nom = jsonBody.getString("nom");
            String prenom = jsonBody.getString("prenom");
            String email = jsonBody.getString("email");
            String password = jsonBody.getString("password");
            String telephone = jsonBody.getString("telephone");
            String adresse = jsonBody.getString("adresse");
            String userType = jsonBody.getString("user");

            if(userType.equals("student")) {
                // Création d'un nouvel utilisateur étudiant
                Etudiant etudiant = new Etudiant();
                etudiant.setNom(nom);
                etudiant.setPrenom(prenom);
                etudiant.setEmail(email);
                etudiant.setMotDePasse(password);
                etudiant.setTelephone(telephone);
                etudiant.setAdresse(adresse);

                // Enregistrement de l'étudiant dans la base de données
                facade.registerStudent(etudiant);

                
                // Réponse indiquant une inscription réussie avec un code de statut 200 (OK)
                response.setStatus(HttpServletResponse.SC_OK);
                
            } else if(userType.equals("tutor")) {
                // Création d'un nouvel utilisateur tuteur
                Tuteur tutor = new Tuteur();
                tutor.setNom(nom);
                tutor.setPrenom(prenom);
                tutor.setEmail(email);
                tutor.setMotDePasse(password);
                tutor.setTelephone(telephone);
                tutor.setAdresse(adresse);

                // Enregistrement du tuteur dans la base de données
                facade.registerTuteur(tutor);

                response.setStatus(HttpServletResponse.SC_OK);
          }      
           
        } catch (Exception e) {
            //réponse avec un code de statut 400 (Bad Request)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
