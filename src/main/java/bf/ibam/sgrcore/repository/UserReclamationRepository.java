package bf.ibam.sgrcore.repository;

import bf.ibam.sgrcore.domain.UserReclamation;
import bf.ibam.sgrcore.domain.pk.UserReclamationPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserReclamationRepository extends JpaRepository<UserReclamation, UserReclamationPk> {

    @Query("SELECT ur FROM UserReclamation ur WHERE ur.user.id=:idUser AND ur.reclamation.id=:idReclamation")
    UserReclamation findUserReclamationByIdUserIdReclamation(@Param("idUser") Long idDirecteur, @Param("idReclamation") Long idReclamation);

    @Query("SELECT COUNT(ur) > 0 FROM UserReclamation ur WHERE ur.userReclamationPk.idUser = :idUser")
    boolean isReclamationsAffectedToUser(@Param("idUser") Long idUser);

    @Query("SELECT COUNT(ur) > 0 FROM UserReclamation ur WHERE ur.userReclamationPk.idReclamation = :idReclamation")
    boolean isReclamationAffectedToUser1(@Param("idReclamation") Long idReclamation);

    @Query("SELECT COUNT(ur) > 1 FROM UserReclamation ur WHERE ur.userReclamationPk.idReclamation = :idReclamation")
    boolean isReclamationAffectedToUser2(@Param("idReclamation") Long idReclamation);



}
