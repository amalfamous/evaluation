package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CommandeService implements IDao<Commande> {
    @Autowired private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Commande o) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(o);
        return true;
    }

    @Override
    public boolean update(Commande o) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(o);
        return true;
    }

    @Override
    public boolean delete(Commande o) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(o);
        return true;
    }

    @Override
    public Commande findById(int id) {
        return sessionFactory.getCurrentSession().get(Commande.class, id);
    }


    @Override
    public List<Commande> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Commande", Commande.class).list();
    }
}
