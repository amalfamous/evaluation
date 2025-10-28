package org.example.classes;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tache")
@NamedQueries({
        @NamedQuery(name = "Tache.findPrixSup", query = "SELECT t FROM Tache t WHERE t.prix > :prix")
})
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private double prix;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeTache> employeTaches = new HashSet<>();

    public Tache() {}

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }

    public Set<EmployeTache> getEmployeTaches() { return employeTaches; }
    public void setEmployeTaches(Set<EmployeTache> employeTaches) { this.employeTaches = employeTaches; }

    // helper
    public void addEmployeTache(EmployeTache et) {
        employeTaches.add(et);
        et.setTache(this);
    }
    public void removeEmployeTache(EmployeTache et) {
        employeTaches.remove(et);
        et.setTache(null);
    }
}
