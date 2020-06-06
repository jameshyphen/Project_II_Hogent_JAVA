package domain.models;

import domain.enums.SessionState;
import domain.enums.UserStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity
@Table(name = "Sessions")
public class Session implements Serializable {
    @Id
    @Column(name = "SessionId", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Status", nullable = false)
    private SessionState state;

    @Column(name = "Name", nullable = false)
    private String title;

    @Column(name = "Speaker")
    private String speaker;

    @Column(name = "Room", nullable = false)
    private String room;

    @Column(name = "StartTime", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndTime", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "MaxAttendees", nullable = false)
    private int maxAttendees;

    @Transient
    public boolean reminder;

    @Transient
    private LocalDateTime reminderDate;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SessionLeader> sessionLeaders = new HashSet<>();

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RegisteredUser> registeredUsers = new HashSet<>();

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Announcement> announcements = new HashSet<>();

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @Column(name = "Description")
    private String description;

    @Transient
    private static String uploadDirectory = System.getProperty("user.dir") + "/media/sessions";

    @Transient
    private String firstLeader = this.getFirstLeader();

    @Transient
    private int amountOfRegisters = this.getAmountOfRegisters();
//
//    public int getAttendingCount() {
//        return getAmountOfRegisters();
//    }

    public String getFirstLeader() {
//        if (sessionLeaders != null && !sessionLeaders.isEmpty())
//            return this.sessionLeaders.iterator().next().getLeader().getFullName();
//        return "No leader(?)";
        return this.sessionLeaders.stream().map(l -> l.getLeader().getFullName()).findFirst().orElse("No leader(?)");
    }

    public int getAmountOfRegisters() {
        // if(sessionLeaders!=null)
        // return this.sessionLeaders.iterator().next().getLeader().getFullName();
        // return "No leader(?)";
        return this.registeredUsers.size();
    }

    public String getFirstLeaderUsername() {
//        if(sessionLeaders!=null && !sessionLeaders.isEmpty())
//            return this.sessionLeaders.iterator().next().getLeader().getUsername();
//        return "No leader(?)";
        return this.sessionLeaders.stream().map(l -> l.getLeader().getUsername()).findFirst().orElse("No leader(?)");
    }


    public Session() {
        setState(SessionState.JOINABLE);
    }

    public Session(User leader, String title, String room, LocalDateTime startDate, LocalDateTime endDate, int maxAttendees) {
        this();
        this.title = title;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxAttendees = maxAttendees;
        setState(SessionState.JOINABLE);
        new File(uploadDirectory).mkdir();
        this.sessionLeaders.add(new SessionLeader(leader, this));
        this.firstLeader = leader.getFullName();
    }

    //new Session(1, , "Hacking", "Donald", "B.1013",LocalDateTime.now(), LocalDateTime.now(), 100)
    public Session(User leader, String title, String description, String speaker, String room,
                   LocalDateTime startDate, LocalDateTime endDate, Integer maxAttendees,
                   boolean reminder, LocalDateTime reminderDate) throws Exception {

        this(leader, title, room, startDate, endDate, maxAttendees);
        if (leader == null || title == null || description == null ||
                room == null || startDate == null || endDate == null || maxAttendees == null)
            throw new Exception("Een of meer nodige velden waren niet ingevuld.");

        this.speaker = speaker;
        this.reminder = reminder;
        this.reminderDate = reminderDate;
        this.description = description;
        this.firstLeader = leader.getFullName();
    }

    public boolean sendReminder(LocalDateTime ldt) {
        if (!reminder)
            return false;
        // Insert ITLab mail here
        String from = "projectitlab@gmail.com";

        // Insert ITLab smtp here
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        javax.mail.Session ses = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                // Dit is een mail dat ik heb aangemaakt voor dit project. En is het dus niet erg dat mail/password in github staat, want ik zal deze mail toch deleten na dit project.
                return new PasswordAuthentication("projectitlab@gmail.com", "Heimdal!");

            }

        });

        for (RegisteredUser u : registeredUsers) {
            // Recipient's email ID needs to be mentioned.
            String to = u.getUser().getEmail();

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(ses);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // reminderdate
                Date in = new Date();
                Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                message.setSentDate(out);

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("ITLab reminder " + title);

                // Now set the actual message
                message.setText("ITLab " + title + " occurs at " + startDate.toString());

                // Send message
                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Session))
            return false;
        Session ses = (Session) obj;
        // return this.id == ses.getId();
        return true;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return this.description;
    }

    public SessionState getState() {
        return state;
    }

    public Set<SessionLeader> getLeaders() {
        return sessionLeaders;
    }

    public Set<RegisteredUser> getRegisteredUsers() {
        return registeredUsers;
    }


    public Boolean getReminder() {
        return reminder;
    }

    public String getTitle() {
        return title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getRoom() {
        return room;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void updateFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
        feedbacks.add(feedback);
    }

    public void removeFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void upload(List<MultipartFile> media) {
        StringBuilder filenames = new StringBuilder();
        for (MultipartFile file : media) {
            Path pathNameAndFile = Paths.get(uploadDirectory, file.getOriginalFilename());
            filenames.append(file.getOriginalFilename());
            try {
                Files.write(pathNameAndFile, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addSessionLeader(User leader) {
        if (leader == null)
            throw new IllegalArgumentException("Gelieve een verantwoordelijkea mee te geven");
        SessionLeader sessionLeader = new SessionLeader();
        sessionLeader.setLeader(leader);
        sessionLeader.setSession(this);
        sessionLeader.setIsSessionLeader(true);
        this.sessionLeaders.add(sessionLeader);
    }

    public void removeLeader(User leader) {
        if (leader == null)
            throw new IllegalArgumentException("Gelieve verantwoordelijke in te vullen");
        SessionLeader toRemove = this.sessionLeaders.stream().filter(x -> x.getLeader().equals(leader)).findFirst().get();
        this.sessionLeaders.remove(toRemove);

    }

    //Deze boolean moet true retourneren als je IETS wilt doen van aanpassingen.
    public boolean isOpen() {
        return this.state == SessionState.RUNNING;
    }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException("Gelieve titel in te vullen");
        this.title = title;
    }

    public void setSpeaker(String speaker) {
//        if (speaker == null)
//            throw new IllegalArgumentException("Gelieve gastspreker in te vullen");
        this.speaker = speaker;
    }

    public void setRoom(String room) {
        if (room == null)
            throw new IllegalArgumentException("Gelieve lokaal in te vullen");
        this.room = room;
    }

    public void setStartDate(LocalDateTime date) {
        if (this.endDate != null)
            if (!date.isBefore(this.endDate.minusMinutes(30)))
                throw new IllegalArgumentException("Startdatum moet VOOR eindatum zijn");
        if (date.isBefore(LocalDateTime.now().minusDays(1)))
            throw new IllegalArgumentException("Startdatum moet minstens 1 dag in de toekomst liggen");
        this.startDate = date;
    }

    public void setEndDate(LocalDateTime date) {
        if (date.isBefore(this.startDate.plusMinutes(30)) && this.startDate != null)
            throw new IllegalArgumentException("Einddatum moet na startdatum zijn");
        this.endDate = date;
    }

    public void setMaxAttendees(int max) {
        if (max == 0)
            throw new IllegalArgumentException("Gelieve de max aantal deelnemers te geven");
        this.maxAttendees = max;
    }

    public void setReminderDate(LocalDateTime date) {
        if (date == null) {
            this.reminderDate = null;
            return;
        }
//        if (date.isBefore(date.plusMinutes(60))) {
//            this.reminderDate = null;
//            return;
//        }
        if (date.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("ReminderDate mag niet in het verleden zijn");
        this.reminderDate = date;
    }

    public void addRegisteredUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("Gelieve een gebruiker mee te geven");
        RegisteredUser regUser = new RegisteredUser();
        regUser.setUser(user);
        regUser.setSession(this);
        regUser.setHasAttended(false);
        this.registeredUsers.add(regUser);
    }

    public void addAnnouncement(Announcement announcement) {
        this.announcements.add(announcement);
    }

    public void addFeedback(Feedback feedback) {
        if (feedback.getMessage() == null || feedback == null || feedback.getAuthor() == null)
            throw new IllegalArgumentException("Feedback mag niet leeg zijn");
        this.feedbacks.add(feedback);
    }

    public void setLeader(User leader) {
        this.sessionLeaders.add(new SessionLeader(leader, this));
    }
}
