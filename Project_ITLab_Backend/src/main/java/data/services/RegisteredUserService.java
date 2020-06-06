package data.services;

import data.dao.RegisteredUserDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.RegisteredUser;
import io.reactivex.Observable;

import java.util.Set;

/**
 * Service class used to perform methods involving the Session Class.
 */
public class RegisteredUserService implements CRUDService<RegisteredUser>, ManagedClass {

    // MOCK DATA
    // private SessionRepository repo;
    private final RegisteredUserDao registeredUserDao;

    public RegisteredUserService(RegisteredUserDao registeredUserDao) {
        this.registeredUserDao = registeredUserDao;
    }

    public boolean persist(RegisteredUser s) {
        try {
            registeredUserDao.openCurrentSessionWithTransaction();
            registeredUserDao.persist(s);
            registeredUserDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<RegisteredUser> findLeaders(int sessionId) {
        registeredUserDao.openCurrentSession();
        Set<RegisteredUser> session = registeredUserDao.findRegisteredUsers(sessionId);
        registeredUserDao.closeCurrentSession();
        return session;
    }

    public Set<RegisteredUser> findSessions(int userId) {
        registeredUserDao.openCurrentSession();
        Set<RegisteredUser> sessions = registeredUserDao.findRegisteredSessions(userId);
        registeredUserDao.closeCurrentSession();
        return sessions;
    }

    public RegisteredUser findRegisteredUser(int userId, int sessionId) {
        registeredUserDao.openCurrentSession();
        RegisteredUser session = registeredUserDao.findById(userId, sessionId);
        registeredUserDao.closeCurrentSession();
        return session;
    }

    public Set<RegisteredUser> findAll() {
        //return repo.getAll();
        registeredUserDao.openCurrentSession();
        //unmodifiable cus currentsession doesnt have a transaction open
        Set<RegisteredUser> sessions = registeredUserDao.findAll();
        //Refactor for filters
        registeredUserDao.closeCurrentSession();
        return sessions;
    }

    public boolean update(RegisteredUser s) {
        try {
            registeredUserDao.openCurrentSessionWithTransaction();
            registeredUserDao.update(s);
            registeredUserDao.closeCurrentSessionWithTransaction();
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
        return this.registeredUserDao.getChangeListener();
    }

    public boolean delete(int userId, int sessionId) {
        try {
            registeredUserDao.openCurrentSessionWithTransaction();
            RegisteredUser session = registeredUserDao.findById(userId, sessionId);
            registeredUserDao.delete(session);
            registeredUserDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
