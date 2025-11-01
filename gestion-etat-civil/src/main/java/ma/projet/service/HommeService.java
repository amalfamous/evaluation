package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HommeService implements IDao<Homme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Homme o) {
        Session session=sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean delete(Homme o) {
        Session session=sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public boolean update(Homme o) {
        Session session=sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public Homme findById(int id) {
        return sessionFactory.getCurrentSession().get(Homme.class, id);
    }

    @Override
    public List<Homme> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Homme").list();
    }
    public List<Femme> findePousesHomme(int idHomme, Date date1, Date date2){
        return sessionFactory.getCurrentSession().createQuery("select distinct m.femme from Mariage m where m.homme.id = :idHomme and m.dateDebut BETWEEN :date1 and :date2", Femme.class).setParameter("idHomme", idHomme).setParameter("date1", date1).setParameter("date2", date2).list();
    }
}
