package domain.interfaces;

import java.util.List;
import java.util.Set;

/**
 * Generic implementation for a Repository type.  For implementation, include the typing in the class signature. Each implementation should include a collection in some form.
 *
 * @param <T> Typing for this repository.
 */
public interface Repository<T> {
    /**
     * Get T by ID. GET.
     *
     * @return T
     */
    T getById(int t);

    /**
     * Get all objects in repository. GET.
     *
     * @return List of objects.
     */
    Set<T> getAll();

    /**
     * Adds a new object to the repository. POST.
     *
     * @param t Object to add.
     */
    void create(T t);

    /**
     * Updated an object in the repository. Replaces the earlier object. PUT.
     *
     * @param t Object to update. New value.
     */
    void update(T t);

    /**
     * Deletes the given object from the repository. DELETE.
     *
     * @param t Object to delete.
     */
    void delete(T t);

    /**
     * Saves changes.
     */
    void saveChanges();
}
