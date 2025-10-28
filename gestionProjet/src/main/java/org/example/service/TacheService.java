package org.example.service;
import org.example.classes.Tache;

import java.util.Date;
import java.util.List;

public interface TacheService {
    boolean create(Tache t);
    boolean update(Tache t);
    boolean delete(Tache t);
    Tache findById(int id);
    List<Tache> findAll();

    // Méthodes demandées
    List<Tache> findTachesPrixSup(double prix); // named query
    List<Tache> findTachesRealiseesEntre(Date d1, Date d2); // via EmployeTache.dateFinReelle
}
