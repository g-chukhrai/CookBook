package by.chuger.cookbook.model.dao;

import java.util.Collection;

public interface BaseDAO<E> {

    void create(final E entity);

    E getByID(final Integer id);

    Collection<E> getAll();

    void delete(final Integer id);

    E getByName(final String name);
}
