package modele;

import javax.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "Tuteur")

public class Tuteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nom;

    @Column
    private String prenom;

    @Column(unique = true,name = "email")
    private String email;

    @Column
    private String motDePasse;
    
    @Column
    private String telephone;
    
    @Column
    private String adresse;
    
    @ManyToMany(fetch = FetchType.EAGER) 
    private Collection<Subject> competentSubjects;
    
    @OneToMany(mappedBy = "tuteur")
    private Collection<Session> tutoringSessions;
    
    
    public Tuteur () {
    	
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public Collection<Subject> getCompetentSubjects() {
        return competentSubjects;
    }

    public void setCompetentSubjects(Collection<Subject> competentSubjects) {
        this.competentSubjects = competentSubjects;
    }
    
    public Collection<Session> getTutoringSessions() {
        return tutoringSessions;
    }

    public void setTutoringSessions(Collection<Session> tutoringSessions) {
        this.tutoringSessions = tutoringSessions;
    }

}