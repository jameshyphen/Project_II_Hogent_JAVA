package data.services;

import data.dao.SessionDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.Session;
import io.reactivex.Observable;

import java.util.Set;

/**
 * Service class used to perform methods involving the Session Class.
 */
public class SessionService implements CRUDService<Session>, ManagedClass {

    // MOCK DATA
    // private SessionRepository repo;
    private final SessionDao sessionDao;

    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public boolean persist(Session s) {
        try {
            sessionDao.openCurrentSessionWithTransaction();
            sessionDao.persist(s);
            sessionDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Session findById(int id) {
        sessionDao.openCurrentSession();
        Session session = sessionDao.findById(id);
        sessionDao.closeCurrentSession();
        return session;
    }

    public Set<Session> findAll() {
        //return repo.getAll();
        sessionDao.openCurrentSession();
        //unmodifiable cus currentsession doesnt have a transaction open
        Set<Session> sessions = sessionDao.findAll();
        //Refactor for filters
        sessionDao.closeCurrentSession();
        return sessions;
    }

    public boolean update(Session s) {
        try {
            sessionDao.openCurrentSessionWithTransaction();
            sessionDao.update(s);
            sessionDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            sessionDao.openCurrentSessionWithTransaction();
            Session session = sessionDao.findById(id);
            sessionDao.delete(session);
            sessionDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Observable<Boolean> getChangeListener() {
        return this.sessionDao.getChangeListener();
    }
}
