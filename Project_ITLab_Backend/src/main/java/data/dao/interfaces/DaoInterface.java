package data.dao.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface DaoInterface<T, Id extends Serializable> {

    void persist(T entity);

    void update(T entity);

    T findById(Id id);

    void delete(T entity);

    Set<T> findAll();

}