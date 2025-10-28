package org.example.service;


import org.example.classes.Tache;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class TacheServiceImpl implements TacheService {

    @Override
    public boolean create(Tache t) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.save(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Tache t) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.update(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Tache t) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.delete(t);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Tache findById(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Tache.class, id);
        }
    }

    @Override
    public List<Tache> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Tache", Tache.class).list();
        }
    }

    @Override
    public List<Tache> findTachesPrixSup(double prix) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createNamedQuery("Tache.findPrixSup", Tache.class)
                    .setParameter("prix", prix)
                    .list();
        }
    }

    @Override
    public List<Tache> findTachesRealiseesEntre(Date d1, Date d2) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "select et.tache from EmployeTache et " +
                                    "where et.dateFinReelle between :d1 and :d2",
                            Tache.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        }
    }
}

