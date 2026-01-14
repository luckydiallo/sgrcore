//package bf.ibam.sgrcore.repository;
//
//import bf.ibam.sgrcore.domain.DirecteurReclamation;
//import bf.ibam.sgrcore.domain.ProfesseurReclamation;
//import bf.ibam.sgrcore.domain.pk.DirecteurReclamationPk;
//import bf.ibam.sgrcore.domain.pk.ProfesseurReclamationPk;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface ProfesseurReclamationRepository extends JpaRepository<ProfesseurReclamation, ProfesseurReclamationPk> {
//
//    @Query("SELECT pr FROM ProfesseurReclamation pr WHERE pr.professeur.id=:idProfesseur AND pr.reclamation.id=:idReclamation")
//    ProfesseurReclamation findProfesseurReclamationByIdProfesseurIdReclamation(@Param("idProfesseur") Long idProfesseur, @Param("idReclamation") Long idReclamation);
//
//    @Query("SELECT COUNT(pr) > 0 FROM ProfesseurReclamation pr WHERE pr.professeurReclamationPk.idProfesseur = :idProfesseur")
//    boolean isReclamationsAffectedToProfesseur(@Param("idProfesseur") Long idProfesseur);
//
//    @Query("SELECT COUNT(pr) > 0 FROM ProfesseurReclamation pr WHERE pr.professeurReclamationPk.idReclamation = :idReclamation")
//    boolean isReclamationAffectedToProfesseur1(@Param("idReclamation") Long idReclamation);
//
//
//}
