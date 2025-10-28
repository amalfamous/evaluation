package org.example.service;


import org.example.classes.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeServiceImpl implements EmployeService {

    @Override
    public boolean create(Employe e) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.save(e);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Employe e) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.update(e);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Employe e) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.delete(e);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Employe findById(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Employe.class, id);
        }
    }

    @Override
    public List<Employe> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Employe", Employe.class).list();
        }
    }

    @Override
    public List<Tache> findTachesRealiseesParEmploye(int employeId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "select et.tache from EmployeTache et " +
                                    "where et.employe.id = :id and et.dateFinReelle is not null",
                            Tache.class)
                    .setParameter("id", employeId)
                    .list();
        }
    }

    @Override
    public List<Projet> findProjetsGeresParEmploye(int employeId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "from Projet where chef.id = :id",
                            Projet.class)
                    .setParameter("id", employeId)
                    .list();
        }
    }
}
