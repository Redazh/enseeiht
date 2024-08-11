package modele;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MessageToStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_student_id")
    private Etudiant receiverStudent;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Tuteur sender;

    // Le contenu du message
    private String content;

    public MessageToStudent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tuteur getSender() {
        return sender;
    }

    public void setSender(Tuteur sender) {
        this.sender = sender;
    }

    public Etudiant getReceiver() {
        return receiverStudent;
    }

    public void setReceiver(Etudiant receiver) {
        this.receiverStudent = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
