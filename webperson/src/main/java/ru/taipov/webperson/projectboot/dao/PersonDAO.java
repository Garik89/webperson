package ru.taipov.webperson.projectboot.dao;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.taipov.webperson.projectboot.PersonSession;
import ru.taipov.webperson.projectboot.model.Person;

import java.util.Date;
import java.util.List;


public class PersonDAO {


    private static final Logger logger = LoggerFactory.getLogger(PersonDAO.class);
    private Session session;
    private PersonSession personSession;
    private Configuration configuration;
    SessionFactory sessionFactory;

    public PersonDAO()
    {
        this.configuration = new Configuration().addAnnotatedClass(Person.class);
        this.sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.getCurrentSession();
        personSession = new PersonSession(Person.class);

    }




    //Метод добавления человека в базу данных
    public void create(Person person)
    {

        try {
            session.beginTransaction();

            session.persist(person);

            session.getTransaction().commit();

        }

        catch (HibernateException e) {
            System.out.println();
            System.out.println();
            logger.error("Ошибка введения данных");
            System.out.println("Введено недопустимое имя или email значение");
            System.out.println();
            System.out.println();
        }

        finally{
        personSession.getSessionFactory().close();
          }

    }

    //Перегрузка метода считывания одного человека из базы
    public Person read(String id) {


        Person person;

        try {
            personSession.getSession().beginTransaction();

            person = personSession.getSession().find(Person.class, Integer.parseInt(id));

            personSession.getSession().getTransaction().commit();

        }
        finally{
                personSession.getSessionFactory().close();
            }

        return person;
    }

    //Перегрузка метода считывания всех людей из базы
    public List<Person> readAll(String query) {


        List<Person> persons;

        try {
            personSession.getSession().beginTransaction();

         //   persons = personSession.getSession().createQuery(query, Person.class).getResultList();
            persons = personSession.getSession().createQuery(query, Person.class).getResultList();

            personSession.getSession().getTransaction().commit();

        }
        finally{
            personSession.getSessionFactory().close();
        }

        return persons;
    }

    //Метод обновления человека в базе
    public  void update(String id, String name, String age, String email)
    {

        Person person;

        try {
            personSession.getSession().beginTransaction();
            person = personSession.getSession().find(Person.class, Integer.parseInt(id));

            person.setName(name);
            person.setAge(Integer.parseInt(age));
            person.setEmail(email);
            person.setCreated_at(new Date());

            personSession.getSession().getTransaction().commit();

        }
        catch (HibernateException e) {
            logger.error("Ошибка введения данных");
            System.out.println("Введено недопустимое имя или email значение");
        }

        finally{
            personSession.getSessionFactory().close();
        }

        }


    //Метод удаления человека из базы
    public  void delete(Person person)
    {


        try {
            session.beginTransaction();
            session.remove(person);
            session.getTransaction().commit();


        }

        catch (HibernateException e) {
            System.out.println("Ошибка при работе с базой данных");
        }

        finally {
            personSession.getSessionFactory().close();
        }

    }

    }


