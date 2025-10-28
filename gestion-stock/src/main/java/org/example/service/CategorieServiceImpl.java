package org.example.service;

import org.example.classes.Categorie;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CategorieServiceImpl implements CategorieService {

    @Override
    public boolean create(Categorie c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(c);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Categorie c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(c);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Categorie c) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(c);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Categorie findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Categorie.class, id);
        }
    }

    @Override
    public List<Categorie> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Categorie", Categorie.class).list();
        }
    }
}

