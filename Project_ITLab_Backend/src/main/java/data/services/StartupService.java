package data.services;

import domain.interfaces.ManagedClass;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class StartupService {

    private static final Subject<Boolean> eagerLoader = PublishSubject.create();

    /**
     * Returns the pushing part of the eager loading subject.
     *
     * @return Subject
     */
    public static Subject<Boolean> getEagerLoaderPusher() {
        return eagerLoader;
    }

    /**
     * Returns the subscribing part of the eager loading subject.
     *
     * @return Observable
     */
    public static Observable<Boolean> getEagerLoaderPuller() {
        return eagerLoader;
    }


}
