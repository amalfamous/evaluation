package org.example;

import org.example.classes.*;
import org.example.service.*;
import org.example.util.HibernateUtil;

import java.util.Date;

public class App {
    public static void main(String[] args) {
        CategorieService cs = new CategorieServiceImpl();
        ProduitService ps = new ProduitServiceImpl();
        CommandeService commandes = new CommandeServiceImpl();
        LigneCommandeService lcs = new LigneCommandeServiceImpl();

        // 1) Créer une catégorie
        Categorie cat = new Categorie();
        cat.setCode("CP");
        cat.setLibelle("Cartes-mères");
        cs.create(cat);

        // 2) Créer 2 produits pour la catégorie
        Produit p1 = new Produit();
        p1.setReference("ES12");
        p1.setPrix(120f);
        p1.setCategorie(cat);
        ps.create(p1);

        Produit p2 = new Produit();
        p2.setReference("ZR85");
        p2.setPrix(100f);
        p2.setCategorie(cat);
        ps.create(p2);

        System.out.println("== Produits > 100 (named query) ==");
        ps.findProduitsPrixSup(100).forEach(pr -> System.out.println(pr.getReference() + " " + pr.getPrix()));

        // 3) Créer une commande
        Commande cmd = new Commande();
        cmd.setDate(new Date());
        commandes.create(cmd); // persiste la commande (commit)

        // 4) Créer 2 lignes de commande (on persiste explicitement les lignes)
        LigneCommandeProduit l1 = new LigneCommandeProduit();
        l1.setCommande(cmd);
        l1.setProduit(p1);
        l1.setQuantite(7);
        lcs.create(l1);

        LigneCommandeProduit l2 = new LigneCommandeProduit();
        l2.setCommande(cmd);
        l2.setProduit(p2);
        l2.setQuantite(14);
        lcs.create(l2);

        // 5) Afficher produits d'une commande (via ProduitService ou utilitaire)
        System.out.println("\n== Produits dans la commande id=" + cmd.getId() + " ==");
        ps.findProductsInCommande(cmd.getId()).forEach(pr -> System.out.println(pr.getReference() + " " + pr.getPrix()));

        // 6) Clean shutdown du SessionFactory (optionnel en dev)
        HibernateUtil.getSessionFactory().close();
    }
}
