package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        // Créer 10 femmes
        Femme f1 = new Femme("RAMI", "SALIMA", "0661111111", "Casa", LocalDate.of(1970, 5, 15));
        Femme f2 = new Femme("ALI", "AMAL", "0662222222", "Rabat", LocalDate.of(1975, 8, 20));
        Femme f3 = new Femme("ALAOUI", "WAFA", "0663333333", "Fès", LocalDate.of(1980, 3, 10));
        Femme f4 = new Femme("ALAMI", "KARIMA", "0664444444", "Marrakech", LocalDate.of(1968, 12, 5));
        Femme f5 = new Femme("BENNANI", "FATIMA", "0665555555", "Tanger", LocalDate.of(1985, 7, 25));
        Femme f6 = new Femme("IDRISSI", "LAILA", "0666666666", "Agadir", LocalDate.of(1978, 11, 30));
        Femme f7 = new Femme("TAZI", "SAMIRA", "0667777777", "Meknès", LocalDate.of(1982, 4, 18));
        Femme f8 = new Femme("FASSI", "NADIA", "0668888888", "Tétouan", LocalDate.of(1972, 9, 22));
        Femme f9 = new Femme("SEFRIOUI", "HAFSA", "0669999999", "Oujda", LocalDate.of(1987, 1, 8));
        Femme f10 = new Femme("ALAOUI", "MALIKA", "0660000000", "Safi", LocalDate.of(1965, 6, 14));

        fs.create(f1); fs.create(f2); fs.create(f3); fs.create(f4); fs.create(f5);
        fs.create(f6); fs.create(f7); fs.create(f8); fs.create(f9); fs.create(f10);

        // Créer 5 hommes
        Homme h1 = new Homme("SAFI", "SAID", "0671111111", "Casa", LocalDate.of(1965, 3, 10));
        Homme h2 = new Homme("ALAMI", "AHMED", "0672222222", "Rabat", LocalDate.of(1970, 6, 15));
        Homme h3 = new Homme("BENNANI", "YOUSSEF", "0673333333", "Fès", LocalDate.of(1968, 9, 20));
        Homme h4 = new Homme("TAZI", "KARIM", "0674444444", "Marrakech", LocalDate.of(1975, 12, 5));
        Homme h5 = new Homme("IDRISSI", "OMAR", "0675555555", "Tanger", LocalDate.of(1980, 2, 28));

        hs.create(h1); hs.create(h2); hs.create(h3); hs.create(h4); hs.create(h5);

        // Créer des mariages pour h1 (comme dans l'exemple)
        ms.create(new Mariage(h1, f4, LocalDate.of(1989, 9, 3), LocalDate.of(1990, 9, 3), 0));
        ms.create(new Mariage(h1, f1, LocalDate.of(1990, 9, 3), null, 4));
        ms.create(new Mariage(h1, f2, LocalDate.of(1995, 9, 3), null, 2));
        ms.create(new Mariage(h1, f3, LocalDate.of(2000, 11, 4), null, 3));

        // Autres mariages
        ms.create(new Mariage(h2, f5, LocalDate.of(1995, 5, 20), null, 3));
        ms.create(new Mariage(h3, f1, LocalDate.of(2005, 3, 15), null, 1));
        ms.create(new Mariage(h4, f2, LocalDate.of(2000, 8, 10), null, 2));

        // Tests
        System.out.println("===== Liste des femmes =====");
        fs.findAll().forEach(f -> System.out.println(f.getPrenom() + " " + f.getNom()));

        System.out.println("\n===== Femme la plus âgée =====");
        Femme oldest = fs.findOldestFemme();
        System.out.println(oldest.getPrenom() + " " + oldest.getNom() + " - " + oldest.getDateNaissance());

        System.out.println("\n===== Épouses de h1 entre 1990-2000 =====");
        hs.getEpousesBetweenDates(h1, LocalDate.of(1990, 1, 1), LocalDate.of(2000, 12, 31))
                .forEach(f -> System.out.println(f.getPrenom() + " " + f.getNom()));

        System.out.println("\n===== Enfants de f1 entre 1990-2000 =====");
        System.out.println("Nombre: " + fs.countChildrenBetweenDates(f1,
                LocalDate.of(1990, 1, 1), LocalDate.of(2000, 12, 31)));

        System.out.println("\n===== Femmes mariées au moins 2 fois =====");
        fs.findFemmesMarriedAtLeastTwice()
                .forEach(f -> System.out.println(f.getPrenom() + " " + f.getNom()));

        System.out.println("\n===== Hommes mariés à 4 femmes (1989-2001) =====");
        System.out.println("Nombre: " + hs.countHommesMarriedTo4FemmesBetweenDates(
                LocalDate.of(1989, 1, 1), LocalDate.of(2001, 12, 31)));

        System.out.println("\n===== Mariages de h1 avec détails =====");
        hs.displayMariagesOfHomme(h1);

        HibernateUtil.shutdown();
    }}
