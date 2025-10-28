package org.example.service;

import org.example.classes.LigneCommandeProduit;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LigneCommandeServiceImpl implements LigneCommandeService {

    @Override
    public boolean create(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(l);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(l);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(l);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LigneCommandeProduit findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(LigneCommandeProduit.class, id);
        }
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM LigneCommandeProduit", LigneCommandeProduit.class).list();
        }
    }
}

