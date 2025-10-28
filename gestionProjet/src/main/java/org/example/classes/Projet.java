package org.example.classes;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projet")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    // Chef de projet (employé)
    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Employe chef;

    // Tâches du projet
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tache> taches = new HashSet<>();

    public Projet() {}

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public Employe getChef() { return chef; }
    public void setChef(Employe chef) { this.chef = chef; }
    public Set<Tache> getTaches() { return taches; }
    public void setTaches(Set<Tache> taches) { this.taches = taches; }

    // helper
    public void addTache(Tache t) {
        taches.add(t);
        t.setProjet(this);
    }
    public void removeTache(Tache t) {
        taches.remove(t);
        t.setProjet(null);
    }
}


