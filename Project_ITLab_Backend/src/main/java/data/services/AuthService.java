package data.services;

import data.dao.AuthDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.AuthUser;
import io.reactivex.Observable;

import java.util.Set;

public class AuthService implements CRUDService<AuthUser>, ManagedClass {
    private final AuthDao authDao;
    private AuthUser currentUser;
    private final Observable<Boolean> eagerLoading = StartupService.getEagerLoaderPuller();
    private Set<AuthUser> eagerLoadedSet;

    private boolean isLoadingFinished = false;

    public AuthService(AuthDao authDao) {
        this.authDao = authDao;

        eagerLoading.subscribe(s ->
                new Thread(() -> {
                    eagerLoadedSet = this.findAll();
                    isLoadingFinished = true;
                }).start()
        );
    }

    public boolean login(String username, String password) {
        AuthUser user;
        if (this.eagerLoadedSet != null && !this.eagerLoadedSet.isEmpty())
            user = eagerLoadedSet.stream().filter(s -> s.getUsername().equals(username)).findFirst().get();
        else {
            authDao.openCurrentSession();
            user = authDao.findByUsername(username);
            authDao.closeCurrentSession();
        }
        //If user isn't found, or the passwords don't match
        if (user == null || !user.comparePassword(password))
            return false;

        this.currentUser = user;

        return true;

    }

    public AuthUser getCurrentUser() {
        return this.currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    @Override
    public boolean persist(AuthUser authUser) {
        try {
            authDao.openCurrentSessionWithTransaction();
            authDao.persist(authUser);
            authDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<AuthUser> findAll() {
        Set<AuthUser> set;
        try {
            authDao.openCurrentSession();
            set = authDao.findAll();
            authDao.closeCurrentSession();
            return set;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public boolean update(AuthUser authUser) {
        try {
            authDao.openCurrentSessionWithTransaction();
            authDao.update(authUser);
            authDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            var user = authDao.findById(id);
            authDao.openCurrentSessionWithTransaction();
            authDao.delete(user);
            authDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.authDao.getChangeListener();
    }
}
