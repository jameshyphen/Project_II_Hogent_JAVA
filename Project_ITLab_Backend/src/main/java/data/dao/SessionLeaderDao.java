package data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.dao.interfaces.DaoBaseImpl;
import data.dao.interfaces.ManyToManyDaoAdapter;
import domain.interfaces.ManagedClass;
import domain.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import data.dao.interfaces.DaoInterface;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class SessionLeaderDao extends ManyToManyDaoAdapter<SessionLeader, Integer, Integer> implements ManagedClass {

    @Override
    public void persist(SessionLeader entity) {
        try {
            getCurrentSession().save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(SessionLeader entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public SessionLeader findById(Integer userId, Integer sessionId) {
        String query = "SELECT c FROM SessionLeader c WHERE c.user.id= :userID and c.session.id= :sessionID";

        TypedQuery<SessionLeader> tq = getCurrentSession().createQuery(query, SessionLeader.class);
        tq.setParameter("userID", userId);
        tq.setParameter("sessionID", sessionId);

        SessionLeader sess = null;
        try {
            sess = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return sess;
    }


    public Set<SessionLeader> findLeaders(Integer sessionId) {
        String query = "SELECT c FROM SessionLeader c WHERE c.session.id= :sessionID";

        TypedQuery<SessionLeader> tq = getCurrentSession().createQuery(query, SessionLeader.class);
        tq.setParameter("sessionID", sessionId);

        List<SessionLeader> sess = new ArrayList<>();
        try {
            sess = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return new HashSet(sess);
    }

    public Set<SessionLeader> findSessions(Integer userId) {
        String query = "SELECT c FROM SessionLeader c WHERE c.user.id= :userID";

        TypedQuery<SessionLeader> tq = getCurrentSession().createQuery(query, SessionLeader.class);
        tq.setParameter("userID", userId);

        List<SessionLeader> sess = new ArrayList<>();
        try {
            sess = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return new HashSet(sess);
    }


    @Override
    public void delete(SessionLeader entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public Set<SessionLeader> findAll() {
        List<SessionLeader> leaders = (List<SessionLeader>) getCurrentSession().createQuery("from SessionLeader ").list();
        return new HashSet(leaders);
    }
}