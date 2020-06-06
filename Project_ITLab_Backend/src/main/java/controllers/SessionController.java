package controllers;

import controllers.DTO.SessionDTO;
import controllers.interfaces.ChangeListenerProvider;
import data.services.SessionService;
import domain.enums.SessionState;
import domain.interfaces.ManagedClass;
import domain.models.Session;
import io.reactivex.Observable;
//import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

/**
 * Handles all data exchange involving the Session Class.
 */
public class SessionController implements ManagedClass, ChangeListenerProvider {

    private final SessionService serv;

    public SessionController(SessionService s) {
        serv = s;
    }

    /**
     * Persists the given Session if it isn't already.
     *
     * @param dto SessionDTO to persist
     * @return True if successful, the Session is valid and it doesn't exist already.
     */
    public boolean createSession(SessionDTO dto) {
        var s = copySessionData(new Session(), dto);
        return serv.persist(s);
    }

    /**
     * Returns a List of all Sessions.
     *
     * @return List of Sessions.
     */
    public Set<Session> getSessions() {
        return serv.findAll();
    }

    /**
     * Updates a Session to the provided session. Searches for a matching ID to update the persisted Session.
     *
     * @param dto DTO with the new value for the Session.
     * @return True if successful, the Session was found and it was successfully updated.
     */
    public boolean updateSession(SessionDTO dto) {
        try {
            return serv.update(copySessionData(serv.findById(dto.id), dto));
        } catch (Exception e) {
            return false;
            //TODO werken met error in exception of gwn yeeten?
        }
    }

    private Session copySessionData(Session ses, SessionDTO dto) {

        ses.setTitle(dto.title);
        ses.setLeader(dto.leader);//TODO DZHEM FIX IT
        ses.setStartDate(dto.startDate);
        ses.setEndDate(dto.endDate);
        ses.setRoom(dto.room);
        ses.setSpeaker(dto.speaker);
        ses.setDescription(dto.description);
        ses.setMaxAttendees(dto.maxAttendees);
        ses.reminder = dto.reminder;
        ses.setReminderDate(dto.reminderDate);

        if(ses.getState() == null)
            ses.setState(SessionState.JOINABLE);
        return ses;
    }

    /**
     * Deletes the Session matching the provided ID.
     *
     * @param id ID of the Session.
     * @return True if successful, the Session was found and it was successfully deleted.
     */
    public boolean deleteSession(int id) {
        return serv.delete(id);
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.serv.getChangeListener();
    }


}
