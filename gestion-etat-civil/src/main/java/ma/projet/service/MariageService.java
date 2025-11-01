package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MariageService implements IDao<Mariage> {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public boolean create(Mariage o) {
        Session session=sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean delete(Mariage o) {
        Session session=sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public boolean update(Mariage o) {
        Session session=sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public Mariage findById(int id) {
        return sessionFactory.getCurrentSession().get(Mariage.class,id);
    }

    @Override
    public List<Mariage> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Mariage ", Mariage.class).list();
    }
}
