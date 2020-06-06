package managers;

import data.dao.SessionDao;
import data.services.SessionService;
import domain.enums.ManagedType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InstanceManagerTest {
    private InstanceManager inst;

    public InstanceManagerTest() {
        inst = InstanceManager.get();
    }

    @Test
    void testGetRepository() {
        var instance = inst.getManagedInstance(ManagedType.SESSIONDAO);
        Assertions.assertNotNull(instance);
        Assertions.assertEquals(instance.getClass(), SessionDao.class);
    }

    @Test
    void testGetServiceWithExisting() {
        var instance = inst.getManagedInstance(ManagedType.SESSIONSERVICE);
        Assertions.assertNotNull(instance);
        Assertions.assertEquals(instance.getClass(), SessionService.class);
    }


}