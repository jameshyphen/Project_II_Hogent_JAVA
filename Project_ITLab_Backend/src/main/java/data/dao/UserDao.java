package data.dao;

import data.dao.interfaces.DaoBaseImpl;
import domain.interfaces.ManagedClass;
import domain.models.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDao extends DaoBaseImpl<User, Integer> implements ManagedClass {

    @Override
    public void persist(User entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(User entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public User findById(Integer id) {
        String query = "SELECT c FROM User c WHERE c.id = :userID";

        TypedQuery<User> tq = getCurrentSession().createQuery(query, User.class);
        tq.setParameter("userID", id);

        User user = null;
        try {
            user = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public User findByUsername(String username) {
        String query = "SELECT c FROM User c WHERE c.username = :userUsername";

        TypedQuery<User> tq = getCurrentSession().createQuery(query, User.class);
        tq.setParameter("userUsername", username);

        User user = null;
        try {
            user = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public void delete(User entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<User> findAll() {
        List<User> users = (List<User>) getCurrentSession().createQuery("from User order by FirstName").list();
        Set set = new HashSet(users);
        return set;
    }
}
