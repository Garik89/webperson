package ru.taipov.webperson.projectboot;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonSession {

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private static final Logger logger = LoggerFactory.getLogger(PersonSession.class);

    public SessionFactory sessionFactory;
    public Session session;

    public PersonSession(Class clas) {

        try {

            Configuration configuration = new Configuration().addAnnotatedClass(clas);

            this.sessionFactory = configuration.buildSessionFactory();

            this.session = sessionFactory.getCurrentSession();
        }

        catch (HibernateException e)
        {
            logger.error("Ошибка получения сессии");
            System.out.println("Ошибка при получении сессии");
        }

    }

}
