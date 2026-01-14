package bf.ibam.sgrcore.repository;

import bf.ibam.sgrcore.domain.Reclamation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Reclamation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    @Query("SELECT r FROM Reclamation r WHERE r.id IN (SELECT ur.reclamation.id FROM UserReclamation ur WHERE ur.user.id=:idUser) ORDER BY r.id asc")
    List<Reclamation> findAffectesAUnUtilisateur(@Param("idUser") Long idUser);

    @Query("SELECT r FROM Reclamation r WHERE r.id IN (SELECT ur.reclamation.id FROM UserReclamation ur WHERE ur.user.id=:idUser) ORDER BY r.id asc")
    Page<Reclamation> findAllAffectesAUnUtilisateur( Pageable pageable, @Param("idUser") Long idUser);

    @Query("SELECT r FROM Reclamation r WHERE etudiant.id=:idUser ORDER BY r.id asc")
    Page<Reclamation> findAllByEtudiantId( Pageable pageable, @Param("idUser") Long idUser);

    @Query("SELECT r FROM Reclamation r WHERE r.id NOT IN (SELECT ur.reclamation.id FROM UserReclamation ur) ORDER BY r.id asc")
    Page<Reclamation> findNonAffectesAUnUtilisateur(Pageable pageable);

    @Query("SELECT r FROM Reclamation r WHERE r.id IN (SELECT ur.reclamation.id FROM UserReclamation ur WHERE ur.user.id=:idUser) "
        + "AND r.id NOT IN (SELECT ur2.reclamation.id FROM UserReclamation ur2 WHERE ur2.user.id!=:idUser) "
        + "ORDER BY r.id asc")
    Page<Reclamation> findNonAffectesAUnAutreUtilisateur(Pageable pageable, @Param("idUser") Long idUser);

}
