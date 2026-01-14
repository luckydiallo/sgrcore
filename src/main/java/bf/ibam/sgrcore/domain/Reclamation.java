package bf.ibam.sgrcore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Reclamation.
 */
@Entity
@Table(name = "reclamation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reclamation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "nom_reclamant", length = 150, nullable = false)
    private String nomReclamant;

    @NotNull
    @Size(max = 150)
    @Column(name = "prenom_reclamant", length = 150, nullable = false)
    private String prenomReclamant;

    @Column(name = "niveau_etude")
    private String niveauEtude;

    @NotNull
    @Column(name = "objet_reclamation", nullable = false)
    private String objetReclamation;

    @NotNull
    @Column(name = "motif_reclamation", nullable = false)
    private String motifReclamation;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "justificatif")
    private byte[] justificatif;

    @Column(name = "justificatif_content_type")
    private String justificatifContentType;


    @NotNull
    @Column(name = "etat_reclamation", nullable = false)
    private EtatReclamation etatReclamation;

    @NotNull
    @Column(name = "note_actuelle", nullable = false)
    private Float  noteActuelle;

    @Column(name = "note_corrige")
    private Float  noteCorrige;

    @Column(name = "contact_professeur")
    private String contactProfesseur;

    @Column(name = "date_enregistrement", nullable = false, updatable = false)
    private LocalDate dateEnregistrement;

    @ManyToOne(optional = false)
    @NotNull
    private Filiere filiere;

    @ManyToOne(optional = false)
    @NotNull
    private Matiere matiere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private User etudiant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professeur_id", nullable = false)
    private User professeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reclamation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomReclamant() {
        return this.nomReclamant;
    }

    public Reclamation nomReclamant(String nomReclamant) {
        this.setNomReclamant(nomReclamant);
        return this;
    }

    public void setNomReclamant(String nomReclamant) {
        this.nomReclamant = nomReclamant;
    }

    public String getPrenomReclamant() {
        return this.prenomReclamant;
    }

    public Reclamation prenomReclamant(String prenomReclamant) {
        this.setPrenomReclamant(prenomReclamant);
        return this;
    }

    public void setPrenomReclamant(String prenomReclamant) {
        this.prenomReclamant = prenomReclamant;
    }

    public String getNiveauEtude() {
        return this.niveauEtude;
    }

    public Reclamation niveauEtude(String niveauEtude) {
        this.setNiveauEtude(niveauEtude);
        return this;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getObjetReclamation() {
        return this.objetReclamation;
    }

    public Reclamation objetReclamation(String objetReclamation) {
        this.setObjetReclamation(objetReclamation);
        return this;
    }

    public void setObjetReclamation(String objetReclamation) {
        this.objetReclamation = objetReclamation;
    }

    public String getMotifReclamation() {
        return this.motifReclamation;
    }

    public Reclamation motifReclamation(String motifReclamation) {
        this.setMotifReclamation(motifReclamation);
        return this;
    }

    public void setMotifReclamation(String motifReclamation) {
        this.motifReclamation = motifReclamation;
    }

    public byte[] getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(byte[] justificatif) {
        this.justificatif = justificatif;
    }

    public String getJustificatifContentType() {
        return justificatifContentType;
    }

    public void setJustificatifContentType(String justificatifContentType) {
        this.justificatifContentType = justificatifContentType;
    }

    public EtatReclamation getEtatReclamation() {
        return this.etatReclamation;
    }

    public Reclamation etatReclamation(EtatReclamation etatReclamation) {
        this.setEtatReclamation(etatReclamation);
        return this;
    }

    public void setEtatReclamation(EtatReclamation etatReclamation) {
        this.etatReclamation = etatReclamation;
    }

    public Float getNoteActuelle() {
        return noteActuelle;
    }

    public void setNoteActuelle(Float noteActuelle) {
        this.noteActuelle = noteActuelle;
    }

    public Float getNoteCorrige() {
        return noteCorrige;
    }

    public void setNoteCorrige(Float noteCorrige) {
        this.noteCorrige = noteCorrige;
    }

    public String getContactProfesseur() {
        return contactProfesseur;
    }

    public void setContactProfesseur(String contactProfesseur) {
        this.contactProfesseur = contactProfesseur;
    }

    public LocalDate getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDate dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Reclamation filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Reclamation matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public User getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(User etudiant) {
        this.etudiant = etudiant;
    }

    public User getProfesseur() {
        return professeur;
    }

    public void setProfesseur(User professeur) {
        this.professeur = professeur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reclamation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reclamation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reclamation{" +
            "id=" + getId() +
            ", nomReclamant='" + getNomReclamant() + "'" +
            ", prenomReclamant='" + getPrenomReclamant() + "'" +
            ", niveauEtude='" + getNiveauEtude() + "'" +
            ", objetReclamation='" + getObjetReclamation() + "'" +
            ", motifReclamation='" + getMotifReclamation() + "'" +
            ", justificatif='" + getJustificatif() + "'" +
            ", justificatifContentType='" + getJustificatifContentType() + "'" +
            ", etatReclamation='" + getEtatReclamation() + "'" +
            ", noteActuelle='" + getNoteActuelle() + "'" +
            ", noteCorrige='" + getNoteCorrige() + "'" +
            ", contactProfesseur='" + getContactProfesseur() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            "}";
    }
}
