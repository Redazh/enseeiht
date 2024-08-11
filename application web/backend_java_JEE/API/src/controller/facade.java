package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import modele.Etudiant;
import modele.Feedback;
import modele.MessageToStudent;
import modele.MessageToTutor;
import modele.Session;
import modele.Subject;
import modele.SupportRequest;
import modele.Tuteur;

@Singleton
public class facade {

	@PersistenceContext(unitName = "MaPU")
	private EntityManager em;

	@PostConstruct
	public void init() {
		// Initialiser les utilisateurs
		Etudiant etudiant = new Etudiant();
		etudiant.setNom("admin");
		etudiant.setPrenom("admin");
		etudiant.setEmail("a@a.com");
		etudiant.setMotDePasse("admin");
		etudiant.setTelephone("0600000000");
		etudiant.setAdresse("adrs");
		em.persist(etudiant);
		
		Etudiant etudiant2 = new Etudiant();
		etudiant2.setNom("teste1");
		etudiant2.setPrenom("teste1");
		etudiant2.setEmail("b@b.com");
		etudiant2.setMotDePasse("bbb");
		etudiant2.setTelephone("0600000000");
		etudiant2.setAdresse("adrs");
		em.persist(etudiant2);
		
		Etudiant etudiant3 = new Etudiant();
		etudiant3.setNom("teste2");
		etudiant3.setPrenom("teste2");
		etudiant3.setEmail("e@e.com");
		etudiant3.setMotDePasse("eee");
		etudiant3.setTelephone("0600000000");
		etudiant3.setAdresse("adrs");
		em.persist(etudiant3);
		
		Etudiant etudiant4 = new Etudiant();
		etudiant4.setNom("teste3");
		etudiant4.setPrenom("teste3");
		etudiant4.setEmail("c@c.com");
		etudiant4.setMotDePasse("ccc");
		etudiant4.setTelephone("0600000000");
		etudiant4.setAdresse("adrs");
		em.persist(etudiant4);
		
		
		Tuteur tuteur1 = new Tuteur();
		tuteur1.setNom("testp1");
		tuteur1.setPrenom("testp1");
		tuteur1.setEmail("t@t.com");
		tuteur1.setMotDePasse("test");
		tuteur1.setTelephone("0600000000");
		tuteur1.setAdresse("test");
		em.persist(tuteur1);
		
		Tuteur tuteur2 = new Tuteur();
		tuteur2.setNom("testp2");
		tuteur2.setPrenom("testp2");
		tuteur2.setEmail("d@d.com");
		tuteur2.setMotDePasse("ddd");
		tuteur2.setTelephone("0600000000");
		tuteur2.setAdresse("test");
		em.persist(tuteur2);
		
		Tuteur tuteur3 = new Tuteur();
		tuteur3.setNom("testp2");
		tuteur3.setPrenom("testp2");
		tuteur3.setEmail("s@s.com");
		tuteur3.setMotDePasse("sss");
		tuteur3.setTelephone("0600000000");
		tuteur3.setAdresse("test");
		em.persist(tuteur3);
		
		
		
		
		// Initialiser les sessions

		Session event = new Session();
		event.setTitle("Tutoring Session");
		event.setStart(LocalDateTime.of(2024, 5, 28, 8, 0));
		event.setEnd(LocalDateTime.of(2024, 5, 28, 12, 0));
		event.setSubject("maths");
		event.setTutor("John Doe");
		event.setLessonTitle("Algebra Basics");
		event.setSessionPoints(Arrays.asList("Linear Equations", "Quadratic Equations", "Graphing Functions"));
		event.setAdditionalInfo("Please bring your textbook.");
		event.setMaxStudents(30);
		event.setTuteur(tuteur1);
		em.persist(event);
		
		
		Session event1 = new Session();
		event1.setTitle("Tutoring Session");
		event1.setStart(LocalDateTime.of(2024, 5, 31, 9, 0));
		event1.setEnd(LocalDateTime.of(2024, 5, 31, 11, 0));
		event1.setSubject("physics");
		event1.setTutor("Albert Einstein");
		event1.setLessonTitle("Algebra Basics");
		event1.setSessionPoints(Arrays.asList("Linear Equations", "Quadratic Equations", "Graphing Functions"));
		event1.setAdditionalInfo("Please bring your textbook.");
		event1.setMaxStudents(30);
		event1.setTuteur(tuteur1);
		em.persist(event1);
		
		
		Session event2 = new Session();
		event2.setTitle("Tutoring Session");
		event2.setStart(LocalDateTime.of(2024, 5, 30, 10, 0));
		event2.setEnd(LocalDateTime.of(2024, 5, 30, 12, 0));
		event2.setSubject("science");
		event2.setTutor("Alexandra Penent");
		event2.setLessonTitle("Algebra Basics");
		event2.setSessionPoints(Arrays.asList("Linear Equations", "Quadratic Equations", "Graphing Functions"));
		event2.setAdditionalInfo("Please bring your textbook.");
		event2.setMaxStudents(30);
		event2.setTuteur(tuteur1);
		em.persist(event2);

		// Initialiser les matières
		Subject subject1 = new Subject();
		subject1.setName("maths");
		em.persist(subject1);

		Subject subject2 = new Subject();
		subject2.setName("physics");
		em.persist(subject2);

		Subject subject3 = new Subject();
		subject3.setName("litterature");
		em.persist(subject3);

		Subject subject4 = new Subject();
		subject4.setName("chemistry");
		em.persist(subject4);

		Subject subject5 = new Subject();
		subject5.setName("biology");
		em.persist(subject5);

		event.setMatiere(subject1);
		em.persist(event);

		event1.setMatiere(subject2);
		em.persist(event1);

		event2.setMatiere(subject3);
		em.persist(event2);

	}

	// Enregistrer un nouvel étudiant
	public void registerStudent(Etudiant etudiant) {
		em.persist(etudiant);
	}

	// Enregistrer un nouveau tuteur
	public void registerTuteur(Tuteur tuteur) {
		em.persist(tuteur);
	}

	// Connexion d'un étudiant
	public Etudiant loginEtudiant(String email, String password) {
		Etudiant etudiant = em.createQuery("SELECT u FROM Etudiant u WHERE u.email = :email", Etudiant.class)
				.setParameter("email", email).getSingleResult();

		if (etudiant != null && checkPassword(password, etudiant.getMotDePasse())) {
			return etudiant; // Connexion réussie
		} else {
			return null; // Échec de la connexion
		}
	}

	// Connexion d'un tuteur
	public Tuteur loginTuteur(String email, String password) {
		Tuteur tuteur = em.createQuery("SELECT u FROM Tuteur u WHERE u.email = :email", Tuteur.class)
				.setParameter("email", email).getSingleResult();

		if (tuteur != null && checkPassword(password, tuteur.getMotDePasse())) {
			return tuteur; // Connexion réussie
		} else {
			return null; // Échec de la connexion
		}
	}

	private boolean checkPassword(String motdepasse, String motdepasse1) {
		return motdepasse.equals(motdepasse1);
	}

	// Récupérer tous les étudiants
	public List<Etudiant> getAllStudents() {
		TypedQuery<Etudiant> query = em.createQuery("SELECT e FROM Etudiant e", Etudiant.class);
		return query.getResultList();
	}

	// Récupérer tous les tuteurs

	public List<Tuteur> getAllTuteurs() {
		TypedQuery<Tuteur> query = em.createQuery("SELECT t FROM Tuteur t", Tuteur.class);
		return query.getResultList();
	}

	// Récupérer un étudiant spécifique par ID
	public Etudiant getEtudiant(Long id) {
		return em.find(Etudiant.class, id);
	}

	// Récupérer un tuteur spécifique par ID
	public Tuteur getTuteur(Long id) {
		return em.find(Tuteur.class, id);
	}

	// Mettre à jour les informations d'un étudiant
	public void updateEtudiant(Etudiant etudiant) {
		Etudiant existingEtudiant = em.find(Etudiant.class, etudiant.getId());
		if (existingEtudiant != null) {
			existingEtudiant.setNom(etudiant.getNom());
			existingEtudiant.setPrenom(etudiant.getPrenom());
			existingEtudiant.setEmail(etudiant.getEmail());
			existingEtudiant.setMotDePasse(etudiant.getMotDePasse());
			existingEtudiant.setTelephone(etudiant.getTelephone());
			existingEtudiant.setAdresse(etudiant.getAdresse());
			em.merge(existingEtudiant);
		}
	}

	// Mettre à jour les informations d'un tuteur
	public void updateTuteur(String email, String nom, String prenom, String motDePasse, String telephone,
			String adresse, List<String> subjectNames) {
		Tuteur existingTuteur = em.createQuery("SELECT t FROM Tuteur t WHERE t.email = :email", Tuteur.class)
				.setParameter("email", email).getSingleResult();
		if (existingTuteur != null) {
			if (nom != null && !nom.isEmpty()) {
				existingTuteur.setNom(nom);
			}
			if (prenom != null && !prenom.isEmpty()) {
				existingTuteur.setPrenom(prenom);
			}
			if (motDePasse != null && !motDePasse.isEmpty()) {
				existingTuteur.setMotDePasse(motDePasse);
			}
			if (telephone != null && !telephone.isEmpty()) {
				existingTuteur.setTelephone(telephone);
			}
			if (adresse != null && !adresse.isEmpty()) {
				existingTuteur.setAdresse(adresse);
			}

			Set<Subject> competentSubjects = new HashSet<>();
			for (String subjectName : subjectNames) {
				Subject subject = em.createQuery("SELECT s FROM Subject s WHERE s.name = :name", Subject.class)
						.setParameter("name", subjectName).getSingleResult();
				competentSubjects.add(subject);
			}
			existingTuteur.setCompetentSubjects(competentSubjects);

			em.merge(existingTuteur);
		}
	}
	
	// Mettre à jour les informations d'un étudiant par email
	public void updateStudent(String email, String nom, String prenom, String motDePasse, String telephone,
			String adresse) {
		// Trouver letudiant existant dans la base de données en fonction de l'email
		Etudiant studentToUpdate = em.createQuery("SELECT t FROM Etudiant t WHERE t.email = :email", Etudiant.class)
				.setParameter("email", email).getSingleResult();

		// Mettre à jour les informations personnelles
		// Vérifier si les informations personnelles ne sont pas vides avant de les
		// mettre à jour
		if (nom != null && !nom.isEmpty()) {
			studentToUpdate.setNom(nom);
		}
		if (prenom != null && !prenom.isEmpty()) {
			studentToUpdate.setPrenom(prenom);
		}
		if (motDePasse != null && !motDePasse.isEmpty()) {
			studentToUpdate.setMotDePasse(motDePasse);
		}
		if (telephone != null && !telephone.isEmpty()) {
			studentToUpdate.setTelephone(telephone);
		}
		if (adresse != null && !adresse.isEmpty()) {
			studentToUpdate.setAdresse(adresse);
		}
		// Fusionner les modifications avec la base de données
		em.merge(studentToUpdate);
	}

	// Récupérer les tuteurs par nom de matiere
	public List<Tuteur> getTuteursBySubjectName(String subjectName) {
	    TypedQuery<Tuteur> query = em.createQuery(
	        "SELECT t FROM Tuteur t JOIN t.competentSubjects s WHERE s.name = :subjectName", Tuteur.class);
	    query.setParameter("subjectName", subjectName);
	    return query.getResultList();
	}
	
	// Récupérer tous les noms de matieres
	public List<String> getAllSubjectNames() {
		TypedQuery<String> query = em.createQuery("SELECT s.name FROM Subject s", String.class);
		return query.getResultList();
	}

	// Sauvegarder une session
	public void saveSession(Session session) {
		em.persist(session);
	}

	// Récupérer toutes les sessions
	public List<Session> getAllEvents() {
		TypedQuery<Session> query = em.createQuery("SELECT s FROM Session s", Session.class);
		return query.getResultList();
	}

	// Trouver un sujet par son nom
	public Subject findSubjectByName(String subjectName) {
		TypedQuery<Subject> query = em.createQuery("SELECT s FROM Subject s WHERE s.name = :name", Subject.class);
		query.setParameter("name", subjectName);

		List<Subject> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return null;
		}
	}

	// Chercher une session par ses détails
	public Session findSessionByDetails(String title, LocalDateTime start, LocalDateTime end, String subject, String tutor, String lessonTitle, String additionalInfo) {
	    TypedQuery<Session> query = em.createQuery(
	        "SELECT s FROM Session s WHERE s.title = :title AND s.subject = :subject AND s.tutor = :tutor AND s.lessonTitle = :lessonTitle AND s.additionalInfo = :additionalInfo", 
	        Session.class
	    );
	    query.setParameter("title", title);
	    query.setParameter("subject", subject);
	    query.setParameter("tutor", tutor);
	    query.setParameter("lessonTitle", lessonTitle);
	    query.setParameter("additionalInfo", additionalInfo);
	    
	    List<Session> results = query.getResultList();
	    System.out.println(results);
	    if (!results.isEmpty()) {
	        Session session = results.get(0);
	        
	     // Ajouter 2 heures aux heures d'entrée
	        LocalDateTime adjustedStart = start.minusHours(2);
	        LocalDateTime adjustedEnd = end.minusHours(2);

            // Formater les dates stockées et les dates d'entrée pour les comparer
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	        String storedStart = session.getStart().format(formatter);
	        String storedEnd = session.getEnd().format(formatter);
	        String inputStart = adjustedStart.format(formatter);
	        String inputEnd = adjustedEnd.format(formatter);

	        
	        System.out.println("Stored Start: " + storedStart);
	        System.out.println("Input Start: " + inputStart);
	        System.out.println("Stored End: " + storedEnd);
	        System.out.println("Input End: " + inputEnd);
	        
	       // Vérifier si tous les attributs correspondent, y compris les dates formatées
	        if (storedStart.equals(inputStart) && 
	            storedEnd.equals(inputEnd) && 
	            session.getSubject().equals(subject) && 
	            session.getTutor().equals(tutor) && 
	            session.getLessonTitle().equals(lessonTitle) && 
	            session.getAdditionalInfo().equals(additionalInfo)) {
	            return session;
	        } else {
	            return null;
	        }
	    } else {
	        System.out.println("pas de session trouve");
	        return null;
	    }
	}


	// Sauvegarder un feedback
	public void saveFeedback(String name, String email, String message, int rating, long sessionId, Tuteur tutor) {
		Feedback feedback = new Feedback();
		feedback.setName(name);
		feedback.setEmail(email);
		feedback.setMessage(message);
		feedback.setRating(rating);
		feedback.setSessionId(sessionId);
		feedback.setTutor(tutor);
		em.persist(feedback);
	}
	
	// Sauvegarder un support
	public void saveSupportRequest(String name, String email, String message) {
		SupportRequest supportRequest = new SupportRequest();
		supportRequest.setName(name);
		supportRequest.setEmail(email);
		supportRequest.setMessage(message);
		em.persist(supportRequest);
	}

	// recuperer la liste des feedback
	public List<Feedback> getFeedbacksByTutorId(String tutorId) {
		return em.createQuery("SELECT f FROM Feedback f WHERE f.tutor.email = :tutorEmail")
				.setParameter("tutorEmail", tutorId).getResultList();
	}
	

	// Récupérer un étudiant par son email
	public Etudiant getEtudiantByEmail(String email) {
	    try {
	        return em.createQuery("SELECT e FROM Etudiant e WHERE e.email = :email", Etudiant.class)
	                 .setParameter("email", email)
	                 .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}

	// Récupérer un tuteur par son email
	public Tuteur getTuteurByEmail(String email) {
	    try {
	        return em.createQuery("SELECT t FROM Tuteur t WHERE t.email = :email", Tuteur.class)
	                 .setParameter("email", email)
	                 .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}

	// Mettre à jour une session existante
	public void updateSession(Session session) {
	    em.merge(session);
	}

	// Récupérer le nom complet d'un tuteur par email
	public String getFullNameByEmail(String email) {
	    try {
	        TypedQuery<Tuteur> query = em.createQuery("SELECT t FROM Tuteur t WHERE t.email = :email", Tuteur.class);
	        query.setParameter("email", email);
	        Tuteur tuteur = query.getSingleResult();
	        if (tuteur != null) {
	            String nom = tuteur.getNom();
	            String prenom = tuteur.getPrenom();
	            return prenom + " " + nom; 
	        } else {
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	// Mettre à jour la session quand l'etudiant s'enregistre pour la session
	public void enrollStudentInSession(Session session, Etudiant student) {
	    List<Etudiant> updatedStudentsList = new ArrayList<>(session.getStudents());
	    updatedStudentsList.add(student);
	    session.setStudents(updatedStudentsList);
	    int nbstudents = session.getcurrentStudents();
	    nbstudents = nbstudents + 1;
	    session.setcurrentStudents(nbstudents);
	    em.merge(session);
	}

	// Envoyer un message à un tuteur
	public void envoyerMessageToTutor(Etudiant sender, Tuteur receiver, String content) {
	    MessageToTutor message = new MessageToTutor();
	    message.setSender(sender);
	    message.setReceiver(receiver);
	    message.setContent(content);
	    em.persist(message);
	}

	// Envoyer un message à un étudiant
	public void envoyerMessageToStudent(Tuteur sender, Etudiant receiver, String content) {
	    MessageToStudent message = new MessageToStudent();
	    message.setSender(sender);
	    message.setReceiver(receiver);
	    message.setContent(content);
	    em.persist(message);
	}
	
	// Récupérer les messages envoyés d'un étudiant à un tuteur
	public List<MessageToStudent> getMessagesFromTutorToStudent(Tuteur sender) {
	    return em.createQuery("SELECT m FROM MessageToStudent m WHERE m.sender = :sender", MessageToStudent.class)
	             .setParameter("sender", sender)
	             .getResultList();
	}
    
	// Récupérer les messages envoyés d'un étudiant à un tuteur
	public List<MessageToTutor> getMessagesFromStudentToTutor(Etudiant sender) {
	    return em.createQuery("SELECT m FROM MessageToTutor m WHERE m.sender = :sender", MessageToTutor.class)
	             .setParameter("sender", sender)
	             .getResultList();
	}
    
	// Récupérer les messages reçus par un tuteur
	public List<MessageToTutor> getMessagesToTutor(Tuteur receiver) {
	    TypedQuery<MessageToTutor> query = em.createQuery(
	        "SELECT m FROM MessageToTutor m WHERE m.receiverTutor = :receiver", MessageToTutor.class);
	    query.setParameter("receiver", receiver);
	    return query.getResultList();
	}
    
	// Récupérer les messages reçus par un étudiant
	public List<MessageToStudent> getMessagesToStudent(Etudiant receiver) {
	    TypedQuery<MessageToStudent> query = em.createQuery(
	        "SELECT m FROM MessageToStudent m WHERE m.receiverStudent = :receiver", MessageToStudent.class);
	    query.setParameter("receiver", receiver);
	    return query.getResultList();
	}
	// Récupérer tous les emails des étudiants
	public List<String> getAllReceiverStudentEmails() {
	    TypedQuery<String> query = em.createQuery("SELECT e.email FROM Etudiant e", String.class);
	    return query.getResultList();
	}

	// Récupérer tous les emails des tuteurs
	public List<String> getAllReceiverTutorEmails() {
	    TypedQuery<String> query = em.createQuery("SELECT t.email FROM Tuteur t", String.class);
	    return query.getResultList();
	}

	
}
