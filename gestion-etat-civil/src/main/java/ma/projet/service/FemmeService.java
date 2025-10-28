package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class FemmeService implements IDao<Femme> {

    @Override
    public boolean create(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(Femme o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Femme findById(int id) {
        Session session = null;
        Femme femme = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            femme = session.get(Femme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return femme;
    }

    @Override
    public List<Femme> findAll() {
        Session session = null;
        List<Femme> femmes = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.createQuery("FROM Femme", Femme.class);
            femmes = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return femmes;
    }

    // Trouver la femme la plus âgée
    public Femme findOldestFemme() {
        List<Femme> femmes = findAll();
        if (femmes == null || femmes.isEmpty()) {
            return null;
        }
        return femmes.stream()
                .min(Comparator.comparing(Femme::getDateNaissance))
                .orElse(null);
    }

    // Nombre d'enfants d'une femme entre deux dates
    public long countChildrenBetweenDates(Femme femme, LocalDate d1, LocalDate d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String sql = "SELECT COALESCE(SUM(nbr_enfant), 0) FROM mariage " +
                    "WHERE femme_id = ?1 AND date_debut BETWEEN ?2 AND ?3";
            Query<?> query = session.createNativeQuery(sql);
            query.setParameter(1, femme.getId());
            query.setParameter(2, d1);
            query.setParameter(3, d2);

            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            session.close();
        }
    }


    // Femmes mariées au moins 2 fois (requête nommée)
    public List<Femme> findFemmesMarriedAtLeastTwice() {
        Session session = null;
        List<Femme> femmes = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Femme> query = session.createNamedQuery("Femme.marriedAtLeastTwice", Femme.class);
            femmes = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return femmes;
    }
}