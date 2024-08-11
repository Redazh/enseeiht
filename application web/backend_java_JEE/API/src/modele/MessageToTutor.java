package modele;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MessageToTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_tutor_id")
    private Tuteur receiverTutor;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Etudiant sender;

    // Le contenu du message
    private String content;
    

    public MessageToTutor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getSender() {
        return sender;
    }

    public void setSender(Etudiant sender) {
        this.sender = sender;
    }

    public Tuteur getReceiver() {
        return receiverTutor;
    }

    public void setReceiver(Tuteur receiver) {
        this.receiverTutor = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
