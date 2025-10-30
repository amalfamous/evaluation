package ma.projet.classes;


import javax.persistence.*;
import java.util.Set;

@Entity
@NamedQuery(
        name="Produit.findByPriceGreaterThan100",
        query="select p from Produit p where p.prix > :prix"
)
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String reference;
    private float prix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LigneCommandeProduit> lignes;

    public Produit(Integer id, String reference, float prix, Categorie categorie, Set<LigneCommandeProduit> lignes) {
        this.id = id;
        this.reference = reference;
        this.prix = prix;
        this.categorie = categorie;
        this.lignes = lignes;
    }

    public Produit() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<LigneCommandeProduit> getLignes() {
        return lignes;
    }

    public void setLignes(Set<LigneCommandeProduit> lignes) {
        this.lignes = lignes;
    }
}
