package data.services;

import data.dao.SessionLeaderDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.SessionLeader;
import io.reactivex.Observable;

import java.util.Set;

/**
 * Service class used to perform methods involving the Session Class.
 */
public class SessionLeaderService implements CRUDService<SessionLeader>, ManagedClass {

    // MOCK DATA
    // private SessionRepository repo;
    private final SessionLeaderDao sessionLeaderDao;

    public SessionLeaderService(SessionLeaderDao sessionLeaderDao) {
        this.sessionLeaderDao = sessionLeaderDao;
    }

    @Override
    public boolean persist(SessionLeader s) {
        try {
            sessionLeaderDao.openCurrentSessionWithTransaction();
            sessionLeaderDao.persist(s);
            sessionLeaderDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<SessionLeader> findLeaders(int sessionId) {
        sessionLeaderDao.openCurrentSession();
        Set<SessionLeader> session = sessionLeaderDao.findLeaders(sessionId);
        sessionLeaderDao.closeCurrentSession();
        return session;
    }

    public Set<SessionLeader> findSessions(int leaderId) {
        sessionLeaderDao.openCurrentSession();
        Set<SessionLeader> session = sessionLeaderDao.findSessions(leaderId);
        sessionLeaderDao.closeCurrentSession();
        return session;
    }

    public SessionLeader findSessionLeader(int leaderId, int sessionId) {
        sessionLeaderDao.openCurrentSession();
        SessionLeader session = sessionLeaderDao.findById(leaderId, sessionId);
        sessionLeaderDao.closeCurrentSession();
        return session;
    }

    @Override
    public Set<SessionLeader> findAll() {
        //return repo.getAll();
        sessionLeaderDao.openCurrentSession();
        //unmodifiable cus currentsession doesnt have a transaction open
        Set<SessionLeader> sessions = sessionLeaderDao.findAll();
        //Refactor for filters
        sessionLeaderDao.closeCurrentSession();
        return sessions;
    }

    @Override
    public boolean update(SessionLeader s) {
        try {
            sessionLeaderDao.openCurrentSessionWithTransaction();
            sessionLeaderDao.update(s);
            sessionLeaderDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Unimplemented, should not be used. Use the version with 2 parameters instead.
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.sessionLeaderDao.getChangeListener();
    }

    public boolean delete(int userId, int sessionId) {
        try {
            sessionLeaderDao.openCurrentSessionWithTransaction();
            SessionLeader session = sessionLeaderDao.findById(userId, sessionId);
            sessionLeaderDao.delete(session);
            sessionLeaderDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
