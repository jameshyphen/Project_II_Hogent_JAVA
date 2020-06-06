package domain.models;

import org.springframework.context.annotation.ComponentScan;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Calendars")
public class SessionCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SessionCalendarId")
    private int id;

    @Column(name = "startDate")
    private LocalDateTime startCalendar;

    @Column(name = "endDate")
    private LocalDateTime endCalendar;

    @Transient
    private String startingDate = this.getStartingDate();

    @Transient
    private String endingDate = this.getEndingDate();

    public String getStartingDate() {
        if(this.startCalendar==null)
            return "No starting date set (?)";
        //parse beautiful date and return
        return this.startCalendar.toString();
    }

    public String getEndingDate() {
        if(this.startCalendar==null)
            return "No ending date set (?)";
        //parse beautiful date and return
        return this.endCalendar.toString();
    }


    public SessionCalendar() {

    }

    public SessionCalendar(LocalDateTime start, LocalDateTime end) {
        this.startCalendar = start;
        this.endCalendar = end;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean overlaps(SessionCalendar sc) {
        //this.before sc
        if (startCalendar.isBefore(sc.getStart()))
            if (endCalendar.isBefore(sc.getStart()))
                return false;
            else if (startCalendar.isAfter(sc.getStart()))
                if (startCalendar.isAfter(sc.getEnd()))
                    return false;

        return true;
    }

    /**
     * Checks if the given date is within the scope of this calendar.
     *
     * @param date Date to check
     * @return True if the date is between the start and end date of this calendar
     */
    public boolean containsDate(LocalDateTime date) {
        return this.startCalendar.isBefore(date) && this.endCalendar.isAfter(date);
    }

    public void setStart(LocalDateTime set) {
        this.startCalendar = set;
    }

    public void setEnd(LocalDateTime set) {
        this.endCalendar = set;
    }

    public LocalDateTime getStart() {
        return this.startCalendar;
    }

    public LocalDateTime getEnd() {
        return this.endCalendar;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("%d - %d", startCalendar.getYear(), endCalendar.getYear());
    }
}
