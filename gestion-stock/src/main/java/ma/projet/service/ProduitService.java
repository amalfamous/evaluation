package ma.projet.service;

import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class ProduitService implements IDao<Produit> {
    @Autowired private SessionFactory sessionFactory;
    @Override
    @Transactional
    public boolean create(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean update(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public boolean delete(Produit o) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public Produit findById(int id) {
        return sessionFactory.getCurrentSession().get(Produit.class,id);
    }

    @Override
    public List<Produit> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Produit", Produit.class).list();
    }
    public List<Produit> findByCategorie(int categorieId) {
        Session session= sessionFactory.getCurrentSession();
        return session.createQuery(
                "from Produit p where p.categorie.id = :categorieId", Produit.class).setParameter("categorieId", categorieId).list();
    }
    public List<Produit> findByProduitsBetweenTwoDates(Date d1, Date d2) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct l.produit " + "from LigneCommandeProduit l" + " where l.commande.date between :d1 and :d2", Produit.class).setParameter("d1", d1).setParameter("d2",d2).list();
    }
    public List<Produit> findByProduitsCommande(int commandeId){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct l.produit from LigneCommandeProduit l where l.commande.id = :commandeId", Produit.class).setParameter("commandeId",commandeId).list();
    }
    public List<Produit> findByPriceGreaterThan100(){
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createNamedQuery("Produit.findByPriceGreaterThan100", Produit.class).setParameter("prix", 100f).list();
    }
}
