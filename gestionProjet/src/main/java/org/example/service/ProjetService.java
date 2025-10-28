package org.example.service;

import org.example.classes.Projet;
import org.example.classes.Tache;

import java.util.List;

public interface ProjetService {
    boolean create(Projet p);
    boolean update(Projet p);
    boolean delete(Projet p);
    Projet findById(int id);
    List<Projet> findAll();

    // Méthodes demandées
    List<Tache> findTachesPlanifiees(int projetId); // taches planifiées (toutes taches du projet)
    List<Object[]> findTachesRealiseesAvecDates(int projetId); // récupère tache + dates réelles (via EmployeTache)
}
