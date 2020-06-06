package domain.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RegisteredUser")
public class RegisteredUser implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "SessionId")
    private Session session;

    @Id
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "HasAttended")
    private boolean hasAttended;

    public RegisteredUser() {
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasAttended() {
        return hasAttended;
    }

    public void setHasAttended(boolean hasAttended) {
        this.hasAttended = hasAttended;
    }
}
