package bf.ibam.sgrcore.domain.DTO;

import bf.ibam.sgrcore.domain.Reclamation;

public class UserReclamationDto {

    private Long idUser;
    private Reclamation reclamation;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

}
