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

public class SessionCalendarDao extends DaoBaseImpl<SessionCalendar, Integer> implements ManagedClass {
    @Override
    public void persist(SessionCalendar entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(SessionCalendar entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public SessionCalendar findById(Integer id) {
        String query = "SELECT c FROM SessionCalendar c WHERE c.id = :calendarID";

        TypedQuery<SessionCalendar> tq = getCurrentSession().createQuery(query, SessionCalendar.class);
        tq.setParameter("calendarID", id);

        SessionCalendar sess = null;
        try{
            sess = tq.getSingleResult();
        }catch (NoResultException ex){
            ex.printStackTrace();
        }
        return sess;
    }

    public void delete(SessionCalendar entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public Set<SessionCalendar> findAll() {
        List<SessionCalendar> calendars = (List<SessionCalendar>) getCurrentSession().createQuery("from SessionCalendar ").list();
        Set set = new HashSet(calendars);
        return set;
    }
}