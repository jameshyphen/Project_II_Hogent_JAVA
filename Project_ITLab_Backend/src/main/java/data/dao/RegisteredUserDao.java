package data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.dao.interfaces.DaoBaseImpl;
import data.dao.interfaces.DaoInterface;
import data.dao.interfaces.ManyToManyDaoAdapter;
import domain.interfaces.ManagedClass;
import domain.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class RegisteredUserDao extends ManyToManyDaoAdapter<RegisteredUser, Integer, Integer> implements ManagedClass {
    @Override
    public void persist(RegisteredUser entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(RegisteredUser entity) {
        getCurrentSession().update(entity);
    }

    public Set<RegisteredUser> findRegisteredUsers(Integer sessionId) {
        String query = "SELECT c FROM RegisteredUser c WHERE c.session.id= :sessionID";

        TypedQuery<RegisteredUser> tq = getCurrentSession().createQuery(query, RegisteredUser.class);
        tq.setParameter("sessionID", sessionId);

        List<RegisteredUser> sess = new ArrayList<>();
        try {
            sess = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return new HashSet(sess);
    }

    public Set<RegisteredUser> findRegisteredSessions(Integer userId) {
        String query = "SELECT c FROM RegisteredUser c WHERE c.user.id= :userID";

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
    public RegisteredUser findById(Integer userId, Integer sessionId) {
        String query = "SELECT c FROM RegisteredUser c WHERE c.user.id= :userID and c.session.id= :sessionID";

        TypedQuery<RegisteredUser> tq = getCurrentSession().createQuery(query, RegisteredUser.class);
        tq.setParameter("userID", userId);
        tq.setParameter("sessionID", sessionId);

        RegisteredUser sess = null;
        try {
            sess = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return sess;
    }

    @Override
    public void delete(RegisteredUser entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public Set<RegisteredUser> findAll() {
        List<RegisteredUser> leaders = (List<RegisteredUser>) getCurrentSession().createQuery("from RegisteredUser ").list();
        return new HashSet(leaders);
    }


}