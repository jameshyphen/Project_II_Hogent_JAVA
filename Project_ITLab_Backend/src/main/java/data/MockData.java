package data;

import domain.models.AuthUser;
import domain.models.Session;
import domain.models.SessionCalendar;
import domain.models.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MockData {

    public static List<Session> getMockSessions() throws Exception {
        User nick = new User();
        nick.setFirstName("Nick");
        nick.setLastName("The Great and Magnificent");
        nick.setUsername("nicklambda");
        return Arrays.asList(new Session[]{
                new Session(nick, "How to not suck at Java", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at C#", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at HTML", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at CSS", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at Javascript", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at Angular", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at Life", "Description", "Nick, the Great and Magnificent", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now()),
                new Session(nick, "How to not suck at Contributing to your Project", "Description", "Janne, the Absent and Missing", "D4.20", LocalDateTime.of(2020, 6, 9, 4, 20, 0), LocalDateTime.of(2020, 6, 9, 6, 9, 0), 69, true, LocalDateTime.now())
        });
    }

    public static List<SessionCalendar> getMockCalendars() {
        return Arrays.asList(new SessionCalendar[]{
                        new SessionCalendar(LocalDateTime.of(2017, Month.SEPTEMBER,1,0,0),LocalDateTime.of(2018, Month.JUNE,30,23,59)),
                        new SessionCalendar(LocalDateTime.of(2018, Month.SEPTEMBER,1,0,0),LocalDateTime.of(2019, Month.JUNE,30,23,59)),
                        new SessionCalendar(LocalDateTime.of(2019, Month.SEPTEMBER,1,0,0),LocalDateTime.of(2020, Month.JUNE,30,23,59)),
        }
        );
    }


    public static List<AuthUser> getMockAuthUsers() {
        return Arrays.asList(new AuthUser[]{
                new AuthUser("dzhemaptula", "password"),
                new AuthUser("nicklersberghe", "123123"),
                new AuthUser("tijlzwartjes", "tijltijl")

                }
        );
    }
}
