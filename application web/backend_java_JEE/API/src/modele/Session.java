package modele;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;

@Entity
@Table(name = "Session")
public class Session {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime start;

    @Column
    private LocalDateTime end;

    @Column
    private String subject;

    @Column
    private String tutor;
    
    @Column
    private String title;

    @Column
    private String lessonTitle;

 
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> sessionPoints;

    @Column
    private String additionalInfo;

    @Column
    private int maxStudents;
    @Column
    private int currentStudents;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Etudiant> students;
    
    @ManyToOne
    private Subject matiere;
    
    @ManyToOne
    private Tuteur tuteur;

    public Session() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public List<String> getSessionPoints() {
        return sessionPoints;
    }

    public void setSessionPoints(List<String> sessionPoints) {
        this.sessionPoints = sessionPoints;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Collection<Etudiant> getStudents() {
        return students;
    }

    public void setStudents(Collection<Etudiant> students) {
        this.students = students;
    }
    
    public Subject getMatiere() {
        return matiere;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    

    public void setMatiere(Subject matiere) {
        this.matiere = matiere;
    }
    
    public Tuteur getTuteur() {
        return tuteur;
    }

    public void setTuteur(Tuteur tutor) {
        this.tuteur = tutor;
    }
    
    public void setcurrentStudents(int currentStudents) {
        this.currentStudents = currentStudents;
    }
    public int getcurrentStudents() {
    	return currentStudents;
    }

    @Override
    public String toString() {
        return "TutoringSession{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", subject='" + subject + '\'' +
                ", tutor='" + tutor + '\'' +
                ", lessonTitle='" + lessonTitle + '\'' +
                ", sessionPoints=" + sessionPoints +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", Tutorid='" + tuteur.getId() + '\''+
                '}';
    }
}