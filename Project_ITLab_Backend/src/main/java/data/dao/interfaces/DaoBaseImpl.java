package data.dao.interfaces;

import domain.models.*;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class DaoBaseImpl<T, Id extends Serializable> implements DaoInterface<T, Id> {

    private Session currentHibernateSession;
    private Transaction currentHibernateTransaction;

    private final Subject<Boolean> changeListener = PublishSubject.create();


    public Session getCurrentSession() {
        return currentHibernateSession;
    }

    public void setCurrentSession(Session currentHibernateSession) {
        this.currentHibernateSession = currentHibernateSession;
    }

    public Transaction getCurrentTransaction() {
        return currentHibernateTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentHibernateTransaction = currentTransaction;
    }

    public Session openCurrentSession() {
        currentHibernateSession = getSessionFactory().openSession();
        return currentHibernateSession;
    }

    public void closeCurrentSession() {
        currentHibernateSession.close();
    }

    public Session openCurrentSessionWithTransaction() {
        currentHibernateSession = getSessionFactory().openSession();
        currentHibernateTransaction = currentHibernateSession.beginTransaction();
        return currentHibernateSession;
    }

    public void closeCurrentSessionWithTransaction() {
        currentHibernateTransaction.commit();
        changeListener.onNext(true);
        currentHibernateSession.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(domain.models.Session.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Announcement.class);
        configuration.addAnnotatedClass(Feedback.class);
        configuration.addAnnotatedClass(RegisteredUser.class);
        configuration.addAnnotatedClass(SessionCalendar.class);
        configuration.addAnnotatedClass(SessionLeader.class);
        configuration.addAnnotatedClass(AuthUser.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        return sessionFactory;
    }

    /**
     * Allows access to the changeListener.
     *
     * @return Observable just used to signal a change. No actual data will be pushed.
     */
    public final Observable<Boolean> getChangeListener() {
        return this.changeListener;
    }

    @Override
    public abstract void persist(T entity);

    @Override
    public abstract void update(T entity);

    @Override
    public abstract T findById(Id id);

    @Override
    public abstract void delete(T entity);

    @Override
    public abstract Set<T> findAll();

}
