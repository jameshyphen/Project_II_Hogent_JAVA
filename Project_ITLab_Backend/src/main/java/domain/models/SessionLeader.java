package domain.models;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SessionLeader")
public class SessionLeader implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "SessionId")
    private Session session;

    @Id
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "IsSessionLeader")
    private boolean isSessionLeader;

    public SessionLeader() {
    }

    public SessionLeader(User leader, Session session){
        this.user=leader;
        this.session=session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getLeader() {
        return user;
    }

    public void setLeader(User user) {
        this.user = user;
    }

    public boolean isSessionLeader() {
        return isSessionLeader;
    }

    public void setIsSessionLeader(boolean isSessionLeader) {
        this.isSessionLeader = isSessionLeader;
    }
}
