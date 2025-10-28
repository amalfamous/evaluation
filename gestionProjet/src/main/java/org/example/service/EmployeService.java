package org.example.service;


import org.example.classes.Employe;
import org.example.classes.Projet;
import org.example.classes.Tache;

import java.util.List;

public interface EmployeService {
    boolean create(Employe e);
    boolean update(Employe e);
    boolean delete(Employe e);
    Employe findById(int id);
    List<Employe> findAll();

    // Méthodes demandées
    List<Tache> findTachesRealiseesParEmploye(int employeId); // tâches avec dateFinReelle non null
    List<Projet> findProjetsGeresParEmploye(int employeId);
}
