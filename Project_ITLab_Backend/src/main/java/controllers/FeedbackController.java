package controllers;

import controllers.DTO.FeedbackDTO;
import controllers.interfaces.ChangeListenerProvider;
import data.services.FeedbackService;
import data.services.SessionService;
import domain.interfaces.ManagedClass;
import domain.models.Feedback;
import io.reactivex.Observable;

import java.util.Set;

public class FeedbackController implements ManagedClass, ChangeListenerProvider {
    private final FeedbackService serv;

    public FeedbackController(FeedbackService s) {
        serv = s;
    }

    /**
     * Persists the given Feedback if it isn't already.
     *
     * @param s Feedback to persist
     * @return True if successful, the Feedback is valid and it doesn't exist already.
     */
    public boolean createFeedback(Feedback s) {
        return serv.persist(s);
    }

    /**
     * Returns a List of all Feedbacks.
     *
     * @return List of Feedbacks.
     */
    public Set<Feedback> getFeedbacks() {
        return serv.findAll();
    }

    public Feedback getById(int id) {
        return serv.findById(id);
    }

    /**
     * Updates a Feedback to the provided feedback. Searches for a matching ID to update the persisted Feedback.
     *
     * @param f New value for the Feedback.
     * @return True if successful, the Feedback was found and it was successfully updated.
     */
    public boolean updateFeedback(FeedbackDTO f) {
        var oldFeedback = getById(f.id);
        return oldFeedback == null
                ? false
                : serv.update(copyFeedback(f, oldFeedback));
    }

    private Feedback copyFeedback(FeedbackDTO feedback, Feedback old) {
        old.setText(feedback.message);
        return old;
    }

    /**
     * Deletes the Feedback matching the provided ID.
     *
     * @param id ID of the Feedback.
     * @return True if successful, the Feedback was found and it was successfully deleted.
     */
    public boolean deleteFeedback(int id) {
        return serv.delete(id);
    }

    public Observable<Boolean> getChangeListener() {
        return this.serv.getChangeListener();
    }
}
