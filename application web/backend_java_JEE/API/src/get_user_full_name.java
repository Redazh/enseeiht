

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
 * Servlet implementation class get_user_full_name
 */
@WebServlet("/get_user_full_name")
public class get_user_full_name extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
    private facade facade;

	// Méthode pour recuperer le nom complet a partir du mail
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Récupérer l'email de l'utilisateur
    	String userEmail = request.getParameter("email");
    	// Obtenir le nom complet en utilisant la méthode du facade
        String fullName = facade.getFullNameByEmail(userEmail); 
        // Créer un objet JSON contenant le nom complet
        JsonObject jsonObject = Json.createObjectBuilder().add("fullName", fullName).build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());
       
    }
}

