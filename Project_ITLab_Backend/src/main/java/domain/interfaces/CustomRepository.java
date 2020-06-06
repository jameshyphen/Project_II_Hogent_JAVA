package domain.interfaces;

import java.util.Set;

/**
 * Implementation of Repository that allows the dev to choose which methods they want to implement, should it be restricted in some way.
 *
 * @param <T> Typing of this Repository
 */
public abstract class CustomRepository<T> implements Repository<T> {

    @Override
    public abstract T getById(int t);

    @Override
    public abstract Set<T> getAll();

    @Override
    public abstract void create(T t);

    @Override
    public abstract void update(T t);

    @Override
    public abstract void delete(T t);

    @Override
    public abstract void saveChanges();
}
