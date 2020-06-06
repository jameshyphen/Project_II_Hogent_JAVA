package data.dao;

import data.dao.interfaces.DaoBaseImpl;
import domain.interfaces.ManagedClass;
import domain.models.Feedback;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedbackDao extends DaoBaseImpl<Feedback, Integer> implements ManagedClass {

    @Override
    public void persist(Feedback entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Feedback entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public Feedback findById(Integer feedbackId) {
        String query = "SELECT c FROM Feedback c WHERE c.id = :feedbackID";

        TypedQuery<Feedback> tq = getCurrentSession().createQuery(query, Feedback.class);
        tq.setParameter("feedbackID", feedbackId);

        Feedback feedback = null;
        try {
            feedback = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return feedback;
    }

    @Override
    public void delete(Feedback entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public Set<Feedback> findAll() {
        List<Feedback> feedbacks = (List<Feedback>) getCurrentSession().createQuery("from Feedback ").list();
        return new HashSet(feedbacks);
    }

    public Set<Feedback> findByUserId(Integer userId){
        String query = "SELECT c FROM Feedback c WHERE c.author.id= :userID";

        TypedQuery<Feedback> tq = getCurrentSession().createQuery(query, Feedback.class);
        tq.setParameter("userID", userId);

        List<Feedback> feedbacks = new ArrayList<>();
        try {
            feedbacks = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return new HashSet(feedbacks);
    }
}
