package domain.models;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackId")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "SessionId", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserId", nullable = false)
    private User author;

    @Column(name = "Message", nullable = false)
    private String message;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    @Column(name = "IsActive")
    private boolean isActive;

    public Feedback(User author, Session session, String message, LocalDateTime date) {
        this.session = session;
        this.author = author;
        this.message = message;
        this.date = date;
        this.isActive = true;

    }

    public Feedback() {
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public void removeFeedback() {
        this.isActive = false;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
