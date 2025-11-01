package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "femme")
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Mariage> mariages = new HashSet<>();

    public Femme() {}

    public Femme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public Set<Mariage> getMariages() { return mariages; }
    public void setMariages(Set<Mariage> mariages) { this.mariages = mariages; }

    // helpers
    public void addMariage(Mariage mariage) {
        mariages.add(mariage);
        mariage.setFemme(this);
    }

    public void removeMariage(Mariage mariage) {
        mariages.remove(mariage);
        mariage.setFemme(null);
    }
}