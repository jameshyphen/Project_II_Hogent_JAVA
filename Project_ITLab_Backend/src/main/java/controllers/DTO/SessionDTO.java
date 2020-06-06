package controllers.DTO;

import domain.models.User;

import java.time.LocalDateTime;

public class SessionDTO {
    public final Integer id;

    public String title;

    public User leader;

    public LocalDateTime startDate;

    public LocalDateTime endDate;

    public String room;

    public String speaker;

    public String description;

    public int maxAttendees;

    public boolean reminder;

    public LocalDateTime reminderDate;

    public SessionDTO(int id) {
        this.id = id;
    }

    public SessionDTO() {
        this.id = null;
    }
}
