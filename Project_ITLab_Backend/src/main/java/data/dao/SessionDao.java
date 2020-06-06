package data.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.dao.interfaces.DaoBaseImpl;
import domain.interfaces.ManagedClass;
import domain.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import data.dao.interfaces.DaoInterface;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class SessionDao extends DaoBaseImpl<Session, Integer> implements ManagedClass {

    @Override
    public void persist(Session entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Session entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public Session findById(Integer id) {
        String query = "SELECT c FROM Session c WHERE c.id = :sessionID";

        TypedQuery<Session> tq = getCurrentSession().createQuery(query, Session.class);
        tq.setParameter("sessionID", id);

        Session sess = null;
        try {
            sess = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return sess;
    }

    @Override
    public void delete(Session entity) {
        getCurrentSession().delete(entity);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Set<Session> findAll() {
        List<Session> sessions = (List<Session>) getCurrentSession().createQuery("from Session").list();
        Set set = new HashSet(sessions);
        return set;
    }
}