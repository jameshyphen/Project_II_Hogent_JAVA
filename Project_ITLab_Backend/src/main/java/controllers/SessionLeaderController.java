package controllers;

import data.services.SessionLeaderService;
import data.services.SessionService;
import domain.interfaces.ManagedClass;
import domain.models.Session;
import domain.models.SessionLeader;
//import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

/**
 * Handles all data exchange involving the Session Class.
 */
public class SessionLeaderController implements ManagedClass {

    private final SessionLeaderService serv;

    public SessionLeaderController(SessionLeaderService s) {
        serv = s;
    }

    /**
     * Persists the given Session if it isn't already.
     *
     * @param s SessionLeader to persist
     * @return True if successful, the SessionLeader is valid and it doesn't exist already.
     */
    public boolean createSession(SessionLeader s) {
        return serv.persist(s);
    }

    /**
     * Returns a set of all SessionsLeaders.
     *
     * @return List of SessionsLeaders.
     */
    public Set<SessionLeader> getSessions() {
        return serv.findAll();
    }

    /**
     * Updates a SessionLeader to the provided sessionleader. Searches for a matching ID to update the persisted Session.
     *
     * @param s New value for the SessionLeader.
     * @return True if successful, the SessionLeader was found and it was successfully updated.
     */
    public boolean updateSession(SessionLeader s) {
        return serv.update(s);
    }

    /**
     * Deletes the Session matching the provided ID.
     *
     * @param userId of the User, sessionId of Session.
     * @return True if successful, the SessionLeader was found and it was successfully deleted.
     */
    public boolean deleteSession(int userId, int sessionId) {
        return serv.delete(userId, sessionId);
    }



}
