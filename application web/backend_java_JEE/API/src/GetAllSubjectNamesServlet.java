

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
 * Servlet implementation class GetAllSubjectNamesServlet
 */
@WebServlet("/GetAllSubjectNamesServlet")
public class GetAllSubjectNamesServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
    private facade facade;
	
	
	// Méthode pour recuperer la liste des noms de matières disponibles
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	
            // Récupérer la liste des noms de matières disponibles
            List<String> subjectNames = facade.getAllSubjectNames();

            // Convertir la liste en JSON et envoyer la réponse
            Gson gson = new Gson();
            String jsonSubjectNames = gson.toJson(subjectNames);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonSubjectNames);
        } catch (Exception e) {
            // Réponse indiquant une erreur
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erreur lors de la récupération des matières.");
        }
    }
}

