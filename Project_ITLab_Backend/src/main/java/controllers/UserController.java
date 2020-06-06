package controllers;

import controllers.DTO.UserDTO;
import data.services.UserService;
import domain.interfaces.ManagedClass;
import domain.models.User;
import io.reactivex.Observable;

import java.util.Set;

/**
 * Handles all data exchange involving the User Class.
 */
public class UserController implements ManagedClass {
    private final UserService serv;

    public UserController(UserService serv) {
        this.serv = serv;
    }

    public Set<User> getAllUsers() {
        return serv.findAll();
    }

    public User getById(int id) {
        return serv.findById(id);
    }

    public User getByUsername(String username) {
        return serv.findByUsername(username);
    }

    public boolean createUser(User user) {
        return serv.persist(user);
    }

    /**
     * Updates a User. Will copy the data from the DTO to the corresponding User object.
     *
     * @param user DTO containing the new values.
     * @return True if the change is successful.
     */
    public boolean updateUser(UserDTO user) {
        var oldUser = getById(user.id);
        return oldUser == null
                ? false
                : serv.update(copyUser(user, oldUser));//Copies new data to User object and persists it
//        if (oldUser == null)
//            return false;
//        oldUser = copyUser(user, oldUser);
//        return serv.update(oldUser);
    }


    public boolean deleteUser(int id) {
        return serv.delete(id);
    }

    public Observable<Boolean> getChangeListener() {
        return this.serv.getChangeListener();
    }

    private User copyUser(UserDTO user, User old) {
        old.setFirstName(user.firstName);
        old.setLastName(user.lastName);
        old.setUsername(user.username);
        old.setUserRole(user.role);
        old.setUserState(user.status);
        old.upload(user.mpf);

        return old;
    }
}
