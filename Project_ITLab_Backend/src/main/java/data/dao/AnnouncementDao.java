package data.dao;

import data.dao.interfaces.DaoBaseImpl;
import domain.interfaces.ManagedClass;
import domain.models.Announcement;
import domain.models.Feedback;
import domain.models.RegisteredUser;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnouncementDao extends DaoBaseImpl<Announcement, Integer> implements ManagedClass {
    @Override
    public void persist(Announcement entity)  {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Announcement entity)  {
        getCurrentSession().update(entity);
    }

    @Override
    public Announcement findById(Integer announcementId) {
        String query = "SELECT c FROM Announcement c WHERE c.id = :announcementID";

        TypedQuery<Announcement> tq = getCurrentSession().createQuery(query, Announcement.class);
        tq.setParameter("announcementID", announcementId);

        Announcement announcement = null;
        try {
            announcement = tq.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return announcement;
    }

    @Override
    public void delete(Announcement entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public Set<Announcement> findAll() {
        List<Announcement> announcements = (List<Announcement>) getCurrentSession().createQuery("from Announcement ").list();
        return new HashSet(announcements);
    }

    public Set<Announcement> findByUserId(Integer userId){
        String query = "SELECT c FROM Announcement c WHERE c.author.id= :userID";

        TypedQuery<Announcement> tq = getCurrentSession().createQuery(query, Announcement.class);
        tq.setParameter("userID", userId);

        List<Announcement> announcements = new ArrayList<>();
        try {
            announcements = tq.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return new HashSet(announcements);
    }
}
