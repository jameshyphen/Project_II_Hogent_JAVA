package data.dao.interfaces;

import java.io.Serializable;
import java.util.Set;

public abstract class ManyToManyDaoAdapter<T, Id1 extends Serializable, Id2 extends Serializable> extends DaoBaseImpl<T, Integer> {

    @Override
    public abstract void persist(T entity);

    @Override
    public abstract void update(T entity);

    /**
     * Unused method in ManyToMany implementations, DO NOT USE.
     *
     * @param integer Nope
     * @return Still nope
     */
    @Override
    public final T findById(Integer integer) {
        throw new UnsupportedOperationException();
    }

    public abstract T findById(Id1 id1, Id2 id2);

    @Override
    public abstract void delete(T entity);

    @Override
    public abstract Set<T> findAll();
}
