package data.services.interfaces;

import io.reactivex.Observable;

import java.util.Set;

/**
 * Defines a CRUD-style Service. Implementations provide their own type.
 *
 * @param <T> Generic type
 */
public interface CRUDService<T> {

    boolean persist(T t);

    Set<T> findAll();

    boolean update(T t);

    boolean delete(int id);

    /**
     * Optional method to retrieve the changeListener from the DAO. Not all Controllers need to provide access to this.
     *
     * @return Observable to signal changes.
     */
    Observable<Boolean> getChangeListener();

}
