package controllers;

import controllers.enums.Permissions;
import data.services.AuthService;
import domain.enums.UserRole;
import domain.interfaces.ManagedClass;
import domain.models.AuthUser;
import domain.models.Session;
import managers.ControllerManager;

/**
 * Handles all Authentication.
 */
public class AuthController implements ManagedClass {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    public boolean isLoggedIn() {
        return service.getCurrentUser() != null;
    }

    public boolean login(String username, String password) {
        return service.login(username, password);
    }

    public void logout() {
        service.logout();
    }

    public AuthUser getCurrentUser(){
        return service.getCurrentUser();
    }

    public Permissions getSessionPermissions(Session s) {
        var authuser = service.getCurrentUser();//AuthUser
        var user = ControllerManager.getUserController().getByUsername(authuser.getUsername());//Normal user data

        if (user == null)//If no user is found for some reason
            return Permissions.BEGONE;

        if (authuser.getUsername().equals(s.getFirstLeaderUsername()) || user.getUserRole() == UserRole.HEADADMIN)//If user is the leader of the session or headadmin, caa edit
            return Permissions.EDIT;
        return Permissions.READ;//Else just read
    }

}
