package data.dao;

import data.dao.interfaces.DaoBaseImpl;
import data.dao.interfaces.DaoInterface;
import domain.interfaces.ManagedClass;
import domain.models.AuthUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthDao extends DaoBaseImpl<AuthUser, Integer> implements ManagedClass {


    @Override
    public void persist(AuthUser entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(AuthUser entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public AuthUser findById(Integer id) {
        String query = "SELECT c FROM AuthUser c WHERE c.id = :adminID";

        TypedQuery<AuthUser> tq = getCurrentSession().createQuery(query, AuthUser.class);
        tq.setParameter("adminID", id);

        AuthUser user = null;
        try {
            user = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public void delete(AuthUser entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public Set<AuthUser> findAll() {
        List<AuthUser> authusers = (List<AuthUser>) getCurrentSession()
                .createQuery("from AuthUser ")
                .list();
        return new HashSet(authusers);
    }

    public AuthUser findByUsername(String username) {
        String query = "SELECT c FROM AuthUser c WHERE c.username = :adminUsername";

        TypedQuery<AuthUser> tq = getCurrentSession().createQuery(query, AuthUser.class);
        tq.setParameter("adminUsername", username);

        AuthUser user = null;
        try {
            user = tq.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Username \'" + username + "\' not found");
        }
        return user;
    }
}
