package bf.ibam.sgrcore.domain.pk;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserReclamationPk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "id_user")
    private Long idUser;
    @Basic(optional = false)
    @Column(name = "id_reclamation")
    private Long idReclamation;

    public UserReclamationPk() {
    }

    public UserReclamationPk(Long idUser, Long idReclamation) {
        this.idUser = idUser;
        this.idReclamation = idReclamation;
    }

    public Long getIdReclamation() {return idReclamation;}

    public void setIdReclamation(Long idReclamation) {
        this.idReclamation = idReclamation;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.idUser);
        hash = 59 * hash + Objects.hashCode(this.idReclamation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserReclamationPk other = (UserReclamationPk) obj;
        if (!Objects.equals(this.idUser, other.idUser)) {
            return false;
        }
        return Objects.equals(this.idReclamation, other.idReclamation);
    }
}

