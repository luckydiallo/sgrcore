//package bf.ibam.sgrcore.domain;
//
//import bf.ibam.sgrcore.domain.pk.ProfesseurReclamationPk;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.Objects;
//
//@Entity
//@Table(name = "professeur_reclamation")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//public class ProfesseurReclamation implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    private ProfesseurReclamationPk professeurReclamationPk;
//
//
//    @Column(name = "date_attribution", nullable = false, updatable = false)
//    @NotNull
//    @CreatedDate
//    private LocalDate dateAttribution;
//
//    @ManyToOne(optional = false)
//    @NotNull
//    @JoinColumn(name = "id_professeur", referencedColumnName = "id", insertable = false, updatable = false)
//    private Professeur professeur;
//
//    @ManyToOne(optional = false)
//    @NotNull
//    @JoinColumn(name = "id_reclamation", referencedColumnName = "id", insertable = false, updatable = false)
//    private Reclamation reclamation;
//
//    public ProfesseurReclamation() {
//        if (this.dateAttribution == null) {
//            this.dateAttribution = LocalDate.now();
//        }
//    }
//
//    public ProfesseurReclamation(ProfesseurReclamationPk professeurReclamationPk) {
//        this.professeurReclamationPk = professeurReclamationPk;
//    }
//
//    public ProfesseurReclamationPk getAgentReclamationPk() {
//        return professeurReclamationPk;
//    }
//
//    public void setProfesseurReclamationPk(ProfesseurReclamationPk professeurReclamationPk) {
//        this.professeurReclamationPk = professeurReclamationPk;
//    }
//
//    public Reclamation getReclamation() {
//        return reclamation;
//    }
//
//    public void setReclamation(Reclamation reclamation) {
//        this.reclamation = reclamation;
//    }
//
//    public Professeur getProfesseur() {
//        return professeur;
//    }
//
//    public void setProfesseur(Professeur professeur) {
//        this.professeur = professeur;
//    }
//
//    public LocalDate getDateAttribution() {
//        return dateAttribution;
//    }
//
//    public void setDateAttribution(LocalDate dateAttribution) {
//        this.dateAttribution = dateAttribution;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 71 * hash + Objects.hashCode(this.professeurReclamationPk);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final ProfesseurReclamation other = (ProfesseurReclamation) obj;
//        return Objects.equals(this.professeurReclamationPk, other.professeurReclamationPk);
//    }
//}
