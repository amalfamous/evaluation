package ma.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HibernateUtil.class);

        // récupérer beans
        CategorieService categorieService = ctx.getBean(CategorieService.class);
        ProduitService produitService = ctx.getBean(ProduitService.class);
        CommandeService commandeService = ctx.getBean(CommandeService.class);
        LigneCommandeService ligneCommandeService = ctx.getBean(LigneCommandeService.class);

        PlatformTransactionManager txMgr = ctx.getBean(PlatformTransactionManager.class);
        TransactionTemplate tx = new TransactionTemplate(txMgr);

        // exécuter toutes les opérations (persist + affichage) dans une transaction
        tx.execute(status -> {
            try {
                // 1) créer la catégorie
                Categorie cat = new Categorie();
                cat.setCode("ELEC");
                cat.setLibelle("Électronique");
                // CategorieService.create() n'était pas annoté @Transactional dans ta version,
                // mais nous sommes déjà dans une transaction via TransactionTemplate,
                // donc l'appel ci-dessous persistera correctement.
                categorieService.create(cat);

                // 2) créer produits
                Produit p1 = new Produit();
                p1.setReference("ES12");
                p1.setPrix(120f);
                p1.setCategorie(cat);
                produitService.create(p1);

                Produit p2 = new Produit();
                p2.setReference("ZR85");
                p2.setPrix(100f);
                p2.setCategorie(cat);
                produitService.create(p2);

                Produit p3 = new Produit();
                p3.setReference("EE85");
                p3.setPrix(200f);
                p3.setCategorie(cat);
                produitService.create(p3);

                // 3) créer commande avec date précise (14 mars 2013)
                SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
                Date dateCommande = parser.parse("14/03/2013");

                Commande cmd = new Commande();
                cmd.setDate(dateCommande);
                commandeService.create(cmd);

                // 4) créer lignes et persister
                LigneCommandeProduit l1 = new LigneCommandeProduit();
                l1.setCommande(cmd);
                l1.setProduit(p1);
                l1.setQuantite(7);
                ligneCommandeService.create(l1);

                LigneCommandeProduit l2 = new LigneCommandeProduit();
                l2.setCommande(cmd);
                l2.setProduit(p2);
                l2.setQuantite(14);
                ligneCommandeService.create(l2);

                LigneCommandeProduit l3 = new LigneCommandeProduit();
                l3.setCommande(cmd);
                l3.setProduit(p3);
                l3.setQuantite(5);
                ligneCommandeService.create(l3);

                // --- affichage formaté DANS LA MEME TRANSACTION (pour éviter LazyInitializationException) ---
                SimpleDateFormat outFmt = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
                System.out.println();
                System.out.println("Commande : " + cmd.getId() + "    Date : " + outFmt.format(cmd.getDate()));
                System.out.println("Liste des produits :");
                System.out.printf("%-12s %-8s %s%n", "Référence", "Prix", "Quantité");

                // lire les lignes de commande pour cette commande et afficher (on utilise findAll() et on filtre)
                for (LigneCommandeProduit ligne : ligneCommandeService.findAll()) {
                    if (ligne.getCommande() != null && ligne.getCommande().getId() != null
                            && ligne.getCommande().getId().equals(cmd.getId())) {

                        String ref = ligne.getProduit() != null ? ligne.getProduit().getReference() : "N/A";
                        String prix = ligne.getProduit() != null ? String.format("%.0f DH", ligne.getProduit().getPrix()) : "N/A";
                        int qte = ligne.getQuantite();

                        System.out.printf("%-12s %-8s %d%n", ref, prix, qte);
                    }
                }
            } catch (Exception e) {
                // rollback will be triggered by throwing a RuntimeException
                throw new RuntimeException("Erreur lors de la création/affichage", e);
            }
            return null;
        });

        ctx.close();
    }
}
