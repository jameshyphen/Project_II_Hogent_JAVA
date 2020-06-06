package controllers.interfaces;

import io.reactivex.Observable;

public interface ChangeListenerProvider {
    /**
     * Returns the ChangeListener associated with this object.
     *
     * @return Observable that signals changes.
     */
    Observable<Boolean> getChangeListener();
}
