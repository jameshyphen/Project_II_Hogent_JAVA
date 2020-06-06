package domain.enums;

import controllers.*;
import data.dao.*;
import data.services.*;

public enum ManagedType {
    SESSIONDAO(SessionDao.class),
    SESSIONSERVICE(SessionService.class),
    SESSIONCONTROLLER(SessionController.class),

    FEEDBACKDAO(FeedbackDao.class),
    FEEDBACKSERVICE(FeedbackService.class),
    FEEDBACKCONTROLLER(FeedbackController.class),

    USERDAO(UserDao.class),
//    USERREPOSITORY(null),
    USERSERVICE(UserService.class),
    USERCONTROLLER(UserController.class),

    SESSIONCALENDARDAO(SessionCalendarDao.class),
//    SESSIONCALENDERREPOSITORY(null),
    SESSIONCALENDARSERVICE(SessionCalendarService.class),
    SESSIONCALENDARCONTROLLER(SessionCalendarController.class),

    AUTHDAO(AuthDao.class),
    AUTHSERVICE(AuthService.class),
    AUTHCONTROLLER(AuthController.class),

    REGISTEREDUSERDAO(RegisteredUserDao.class),
    REGISTEREDUSERSERVICE(RegisteredUserService.class),
    REGISTEREDUSERCONTROLLER(RegisteredUserController.class),

    SESSIONLEADERDAO(SessionLeaderDao.class),
    SESSIONLEADERSERVICE(SessionLeaderService.class),
    SESSIONLEADERCONTROLLER(SessionLeaderController.class),

    STARTUPCONTROLLER(StartupController.class);


    public final Class classRef;


    ManagedType(Class classRef) {
        this.classRef = classRef;
    }

    public static ManagedType fromClass(Class cl) {
        var list = ManagedType.values();
        for (ManagedType i : list) {
            if (i.classRef != null && i.classRef.equals(cl))
                return i;
        }
        return null;
    }
}
