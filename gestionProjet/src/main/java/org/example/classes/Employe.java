package org.example.classes;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employe")
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String prenom;
    private String telephone;

    // Projets dont cet employé est chef
    @OneToMany(mappedBy = "chef")
    private Set<Projet> projetsGeres  = new HashSet<>();

    // Association vers les tâches réalisées / affectées
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeTache> employeTaches = new HashSet<>();

    public Employe() {}

    public Employe(Integer id, String nom, String prenom, String telephone, Set<Projet> projetsGeres , Set<EmployeTache> employeTaches) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.projetsGeres  = projetsGeres ;
        this.employeTaches = employeTaches;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Set<Projet> getProjetsGeres () { return projetsGeres ; }
    public void setProjetsGeres (Set<Projet> projetsGeres ) { this.projetsGeres  = projetsGeres ; }

    public Set<EmployeTache> getEmployeTaches() { return employeTaches; }
    public void setEmployeTaches(Set<EmployeTache> employeTaches) { this.employeTaches = employeTaches; }


    // helper pour conserver la bidirectionnalité
    public void addEmployeTache(EmployeTache et) {
        employeTaches.add(et);
        et.setEmploye(this);
    }
    public void removeEmployeTache(EmployeTache et) {
        employeTaches.remove(et);
        et.setEmploye(null);
    }
}
