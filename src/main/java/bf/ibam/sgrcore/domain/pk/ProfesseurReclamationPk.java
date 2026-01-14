//package bf.ibam.sgrcore.domain.pk;
//
//import jakarta.persistence.Basic;
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//public class ProfesseurReclamationPk implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Basic(optional = false)
//    @Column(name = "id_professeur")
//    private Long idProfesseur;
//    @Basic(optional = false)
//    @Column(name = "id_reclamation")
//    private Long idReclamation;
//
//    public ProfesseurReclamationPk() {
//    }
//
//    public ProfesseurReclamationPk(Long idProfesseur, Long idReclamation) {
//        this.idProfesseur = idProfesseur;
//        this.idReclamation = idReclamation;
//    }
//
//    public Long getIdReclamation() {return idReclamation;}
//
//    public void setIdReclamation(Long idReclamation) {
//        this.idReclamation = idReclamation;
//    }
//
//    public Long getIdProfesseur() {
//        return idProfesseur;
//    }
//
//    public void setIdProfesseur(Long idAgent) {
//        this.idProfesseur = idProfesseur;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 59 * hash + Objects.hashCode(this.idProfesseur);
//        hash = 59 * hash + Objects.hashCode(this.idReclamation);
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
//        final ProfesseurReclamationPk other = (ProfesseurReclamationPk) obj;
//        if (!Objects.equals(this.idProfesseur, other.idProfesseur)) {
//            return false;
//        }
//        return Objects.equals(this.idReclamation, other.idReclamation);
//    }
//}
