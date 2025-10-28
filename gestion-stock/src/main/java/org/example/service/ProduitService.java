package org.example.service;


import org.example.classes.Produit;

import java.util.Date;
import java.util.List;

public interface ProduitService {
    boolean create(Produit p);
    Produit update(Produit p);
    void delete(Integer id);
    Produit findById(Integer id);
    List<Produit> findAll();
    List<Produit> findByCategorie(Integer categorieId);
    List<Produit> findProductsOrderedBetweenDates(Date d1, Date d2);
    List<Produit> findProductsInCommande(Integer commandeId);
    List<Produit> findProduitsPrixSup(double prix);
}
