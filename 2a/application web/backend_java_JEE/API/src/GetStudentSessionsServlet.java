import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.facade;
import modele.Session;
import modele.Etudiant;

@WebServlet("/get_student_sessions")
public class GetStudentSessionsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private facade facade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get student email from request
        String email = request.getParameter("email");
        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing email parameter");
            return;
        }

        // Fetch the student from the database using the email
        Etudiant student = facade.getEtudiantByEmail(email);
        if (student == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
            return;
        }

        // Get the list of sessions the student is enrolled in
        Collection<Session> sessionsList = student.getTutoringSessions();

        // Build JSON array for sessions
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Session session : sessionsList) {
            JsonArrayBuilder sessionPointsArrayBuilder = Json.createArrayBuilder();
            for (String point : session.getSessionPoints()) {
                sessionPointsArrayBuilder.add(point);
            }
            JsonArray sessionPointsArray = sessionPointsArrayBuilder.build();
            
            JsonObject jsonSession = Json.createObjectBuilder()
                .add("title", session.getTitle())
                .add("start", session.getStart().toString())
                .add("end", session.getEnd().toString())
                .add("subject", session.getSubject())
                .add("tutor", session.getTutor())
                .add("lessonTitle", session.getLessonTitle())
                .add("sessionPoints", sessionPointsArray)
                .add("additionalInfo", session.getAdditionalInfo())
                .build();
            jsonArrayBuilder.add(jsonSession);
        }
        JsonArray jsonArray = jsonArrayBuilder.build();

        // Send JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonArray);
        out.flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}