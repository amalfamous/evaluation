package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;

/**
 * Main corrigé : utilise TransactionTemplate pour exécuter la logique
 * à l'intérieur d'une transaction Spring (nécessaire pour getCurrentSession()).
 */
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("ma.projet");
        try {
            ctx.refresh();

            FemmeService femmeService = ctx.getBean(FemmeService.class);
            HommeService hommeService = ctx.getBean(HommeService.class);
            MariageService mariageService = ctx.getBean(MariageService.class);

            // Récupérer le transaction manager et créer un TransactionTemplate
            PlatformTransactionManager txMgr = ctx.getBean(PlatformTransactionManager.class);
            TransactionTemplate txTemplate = new TransactionTemplate(txMgr);

            // Exécuter toute la création/affichage dans une transaction
            txTemplate.execute(status -> {
                // ---------- 1) Création des 10 femmes ----------
                Femme f1 = new Femme("RAMI", "SALIMA", "060000001", "Adresse 1", LocalDate.of(1972, 3, 10));
                Femme f2 = new Femme("ALI", "AMAL", "060000002", "Adresse 2", LocalDate.of(1975, 5, 20));
                Femme f3 = new Femme("ALAOUI", "WAFA", "060000003", "Adresse 3", LocalDate.of(1980, 11, 2));
                Femme f4 = new Femme("ALAMI", "KARIMA", "060000004", "Adresse 4", LocalDate.of(1970, 9, 1));
                Femme f5 = new Femme("BENJELLOUN", "NORA", "060000005", "Adresse 5", LocalDate.of(1985, 1, 15));
                Femme f6 = new Femme("ERRAHMANI", "LAILA", "060000006", "Adresse 6", LocalDate.of(1978, 6, 7));
                Femme f7 = new Femme("ZAHRAOUI", "FATIMA", "060000007", "Adresse 7", LocalDate.of(1968, 12, 22));
                Femme f8 = new Femme("MANSOUR", "SARA", "060000008", "Adresse 8", LocalDate.of(1990, 8, 9));
                Femme f9 = new Femme("OUFIR", "HIND", "060000009", "Adresse 9", LocalDate.of(1976, 4, 30));
                Femme f10 = new Femme("KHALID", "IMANE", "060000010", "Adresse 10", LocalDate.of(1982, 2, 18));

                femmeService.create(f1);
                femmeService.create(f2);
                femmeService.create(f3);
                femmeService.create(f4);
                femmeService.create(f5);
                femmeService.create(f6);
                femmeService.create(f7);
                femmeService.create(f8);
                femmeService.create(f9);
                femmeService.create(f10);

                // ---------- 2) Création des 5 hommes ----------
                Homme h1 = new Homme("SAID", "SAFI", "070000001", "AddrH1", LocalDate.of(1960, 2, 2)); // exemple
                Homme h2 = new Homme("HADDAD", "MOHAMED", "070000002", "AddrH2", LocalDate.of(1965, 3, 3));
                Homme h3 = new Homme("TOUIL", "YASSINE", "070000003", "AddrH3", LocalDate.of(1970, 4, 4));
                Homme h4 = new Homme("RAISS", "AHMED", "070000004", LocalDate.of(1972, 5, 5));
                Homme h5 = new Homme("BEN", "KARIM", "070000005", LocalDate.of(1976, 6, 6));

                hommeService.create(h1);
                hommeService.create(h2);
                hommeService.create(h3);
                hommeService.create(h4);
                hommeService.create(h5);

                // ---------- 3) Mariages pour SAFI SAID (h1) ----------
                mariageService.create(new Mariage(h1, f1, LocalDate.of(1990, 9, 3), null, 4));
                mariageService.create(new Mariage(h1, f2, LocalDate.of(1995, 9, 3), null, 2));
                mariageService.create(new Mariage(h1, f3, LocalDate.of(2000, 11, 4), null, 3));
                mariageService.create(new Mariage(h1, f4, LocalDate.of(1989, 9, 3), LocalDate.of(1990, 9, 3), 0));

                // ---------- 4) Homme marié à 4 femmes (h2) ----------
                mariageService.create(new Mariage(h2, f5, LocalDate.of(2001, 1, 1), null, 1));
                mariageService.create(new Mariage(h2, f6, LocalDate.of(2002, 2, 2), null, 0));
                mariageService.create(new Mariage(h2, f7, LocalDate.of(2003, 3, 3), null, 2));
                mariageService.create(new Mariage(h2, f8, LocalDate.of(2004, 4, 4), null, 0));

                // ---------- 5) Quelques autres mariages ----------
                mariageService.create(new Mariage(h3, f9, LocalDate.of(1999, 5, 5), LocalDate.of(2000, 5, 5), 1));
                mariageService.create(new Mariage(h3, f10, LocalDate.of(2005, 6, 6), null, 1));

                // -------------------------
                // AFFICHAGE DES INFOS DEMANDÉES
                // -------------------------
                System.out.println("=== Liste des femmes ===");
                List<Femme> allFemmes = femmeService.findAll();
                allFemmes.forEach(f -> System.out.println(f.getNom() + " " + f.getPrenom() + " (né le : " + f.getDateNaissance() + ")"));

                Femme plusAgee = allFemmes.stream()
                        .min((a, b) -> a.getDateNaissance().compareTo(b.getDateNaissance()))
                        .orElse(null);
                System.out.println("\nFemme la plus âgée : " + (plusAgee != null ? plusAgee.getNom() + " " + plusAgee.getPrenom() + " - " + plusAgee.getDateNaissance() : "Aucune"));

                System.out.println("\nÉpouses de " + h1.getNom() + " " + h1.getPrenom() + " :");
                List<Mariage> mariagesH1 = femmeService.getMariagesOfHomme(h1.getId());
                for (Mariage m : mariagesH1) {
                    System.out.println("- " + (m.getFemme() != null ? (m.getFemme().getNom() + " " + m.getFemme().getPrenom()) : "N/A")
                            + "  (Début: " + (m.getDateDebut() != null ? m.getDateDebut().toString() : "") + ")");
                }

                LocalDate d1 = LocalDate.of(1980, 1, 1);
                LocalDate d2 = LocalDate.of(2010, 12, 31);
                int nbEnfantsF1 = femmeService.getNombreEnfantsEntreDatesNative(f1.getId(), d1, d2);
                System.out.println("\nNombre d'enfants de " + f1.getNom() + " " + f1.getPrenom() + " entre " + d1 + " et " + d2 + " : " + nbEnfantsF1);

                System.out.println("\nFemmes mariées 2 fois ou plus :");
                List<Femme> femmesDeuxFois = femmeService.findFemmesMarriedAtLeastTwice();
                if (femmesDeuxFois.isEmpty()) System.out.println("Aucune");
                else femmesDeuxFois.forEach(f -> System.out.println("- " + f.getNom() + " " + f.getPrenom()));

                LocalDate dd = LocalDate.of(1999, 1, 1);
                LocalDate df = LocalDate.of(2025, 12, 31);
                int nbHommes4Femmes = femmeService.countHommesMarriedToFourWomenBetweenDates(dd, df);
                System.out.println("\nNombre d'hommes mariés à 4 femmes entre " + dd + " et " + df + " : " + nbHommes4Femmes);

                System.out.println("\nMariages détaillés pour " + h1.getNom() + " " + h1.getPrenom() + " :\n");
                System.out.println(femmeService.formatMariagesOfHommeForPrint(h1.getId()));

                // return value for TransactionCallback (not used)
                return null;
            });

        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }
}
