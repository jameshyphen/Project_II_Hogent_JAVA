package run;

import domain.models.*;
import domain.models.RegisteredUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("Project_ITLab");
    //TODO Refactor for use with a mapper and Observables

    public static void main(String[] args) throws Exception{
        String test = "2";

//
//        ser.logout();
//        System.out.println(ser.getCurrentUser());

        User user = new User("Testing", "Pushing", "dh@aa.c", "24123512123");
        user.setEmail(test+"@gmail.com");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime dateTime1 = LocalDateTime.parse("07-08-2020 20:00", formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse("08-08-2020 15:00", formatter);
        LocalDateTime dateTime3 = LocalDateTime.parse("08-08-2020 16:00", formatter);

        Session session = new Session(user, "Session title 2", "Description", "Speakername", "B2009",
                dateTime2, dateTime3, 70,
                true, dateTime1);

        SessionLeader leader = new SessionLeader();
        leader.setLeader(user);
        leader.setSession(session);


        //public Feedback(User author, Session session, String message, LocalDateTime date) {

        User user2 = new User("Testing2213", "Pushin312312g","dh@aa.c", "24123512123123");

        user2.setEmail("Test23a@gmail.com");





        Feedback feedback = new Feedback(user2, session, "This is a test feedback", LocalDateTime.now());


        Announcement announcement = new Announcement(user2, session, "Testing an announcement to db");


        session.addRegisteredUser(user2);
        session.addFeedback(feedback);
        session.addAnnouncement(announcement);
        session.addSessionLeader(user2);



//        session.addRegisteredUser(regUser);
//        session.addSessionLeader(leader);
//        session.addFeedback(feedback);
//        session.addAnnouncement(announcement);

        addSession(session);

//        AuthUser admin = new AuthUser("aptulaxdd", "password");

//        addAdmin(admin);

//        SessionCalendar calendar = new SessionCalendar(dateTime1, dateTime3);
//
//        addCalendar(calendar);

        ENTITY_MANAGER_FACTORY.close();


    }

    public static void getSession(int id){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT c FROM Session c WHERE c.id = :sessionID";

        TypedQuery<Session> tq = em.createQuery(query, Session.class);
        tq.setParameter("sessionID", id);
        Session sess = null;
        try{
            sess = tq.getSingleResult();
            System.out.println(sess.getTitle());
        }catch (NoResultException ex){
            ex.printStackTrace();
        }finally
        {
            em.close();
        }


    }
    public static void addSession(Session session){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(session);
            et.commit();
        }catch (Exception ex){
            if(et !=null){
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally{
            em.close();
        }
    }

    public static void addAdmin(AuthUser admin){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(admin);
            et.commit();
        }catch (Exception ex){
            if(et !=null){
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally{
            em.close();
        }
    }

    public static void addCalendar(SessionCalendar calendar){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try{
            et = em.getTransaction();
            et.begin();
            em.persist(calendar);
            et.commit();
        }catch (Exception ex){
            if(et !=null){
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally{
            em.close();
        }
    }

}

