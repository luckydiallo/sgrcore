package bf.ibam.sgrcore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ETUDIANT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant extends User {

    private String niveauEtude;

    @Column(name = "filiere_id")
    private Long filiereId;

}
