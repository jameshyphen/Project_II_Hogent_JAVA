package managers;

import domain.enums.ManagedType;

public final class StartupManager {
    /**
     * Will preload all classes needed for eager loading.
     */
    public static void signalStartup() {
        var man = InstanceManager.get();
        man.getManagedInstance(ManagedType.AUTHSERVICE);
    }
}
