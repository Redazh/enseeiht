package modele;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "matiere")
    private Set<Session> tutoringSessions;

    @ManyToMany(mappedBy = "competentSubjects")
    private Set<Tuteur> tutors ;

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Session> getTutoringSessions() {
        return tutoringSessions;
    }

    public void setTutoringSessions(Set<Session> tutoringSessions) {
        this.tutoringSessions = tutoringSessions;
    }

    public Set<Tuteur> getTutors() {
        return tutors;
    }

    public void setTutors(Set<Tuteur> tutors) {
        this.tutors = tutors;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}