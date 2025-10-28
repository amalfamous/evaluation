package org.example;

import org.example.classes.Employe;
import org.example.classes.EmployeTache;
import org.example.classes.Projet;
import org.example.classes.Tache;
import org.example.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App 
{
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Services
        EmployeService employeService = new EmployeServiceImpl();
        ProjetService projetService = new ProjetServiceImpl();
        TacheService tacheService = new TacheServiceImpl();
        EmployeTacheService etService = new EmployeTacheServiceImpl();

        // 1. Création des employés
        Employe emp1 = new Employe();
        emp1.setNom("Ali");
        emp1.setPrenom("Ahmed");
        emp1.setTelephone("0600000001");

        Employe emp2 = new Employe();
        emp2.setNom("Sara");
        emp2.setPrenom("Ben");
        emp2.setTelephone("0600000002");

        employeService.create(emp1);
        employeService.create(emp2);

        // 2. Création d'un projet
        Projet projet1 = new Projet();
        projet1.setNom("Gestion de stock");
        projet1.setDateDebut(sdf.parse("14/01/2013"));
        projet1.setDateFin(sdf.parse("30/04/2013"));
        projet1.setChef(emp1); // Ali est le chef

        projetService.create(projet1);

        // 3. Création de tâches
        Tache t1 = new Tache();
        t1.setNom("Analyse");
        t1.setDateDebut(sdf.parse("10/02/2013"));
        t1.setDateFin(sdf.parse("20/02/2013"));
        t1.setPrix(800);
        t1.setProjet(projet1);

        Tache t2 = new Tache();
        t2.setNom("Conception");
        t2.setDateDebut(sdf.parse("10/03/2013"));
        t2.setDateFin(sdf.parse("15/03/2013"));
        t2.setPrix(1200);
        t2.setProjet(projet1);

        Tache t3 = new Tache();
        t3.setNom("Développement");
        t3.setDateDebut(sdf.parse("10/04/2013"));
        t3.setDateFin(sdf.parse("25/04/2013"));
        t3.setPrix(2000);
        t3.setProjet(projet1);

        tacheService.create(t1);
        tacheService.create(t2);
        tacheService.create(t3);

        // 4. Affectation des tâches aux employés avec dates réelles
        EmployeTache et1 = new EmployeTache();
        et1.setEmploye(emp2);
        et1.setTache(t1);
        et1.setDateDebutReelle(sdf.parse("10/02/2013"));
        et1.setDateFinReelle(sdf.parse("20/02/2013"));

        EmployeTache et2 = new EmployeTache();
        et2.setEmploye(emp2);
        et2.setTache(t2);
        et2.setDateDebutReelle(sdf.parse("10/03/2013"));
        et2.setDateFinReelle(sdf.parse("15/03/2013"));

        etService.create(et1);
        etService.create(et2);

        // ======= AFFICHAGE =======

        System.out.println("=== Liste des projets gérés par Ali ===");
        List<Projet> projetsAli = employeService.findProjetsGeresParEmploye(emp1.getId());
        for (Projet p : projetsAli) {
            System.out.println("Projet : " + p.getId() + "  Nom : " + p.getNom() + "  Date début : " + sdf.format(p.getDateDebut()));
        }

        System.out.println("\n=== Liste des tâches réalisées par Sara ===");
        List<Tache> tachesSara = employeService.findTachesRealiseesParEmploye(emp2.getId());
        for (Tache t : tachesSara) {
            System.out.println("Tâche : " + t.getNom() + " du projet " + t.getProjet().getNom());
        }

        System.out.println("\n=== Tâches planifiées pour le projet Gestion de stock ===");
        List<Tache> tachesProjet = projetService.findTachesPlanifiees(projet1.getId());
        for (Tache t : tachesProjet) {
            System.out.println("Num " + t.getId() + "  Nom : " + t.getNom() + "  Prix : " + t.getPrix());
        }

        System.out.println("\n=== Tâches réalisées avec dates réelles pour le projet Gestion de stock ===");
        List<Object[]> tachesRealisees = projetService.findTachesRealiseesAvecDates(projet1.getId());
        System.out.println("Num Nom           Date Début Réelle  Date Fin Réelle");
        for (Object[] obj : tachesRealisees) {
            System.out.println(obj[0] + "  " + obj[1] + "  " + sdf.format((Date)obj[2]) + "  " + sdf.format((Date)obj[3]));
        }

        System.out.println("\n=== Tâches dont le prix > 1000 DH ===");
        List<Tache> tachesChere = tacheService.findTachesPrixSup(1000);
        for (Tache t : tachesChere) {
            System.out.println("Tâche : " + t.getNom() + "  Prix : " + t.getPrix());
        }

        System.out.println("\n=== Tâches réalisées entre 01/02/2013 et 31/03/2013 ===");
        Date d1 = sdf.parse("01/02/2013");
        Date d2 = sdf.parse("31/03/2013");
        List<Tache> tachesEntre = tacheService.findTachesRealiseesEntre(d1, d2);
        for (Tache t : tachesEntre) {
            System.out.println("Tâche : " + t.getNom() + " du projet " + t.getProjet().getNom());
        }
    }
}