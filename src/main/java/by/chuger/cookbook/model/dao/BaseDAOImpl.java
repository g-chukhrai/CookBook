package by.chuger.cookbook.model.dao;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class BaseDAOImpl<E> extends HibernateDaoSupport implements BaseDAO<E> {

    private Class<E> entityClass;

    public BaseDAOImpl(final Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional(readOnly = false)
    public void create(final E entity) {
        getHibernateTemplate().setCheckWriteOperations(false);
        getHibernateTemplate().saveOrUpdate(entity);
    }

    @Override
    public E getByID(final Integer id) {
        E entity = getHibernateTemplate().get(entityClass, id);
        return entity;
    }

    @Override
    public Collection<E> getAll() {
        Collection<E> list = getSession().createQuery("from " + entityClass.getName()).list();
        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(final Integer id) {
        E entity = (E) getHibernateTemplate().get(entityClass, id);
        if (entity != null) {
            getHibernateTemplate().delete(entity);
        }
    }

    @Override
    public E getByName(final String name) {
        if (name != null) {
            Query query = getSession().createQuery("from " + entityClass.getName() + " where name = :name");
            query.setParameter("name", name);
            return (E) query.uniqueResult();
        }
        return null;
    }
}
