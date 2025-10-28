package org.example.classes;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String libelle;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private Set<Produit> produits;

    public Categorie(Integer id, String code, String libelle, Set<Produit> produits) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.produits = produits;
    }

    public Categorie() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
}

