package org.example.classes;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private Set<LigneCommandeProduit> lignes;

    public Commande(Integer id, Date date, Set<LigneCommandeProduit> lignes) {
        this.id = id;
        this.date = date;
        this.lignes = lignes;
    }

    public Commande() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<LigneCommandeProduit> getLignes() {
        return lignes;
    }

    public void setLignes(Set<LigneCommandeProduit> lignes) {
        this.lignes = lignes;
    }
}

