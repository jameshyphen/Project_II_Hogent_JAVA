package controllers;

import data.services.RegisteredUserService;
import data.services.SessionLeaderService;
import domain.interfaces.ManagedClass;
import domain.models.RegisteredUser;
import domain.models.SessionLeader;

import java.util.List;
import java.util.Set;

/**
 * Handles all data exchange involving the Session Class.
 */
public class RegisteredUserController implements ManagedClass {

    private final RegisteredUserService serv;

    public RegisteredUserController(RegisteredUserService s) {
        serv = s;
    }

    /**
     * Persists the given Session if it isn't already.
     *
     * @param s RegisteredUser to persist
     * @return True if successful, the RegisteredUser is valid and it doesn't exist already.
     */
    public boolean createSession(RegisteredUser s) {
        return serv.persist(s);
    }

    /**
     * Returns a set of all RegisteredUsers.
     *
     * @return List of SessionsLeaders.
     */
    public Set<RegisteredUser> getSessions() {
        return serv.findAll();
    }

    /**
     * Updates a RegisteredUser to the provided sessionleader. Searches for a matching ID to update the persisted Session.
     *
     * @param s New value for the SessionLeader.
     * @return True if successful, the RegisteredUser was found and it was successfully updated.
     */
    public boolean updateSession(RegisteredUser s) {
        return serv.update(s);
    }

    /**
     * Deletes the Session matching the provided ID.
     *
     * @param userId of the User, sessionId of Session.
     * @return True if successful, the RegisteredUser was found and it was successfully deleted.
     */
    public boolean deleteSession(int userId, int sessionId) {
        return serv.delete(userId, sessionId);
    }




}
