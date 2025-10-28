package org.example.service;


import org.example.classes.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProjetServiceImpl implements ProjetService {

    @Override
    public boolean create(Projet p) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.save(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Projet p) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.update(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Projet p) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.delete(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Projet findById(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.get(Projet.class, id);
        }
    }

    @Override
    public List<Projet> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Projet", Projet.class).list();
        }
    }

    @Override
    public List<Tache> findTachesPlanifiees(int projetId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "from Tache where projet.id = :id",
                            Tache.class)
                    .setParameter("id", projetId)
                    .list();
        }
    }

    @Override
    public List<Object[]> findTachesRealiseesAvecDates(int projetId) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "select t.id, t.nom, et.dateDebutReelle, et.dateFinReelle " +
                                    "from EmployeTache et join et.tache t " +
                                    "where t.projet.id = :id and et.dateFinReelle is not null",
                            Object[].class)
                    .setParameter("id", projetId)
                    .list();
        }
    }
}
