package org.example.service;

import org.example.classes.Produit;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.List;

public class ProduitServiceImpl implements ProduitService {

    @Override
    public boolean create(Produit p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Produit update(Produit p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(p);
            tx.commit();
            return p;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Produit produit = session.get(Produit.class, id);
            if (produit != null) {
                tx = session.beginTransaction();
                session.remove(produit);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Produit findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        }
    }

    @Override
    public List<Produit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit", Produit.class).list();
        }
    }

    @Override
    public List<Produit> findByCategorie(Integer categorieId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "SELECT p FROM Produit p WHERE p.categorie.id = :cid";
        List<Produit> res = s.createQuery(hql, Produit.class)
                .setParameter("cid", categorieId)
                .getResultList();
        s.close();
        return res;
    }
    @Override
    public List<Produit> findProductsOrderedBetweenDates(Date d1, Date d2) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "SELECT DISTINCT l.produit FROM LigneCommandeProduit l WHERE l.commande.date BETWEEN :d1 AND :d2";
        List<Produit> res = s.createQuery(hql, Produit.class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .getResultList();
        s.close();
        return res;
    }
    @Override
    public List<Produit> findProductsInCommande(Integer commandeId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "SELECT l.produit FROM LigneCommandeProduit l WHERE l.commande.id = :cid";
        List<Produit> res = s.createQuery(hql, Produit.class)
                .setParameter("cid", commandeId)
                .getResultList();
        s.close();
        return res;
    }
    @Override
    public List<Produit> findProduitsPrixSup(double prix) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Produit> res = s.createNamedQuery("Produit.findPrixSup100", Produit.class)
                .setParameter("prix", (float)prix)
                .getResultList();
        s.close();
        return res;
    }
}
