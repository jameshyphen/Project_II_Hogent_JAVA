package data.services;

import data.dao.SessionCalendarDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.SessionCalendar;
import io.reactivex.Observable;

import java.util.Set;

public class SessionCalendarService implements CRUDService<SessionCalendar>, ManagedClass {

    private final SessionCalendarDao dao;

    public SessionCalendarService(SessionCalendarDao dao) {
        this.dao = dao;
    }

    public boolean checkCalendarAvailability(SessionCalendar sc) {
        return dao.findAll().stream().noneMatch(c -> c.overlaps(sc));
    }

    @Override
    public boolean persist(SessionCalendar sc) {
        try {
            if (checkCalendarAvailability(sc))
                throw new IllegalArgumentException("Calendar overlaps existing calendar");

            dao.openCurrentSessionWithTransaction();
            dao.persist(sc);
            dao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<SessionCalendar> findAll() {
        dao.openCurrentSession();
        Set<SessionCalendar> calendars = dao.findAll();
        dao.closeCurrentSession();
        return calendars;
        //Refactor for filters
    }

    @Override
    public boolean update(SessionCalendar sc) {
        try {
            if (checkCalendarAvailability(sc))
                throw new IllegalArgumentException("Calendar overlaps existing calendar");

            dao.openCurrentSessionWithTransaction();
            dao.update(sc);
            dao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            dao.openCurrentSessionWithTransaction();
            SessionCalendar sc = dao.findById(id);
            dao.delete(sc);
            dao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.dao.getChangeListener();
    }
}
