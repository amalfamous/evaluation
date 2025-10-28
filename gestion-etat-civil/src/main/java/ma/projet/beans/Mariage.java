package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mariage")
@NamedNativeQuery(
        name = "Mariage.countChildrenOfFemmeBetweenDates",
        query = "SELECT COALESCE(SUM(nbr_enfant), 0) FROM mariage WHERE femme_id = :femmeId AND date_debut BETWEEN :d1 AND :d2"
)
public class Mariage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "nbr_enfant")
    private Integer nbrEnfant;

    @ManyToOne
    @JoinColumn(name = "femme_id")
    private Femme femme;

    @ManyToOne
    @JoinColumn(name = "homme_id")
    private Homme homme;

    public Mariage() {}

    public Mariage(Homme homme, Femme femme, LocalDate dateDebut, LocalDate dateFin, Integer nbrEnfant) {
        this.homme = homme;
        this.femme = femme;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nbrEnfant = nbrEnfant;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Integer getNbrEnfant() { return nbrEnfant; }
    public void setNbrEnfant(Integer nbrEnfant) { this.nbrEnfant = nbrEnfant; }

    public Femme getFemme() { return femme; }
    public void setFemme(Femme femme) { this.femme = femme; }

    public Homme getHomme() { return homme; }
    public void setHomme(Homme homme) { this.homme = homme; }
}
