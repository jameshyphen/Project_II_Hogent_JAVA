package managers;

import controllers.SessionController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ControllerManagerTest {

    @Test
    void getSessionController() {
        var cont = ControllerManager.getSessionController();
        Assertions.assertEquals(SessionController.class, cont.getClass());
        Assertions.assertEquals(cont, ControllerManager.getSessionController());
    }
}