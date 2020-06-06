package data.services;

import data.dao.FeedbackDao;
import data.services.interfaces.CRUDService;
import domain.interfaces.ManagedClass;
import domain.models.Feedback;
import io.reactivex.Observable;

import java.util.Set;

public class FeedbackService implements CRUDService<Feedback>, ManagedClass {

    // MOCK DATA
    // private FeedbackRepository repo;
    private final FeedbackDao feedbackDao;

    public FeedbackService(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    @Override
    public boolean persist(Feedback s) {
        try {
            feedbackDao.openCurrentSessionWithTransaction();
            feedbackDao.persist(s);
            feedbackDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Feedback findById(int id) {
        feedbackDao.openCurrentSession();
        Feedback feedback = feedbackDao.findById(id);
        feedbackDao.closeCurrentSession();
        return feedback;
    }

    public Set<Feedback> findAll() {
        //return repo.getAll();
        feedbackDao.openCurrentSession();
        //unmodifiable cus currentsession doesnt have a transaction open
        Set<Feedback> feedbacks = feedbackDao.findAll();
        //Refactor for filters
        feedbackDao.closeCurrentSession();
        return feedbacks;
    }

    public boolean update(Feedback f) {
        try {
            feedbackDao.openCurrentSessionWithTransaction();
            feedbackDao.update(f);
            feedbackDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            feedbackDao.openCurrentSessionWithTransaction();
            Feedback feedback = feedbackDao.findById(id);
            feedbackDao.delete(feedback);
            feedbackDao.closeCurrentSessionWithTransaction();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Observable<Boolean> getChangeListener() {
        return this.feedbackDao.getChangeListener();
    }
}
