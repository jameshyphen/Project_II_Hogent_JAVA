package controllers;

import data.services.StartupService;
import domain.interfaces.ManagedClass;
import io.reactivex.subjects.Subject;

public class StartupController implements ManagedClass {

    public Subject<Boolean> getEagerLoadingSignal() {
        return StartupService.getEagerLoaderPusher();
    }
}
