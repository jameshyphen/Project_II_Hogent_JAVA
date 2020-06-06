package domain.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnnouncementId")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "UserId", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "SessionId", nullable = false)
    private Session session;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    @Column(name = "Message", nullable = false)
    private String message;

    public Announcement(User author, Session session, String message) {
        this.session = session;
        this.date = LocalDateTime.now();
        this.author = author;
        this.message = message;
    }

    public Announcement(){}

    public int getId(){ return id; }
    public LocalDateTime getDate() {
        return date;
    }
    public User getAuthor() {
        return author;
    }
    public Session getSession() { return session; }
    public String getMessage() {
        return message;
    }
}
