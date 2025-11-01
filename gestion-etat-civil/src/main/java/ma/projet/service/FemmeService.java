package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FemmeService implements IDao<Femme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Femme o) {
        Session session=sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean delete(Femme o) {
        Session session=sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public boolean update(Femme o) {
        Session session=sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public Femme findById(int id) {
        return sessionFactory.getCurrentSession().get(Femme.class, id);
    }

    @Override
    public List<Femme> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Femme").list();
    }

    public int getNombreEnfantsEntreDatesNative(int idFemme, LocalDate date1, LocalDate date2) {
        Session session = sessionFactory.getCurrentSession();
        Object result = session.createNativeQuery(
                        "SELECT COALESCE(SUM(nbr_enfant), 0) FROM mariage WHERE femme_id = ?1 AND date_debut BETWEEN ?2 AND ?3")
                .setParameter(1, idFemme)
                .setParameter(2, java.sql.Date.valueOf(date1))
                .setParameter(3, java.sql.Date.valueOf(date2))
                .getSingleResult();

        if (result instanceof java.math.BigInteger) return ((java.math.BigInteger) result).intValue();
        if (result instanceof java.math.BigDecimal) return ((java.math.BigDecimal) result).intValue();
        if (result instanceof Number) return ((Number) result).intValue();
        return 0;
    }

    public List<Femme> findFemmesMarriedAtLeastTwice() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT f FROM Femme f WHERE SIZE(f.mariages) >= 2", Femme.class)
                .getResultList();
    }

    public int countHommesMarriedToFourWomenBetweenDates(LocalDate date1, LocalDate date2) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Mariage> root = cq.from(Mariage.class);

        Path<Integer> hommeId = root.get("homme").get("id");
        cq.select(hommeId);

        Predicate betweenDates = cb.between(root.get("dateDebut"), date1, date2);
        cq.where(betweenDates);

        cq.groupBy(hommeId);
        cq.having(cb.equal(cb.countDistinct(root.get("femme")), 4L));

        List<Integer> hommes = session.createQuery(cq).getResultList();
        return hommes.size();
    }

    public List<Mariage> getMariagesOfHomme(int idHomme) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT m FROM Mariage m WHERE m.homme.id = :id ORDER BY m.dateDebut", Mariage.class)
                .setParameter("id", idHomme)
                .getResultList();
    }

    /**
     *  Formate pour affichage console les mariages d'un homme
     */
    public String formatMariagesOfHommeForPrint(int idHomme) {
        Session session = sessionFactory.getCurrentSession();
        Homme h = session.get(Homme.class, idHomme);
        if (h == null) return "Homme introuvable (id=" + idHomme + ")";

        List<Mariage> mariages = getMariagesOfHomme(idHomme);
        java.time.format.DateTimeFormatter DTF = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Nom : ").append(h.getNom()).append(" ").append(h.getPrenom()).append("\n\n");

        sb.append("Mariages En Cours :\n");
        int idxEnCours = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() == null) {
                sb.append(idxEnCours).append(". Femme : ")
                        .append(m.getFemme() != null ? (m.getFemme().getNom() + " " + m.getFemme().getPrenom()) : "N/A")
                        .append("   Date Début : ")
                        .append(m.getDateDebut() != null ? m.getDateDebut().format(DTF) : "")
                        .append("    Nbr Enfants : ").append(m.getNbrEnfant() == null ? 0 : m.getNbrEnfant())
                        .append("\n");
                idxEnCours++;
            }
        }
        if (idxEnCours == 1) sb.append("Aucun\n");

        sb.append("\nMariages échoués :\n");
        int idxEchoues = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() != null) {
                sb.append(idxEchoues).append(". Femme : ")
                        .append(m.getFemme() != null ? (m.getFemme().getNom() + " " + m.getFemme().getPrenom()) : "N/A")
                        .append("   Date Début : ")
                        .append(m.getDateDebut() != null ? m.getDateDebut().format(DTF) : "")
                        .append("\nDate Fin : ")
                        .append(m.getDateFin() != null ? m.getDateFin().format(DTF) : "")
                        .append("    Nbr Enfants : ").append(m.getNbrEnfant() == null ? 0 : m.getNbrEnfant())
                        .append("\n");
                idxEchoues++;
            }
        }
        if (idxEchoues == 1) sb.append("Aucun\n");

        return sb.toString();
    }

}
