package bf.ibam.sgrcore.domain.DTO;

import bf.ibam.sgrcore.domain.Filiere;
import bf.ibam.sgrcore.domain.Matiere;
import bf.ibam.sgrcore.domain.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@ToString
public class ReclamationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String nomReclamant;

    @NotNull
    @Size(max = 150)
    private String prenomReclamant;

    private String niveauEtude;

    @NotNull
    private String objetReclamation;

    @NotNull
    private String motifReclamation;

    private byte[] justificatif;

    private String justificatifContentType;

    private String etatReclamation;

    private Float  noteActuelle;

    private Float  noteCorrige;

    private String contactProfesseur;

    private LocalDate dateEnregistrement;

    /* ===== Relations ===== */

    @NotNull
    private Filiere filiere;

    @NotNull
    private Matiere matiere;

    @NotNull
    private User etudiant;

    @NotNull
    private User professeur;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomReclamant() {
        return nomReclamant;
    }

    public void setNomReclamant(String nomReclamant) {
        this.nomReclamant = nomReclamant;
    }

    public String getPrenomReclamant() {
        return prenomReclamant;
    }

    public void setPrenomReclamant(String prenomReclamant) {
        this.prenomReclamant = prenomReclamant;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getObjetReclamation() {
        return objetReclamation;
    }

    public void setObjetReclamation(String objetReclamation) {
        this.objetReclamation = objetReclamation;
    }

    public String getMotifReclamation() {
        return motifReclamation;
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

    public String getEtatReclamation() {
        return etatReclamation;
    }

    public void setEtatReclamation(String etatReclamation) {
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
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
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
}

