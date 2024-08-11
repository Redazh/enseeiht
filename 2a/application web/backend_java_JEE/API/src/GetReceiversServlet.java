import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import modele.Session;
import modele.Etudiant;
import modele.Tuteur;
import controller.facade;

@WebServlet("/getReceivers")
public class GetReceiversServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private facade facade;
    
    // Méthode pour récupérer la liste des emails a qui l'utilisateur peut envoyer des messages
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupère le type d'utilisateur (étudiant ou tuteur)
        String userType = request.getParameter("user_type"); 
        List<String> emails = null;

        if ("student".equalsIgnoreCase(userType)) {
            // Si l'utilisateur est un étudiant, récupérez les e-mails des tuteurs
            emails = facade.getAllReceiverTutorEmails();
        } else if ("tutor".equalsIgnoreCase(userType)) {
            // Si l'utilisateur est un tuteur, récupérez les e-mails des étudiants
            emails = facade.getAllReceiverStudentEmails();
        }

        if (emails != null) {
            // Convertir la liste en JSON et envoyer la réponse
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(emails));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Envoyer une réponse d'erreur si le type d'utilisateur est invalide
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "user type invalide");
        }
    }
}
