package bf.ibam.sgrcore.domain;

import bf.ibam.sgrcore.domain.pk.UserReclamationPk;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "User_reclamation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserReclamation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UserReclamationPk userReclamationPk;


    @Column(name = "date_attribution", nullable = false, updatable = false)
    @NotNull
    @CreatedDate
    private LocalDate dateAttribution;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "id_user", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "id_reclamation", referencedColumnName = "id", insertable = false, updatable = false)
    private Reclamation reclamation;

    public UserReclamation() {
        if (this.dateAttribution == null) {
            this.dateAttribution = LocalDate.now();
        }
    }

    public UserReclamation(UserReclamationPk userReclamationPk) {
        this.userReclamationPk = userReclamationPk;
    }

    public UserReclamationPk getUserReclamationPk() {
        return userReclamationPk;
    }

    public void setUserReclamationPk(UserReclamationPk userReclamationPk) {
        this.userReclamationPk = userReclamationPk;
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDate dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.userReclamationPk);
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
        final UserReclamation other = (UserReclamation) obj;
        return Objects.equals(this.userReclamationPk, other.userReclamationPk);
    }
}
