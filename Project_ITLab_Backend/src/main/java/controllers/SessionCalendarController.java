package controllers;

import controllers.DTO.SessionCalendarDTO;
import controllers.interfaces.ChangeListenerProvider;
import data.services.SessionCalendarService;
import domain.interfaces.ManagedClass;
import domain.models.SessionCalendar;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

public class SessionCalendarController implements ManagedClass, ChangeListenerProvider {
    private final SessionCalendarService serv;

    public SessionCalendarController(SessionCalendarService s) {
        serv = s;
    }

    /**
     * Persists the given SessionCalendar if it isn't already.
     *
     * @param s Session to persist
     * @return True if successful, the SessionCalendar is valid and it doesn't exist already.
     */
    public boolean createSessionCalendar(SessionCalendar s) {
        return serv.persist(s);
    }

    /**
     * Returns a List of all SessionCalendars.
     *
     * @return List of SessionCalendars.
     */
    public Set<SessionCalendar> getSessionCalendars() {
        return serv.findAll();
    }

    /**
     * Updates a SessionCalendar to the provided sessionCalendar. Searches for a matching ID to update the persisted SessionCalendar.
     *
     * @param s New value for the SessionCalendar.
     * @return True if successful, the SessionCalendar was found and it was successfully updated.
     */
    public boolean updateSessionCalendar(SessionCalendar s) {
        return serv.update(s);
    }

    /**
     * Deletes the SessionCalendar matching the provided ID.
     *
     * @param id ID of the SessionCalendar.
     * @return True if successful, the SessionCalendar was found and it was successfully deleted.
     */
    public boolean deleteSessionCalendar(int id) {
        return serv.delete(id);
    }

    /**
     * Returns the ChangeListener associated with this object.
     *
     * @return Observable that signals changes.
     */
    @Override
    public Observable<Boolean> getChangeListener() {
        return serv.getChangeListener();
    }
}
