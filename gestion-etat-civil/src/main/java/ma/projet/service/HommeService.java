package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Homme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Homme o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Homme findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Homme.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Homme> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Homme> query = session.createQuery("FROM Homme", Homme.class);
            return query.list();
        }
    }

    // Afficher les épouses d'un homme entre deux dates
    public List<Femme> getEpousesBetweenDates(Homme homme, LocalDate d1, LocalDate d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT m.femme FROM Mariage m WHERE m.homme.id = :hommeId " +
                    "AND m.dateDebut BETWEEN :d1 AND :d2";
            Query<Femme> query = session.createQuery(hql, Femme.class);
            query.setParameter("hommeId", homme.getId());
            query.setParameter("d1", d1);
            query.setParameter("d2", d2);
            return query.list();
        }
    }

    // Compter les hommes mariés à 4 femmes entre deux dates
    public long countHommesMarriedTo4FemmesBetweenDates(LocalDate d1, LocalDate d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Mariage> root = cq.from(Mariage.class);

            cq.multiselect(root.get("homme").get("id"), cb.count(root))
                    .where(cb.between(root.get("dateDebut"), d1, d2))
                    .groupBy(root.get("homme").get("id"))
                    .having(cb.equal(cb.count(root), 4));

            return session.createQuery(cq).getResultList().size();
        }
    }

    // Afficher les mariages d'un homme avec détails
    public void displayMariagesOfHomme(Homme homme) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme h = session.get(Homme.class, homme.getId());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.println("Nom : " + h.getNom().toUpperCase() + " " + h.getPrenom().toUpperCase());

            List<Mariage> mariagesEnCours = h.getMariages().stream()
                    .filter(m -> m.getDateFin() == null)
                    .collect(Collectors.toList());

            List<Mariage> mariagesEchoues = h.getMariages().stream()
                    .filter(m -> m.getDateFin() != null)
                    .collect(Collectors.toList());

            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : mariagesEnCours) {
                System.out.printf("%d. Femme : %-15s Date Début : %s    Nbr Enfants : %d%n",
                        i++,
                        m.getFemme().getPrenom().toUpperCase() + " " + m.getFemme().getNom().toUpperCase(),
                        m.getDateDebut().format(fmt),
                        m.getNbrEnfant());
            }

            System.out.println("\nMariages échoués :");
            i = 1;
            for (Mariage m : mariagesEchoues) {
                System.out.printf("%d. Femme : %-15s Date Début : %s%n",
                        i++,
                        m.getFemme().getPrenom().toUpperCase() + " " + m.getFemme().getNom().toUpperCase(),
                        m.getDateDebut().format(fmt));
                System.out.printf("   Date Fin : %s    Nbr Enfants : %d%n",
                        m.getDateFin().format(fmt),
                        m.getNbrEnfant());
            }
        }
    }
}
