package data.services;

import data.dao.UserDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.User;
import io.reactivex.Observable;

import java.util.Set;

/**
 * Service class used to perform methods involving the Session Class.
 */
public class UserService implements CRUDService<User>, ManagedClass {

    // MOCK DATA
    // private SessionRepository repo;
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean persist(User user) {
        try {
            userDao.openCurrentSessionWithTransaction();
            userDao.persist(user);
            userDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User findById(int id) {
        userDao.openCurrentSession();
        User user = userDao.findById(id);
        userDao.closeCurrentSession();
        return user;
    }

    public User findByUsername(String username) {
        userDao.openCurrentSession();
        User user = userDao.findByUsername(username);
        userDao.closeCurrentSession();
        return user;
    }

    public Set<User> findAll() {
        //return repo.getAll();
        userDao.openCurrentSession();
        //unmodifiable cus currentsession doesnt have a transaction open
        Set<User> users = userDao.findAll();
        //Refactor for filters
        userDao.closeCurrentSession();
        return users;
    }

    public boolean update(User s) {
        try {
            userDao.openCurrentSessionWithTransaction();
            userDao.update(s);
            userDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            userDao.openCurrentSessionWithTransaction();
            User user = userDao.findById(id);
            userDao.delete(user);
            userDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.userDao.getChangeListener();
    }
}
