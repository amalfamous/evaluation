package org.example;

import org.example.classes.Commande;
import org.example.classes.LigneCommandeProduit;
import org.example.classes.Produit;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestInsertCommande {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Récupère un produit existant (ou en créer un si besoin)
            Produit produit = session.createQuery("FROM Produit", Produit.class)
                    .setMaxResults(1)
                    .uniqueResult();
            if (produit == null) {
                produit = new Produit();
                produit.setReference("TST01");
                produit.setPrix(50f);
                session.save(produit);
                // flush/refresh non nécessaire ici, id sera assigné après commit si IDENTITY
            }

            // Créer une commande
            Commande cmd = new Commande();
            cmd.setDate(new Date());

            // Créer une ligne et lier produit + commande
            LigneCommandeProduit ligne = new LigneCommandeProduit();
            ligne.setProduit(produit);
            ligne.setQuantite(3);
            ligne.setCommande(cmd); // IMPORTANT

            // Lier la ligne à la commande (bidirectionnel)
            Set<LigneCommandeProduit> lignes = new HashSet<>();
            lignes.add(ligne);
            cmd.setLignes(lignes);

            // Persister la commande (si cascade = ALL sur Commande.lignes, la ligne sera enregistrée)
            session.save(cmd);

            // Si tu n'as PAS cascade sur Commande.lignes, sauvegarde explicitement la ligne :
            // session.save(ligne);

            tx.commit();
            System.out.println("Commande et ligne insérées avec succès. Commande id = " + cmd.getId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.getSessionFactory().close();
        }
    }
}
