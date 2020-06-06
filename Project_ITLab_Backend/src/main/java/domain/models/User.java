package domain.models;

import domain.enums.UserRole;
import domain.enums.UserStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "User")
@Table(name = "Users")
public class User {

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "CardNumber")
    private String cardNumber;


    @Column(name = "Status")
    private UserStatus status;

    @Column(name = "UserRole")
    private UserRole role;

    @Column(name = "ProfilePictureUrl")
    private String url;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private Set<RegisteredUser> registeredSessions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private Set<SessionLeader> sessionLeaders;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private Set<Announcement> announcements;

    @Column(name = "Username", nullable = false)
    private String username;

    @Column(name = "TimesAbsent")
    private int timesAbsent;

    @Transient
    private static String uploadDirectory = System.getProperty("user.dir") + "/media/users";

    public User() {
        sessionLeaders = new HashSet<>();
        registeredSessions = new HashSet<>();
        feedbacks = new HashSet<>();
        announcements = new HashSet<>();
        if (status == null)
            status = UserStatus.ACTIVE;
        if (role == null)
            role = UserRole.STUDENT;
        this.timesAbsent = 0;
    }

    public User(String firstName, String lastName, String email, String cardNumber) {
        this();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.username = (firstName + lastName).toLowerCase();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex))
            throw new IllegalArgumentException("Email isn't valid");

        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserStatus getUserStatus() {
        return status;
    }

    public UserRole getUserRole() {
        return role;
    }

    public void setUserState(UserStatus state) {
        if (state == null)
            throw new IllegalArgumentException("State isn't valid");
        this.status = state;
    }

    public void setUserRole(UserRole type) {
        if (type == UserRole.HEADADMIN)
            throw new IllegalArgumentException("You cannot set someone to headadmin via this application");
        if (type == null)
            throw new IllegalArgumentException("Role isn't valid");

        this.role = type;
    }

    public void setFirstName(String firstName) {
        if (firstName == null)
            throw new IllegalArgumentException("FirstName isn't valid");

        this.firstName = firstName;
    }

    public void upload(List<MultipartFile> media) {
        StringBuilder filenames = new StringBuilder();
        for (MultipartFile file : media) {
            url = file.getOriginalFilename();
            Path pathNameAndFile = Paths.get(uploadDirectory, url);
            filenames.append(url);
            try {
                Files.write(pathNameAndFile, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getLastName() {
        return lastName;
    }

    public String getUrl(){
        return url;
    }

    public void setLastName(String lastName) {
        if (lastName == null)
            throw new IllegalArgumentException("LastName isn't valid");

        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        boolean numeric = true;
        try {
            Double num = Double.parseDouble(cardNumber);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        if (!numeric)
            throw new IllegalArgumentException("CardNumber is not a number");

        this.cardNumber = cardNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null)
            throw new IllegalArgumentException("Username isn't valid");
        this.username = username;
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
}
