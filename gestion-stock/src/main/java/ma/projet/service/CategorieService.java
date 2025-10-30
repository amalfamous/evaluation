package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CategorieService implements IDao<Categorie> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Categorie categorie) {
        Session session = sessionFactory.getCurrentSession();
        session.save(categorie);
        return true;
    }

    @Override
    public boolean update(Categorie categorie) {
         Session session=sessionFactory.getCurrentSession();
        session.update(categorie);
        return true;
    }

    @Override
    public boolean delete(Categorie categorie) {
        sessionFactory.getCurrentSession().delete(categorie);
        return true;
    }

    @Override
    public Categorie findById(int id) {
        return sessionFactory.getCurrentSession().get(Categorie.class,id);
    }

    @Override
    public List<Categorie> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Categorie", Categorie.class).list();
    }
}

