package managers;

import controllers.*;
import domain.enums.ManagedType;

/**
 * Class that allows access from the frontend to the backend. Exposes the controllers.
 */
public class ControllerManager {

    /**
     * Returns the instance of SessionController.
     *
     * @return SessionController
     */
    public static SessionController getSessionController() {
        return (SessionController) InstanceManager.get().getManagedInstance(ManagedType.SESSIONCONTROLLER);
    }

    /**
     * Returns the instance of SessionController.
     *
     * @return SessionController
     */
    public static FeedbackController getFeedbackController() {
        return (FeedbackController) InstanceManager.get().getManagedInstance(ManagedType.FEEDBACKCONTROLLER);
    }

    /**
     * Returns the instance of SessionCalendarController.
     *
     * @return SessionCalendarController
     */
    public static SessionCalendarController getSessionCalendarController() {
        return (SessionCalendarController) InstanceManager.get().getManagedInstance(ManagedType.SESSIONCALENDARCONTROLLER);
    }

    /**
     * Returns the instance of AuthController.
     *
     * @return AuthController
     */
    public static AuthController getAuthController() {
        return (AuthController) InstanceManager.get().getManagedInstance(ManagedType.AUTHCONTROLLER);
    }

    /**
     * Returns the instance of UserController.
     *
     * @return UserController
     */
    public static UserController getUserController() {
        return (controllers.UserController) InstanceManager.get().getManagedInstance(ManagedType.USERCONTROLLER);
    }

    /**
     * Returns the instance of StartupController.
     *
     * @return StartupController
     */
    public static StartupController getStartupController() {
        return (controllers.StartupController) InstanceManager.get().getManagedInstance(ManagedType.STARTUPCONTROLLER);
    }

    public static SessionLeaderController getSessionLeaderController() {
        return (controllers.SessionLeaderController) InstanceManager.get().getManagedInstance(ManagedType.SESSIONLEADERCONTROLLER);
    }


    public static RegisteredUserController getRegisteredUserController() {
        return (controllers.RegisteredUserController) InstanceManager.get().getManagedInstance(ManagedType.REGISTEREDUSERCONTROLLER);
    }
}
