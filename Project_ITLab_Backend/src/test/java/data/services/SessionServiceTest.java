package data.services;

import data.dao.SessionDao;
import domain.models.Session;
import domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SessionService ser;
    private SessionDao mock;
    private User user = new User("Testing", "Mocks","dh@aa.c", "24123512123");
    private final List<Session> ses = new ArrayList<>(Arrays.asList(
            new Session(user, "Hacking", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100),
            new Session(user, "Another Hacking", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100),
            new Session(user, "Once More Hacking", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100),
            new Session(user, "Hacking 2.0", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100),
            new Session(user, "Hacking For A Change", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100),
            new Session(user, "Hacking For Dummies", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100)));

    private final Session validSession = new Session(user, "First Hacking", "B.1013", LocalDateTime.now(), LocalDateTime.now(), 100);

    public SessionServiceTest() {
        trainMock();
    }

    @BeforeEach
    void setUp() {
        ser = new SessionService(mock);
    }

    @Test
    void createSessionValidReturnsTrue() {
        assertTrue(ser.persist(validSession));
        Mockito.verify(mock).persist(validSession);
    }

    @Test
    void createSessionNullReturnsFalse() {
        assertFalse(ser.persist(null));
        Mockito.verify(mock).persist(null);
    }

    @Test
    @Disabled("Sessions not fully implemented FUCK YOU TIJL THIS IS HOW IT IS DONE.")
    void getSessions() {
    }

    @Test
    @Disabled("Sessions not fully implemented FUCK YOU TIJL THIS IS HOW IT IS DONE.")
    void updateSessionValid() {
    }

    @Test
    void updateSessionNullReturnsFalse() {
        assertFalse(ser.update(null));
        Mockito.verify(mock).update(null);
    }

    @Test
    void deleteSessionValidReturnsTrue() {
        assertTrue(ser.delete(1));
        Mockito.verify(mock).delete(ses.get(0));
    }

    @Test
    void deleteSessionNullReturnsFalse() {
        assertFalse(ser.delete(-1));
        Mockito.verify(mock).delete(ses.get(-1));
    }

    private void trainMock() {
        mock = mock(SessionDao.class);

        //Train getAll
        when(mock.findAll()).thenReturn((Set<Session>) ses);

        //Train all GetById
        IntStream.range(0, ses.size()).forEach(s ->
                when(mock.findById(ses.get(s).getId())).thenReturn(ses.get(s))
        );

        doThrow(IllegalArgumentException.class).when(mock).persist(null);
        doThrow(IllegalArgumentException.class).when(mock).delete(null);
        doThrow(IllegalArgumentException.class).when(mock).update(null);
    }

}