package bf.ibam.sgrcore.service;

import bf.ibam.sgrcore.domain.*;
import bf.ibam.sgrcore.domain.DTO.ReclamationDTO;
import bf.ibam.sgrcore.domain.DTO.UserReclamationDto;
import bf.ibam.sgrcore.domain.pk.UserReclamationPk;
import bf.ibam.sgrcore.repository.ReclamationRepository;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import bf.ibam.sgrcore.repository.UserReclamationRepository;
import bf.ibam.sgrcore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static bf.ibam.sgrcore.domain.EtatReclamation.ACCEPTEE;
import static bf.ibam.sgrcore.domain.EtatReclamation.REJETEE;
import static bf.ibam.sgrcore.domain.UserProfile.SCOLARITE;

/**
 * Service Implementation for managing {@link bf.ibam.sgrcore.domain.Reclamation}.
 */
@Service
@Transactional
public class ReclamationService {

    private static final Logger LOG = LoggerFactory.getLogger(ReclamationService.class);

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;
    private final UserReclamationRepository userReclamationRepository;

    public ReclamationService(ReclamationRepository reclamationRepository, UserRepository userRepository, UserReclamationRepository userReclamationRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
        this.userReclamationRepository = userReclamationRepository;
    }

    /**
     * Save a reclamation.
     *
     * @param reclamationDTO the entity to save.
     * @return the persisted entity.
     */
    public ReclamationDTO save(ReclamationDTO reclamationDTO) {
        LOG.debug("Request to save Reclamation : {}", reclamationDTO);
        Reclamation reclamation = maptoReclamation(reclamationDTO);
        reclamation.setEtatReclamation(EtatReclamation.EN_COURS);
        reclamation.setDateEnregistrement(LocalDate.now());
        Reclamation reclamation1 = reclamationRepository.save(reclamation);
        ReclamationDTO reclamationDTO1 = maptoReclamationDTO(reclamation1);
        return reclamationDTO1;
    }


    /**
     *
     * Affecter Reclamations A User
     */

    public void affecterReclamationsAUser(UserReclamationDto userReclamationDto, Long idAccount) {
        if (userReclamationDto == null) {
            throw new RuntimeException("Problème d'affectation de(s) reclamation(s) à l'utilisateur : userReclamationDto semble null.");
        }
        Reclamation reclamationsAAffectee = userReclamationDto.getReclamation();
        User userAccount = userRepository.findById(idAccount)
            .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isScolarite = userAccount.getAuthorities()
            .stream()
            .anyMatch(authority -> "ROLE_SCOLARITE".equals(authority.getName()));

        if (!isScolarite) {
            Reclamation reclamation = reclamationsAAffectee;
            boolean isReclamationAffectedUser = userReclamationRepository.isReclamationAffectedToUser2(reclamation.getId());
            if (isReclamationAffectedUser) {
                throw new RuntimeException("la réclamation est déjà affecté à un Professeur");
            }
        } else{
            Reclamation reclamation = reclamationsAAffectee;
            boolean isReclamationAffectedUser = userReclamationRepository.isReclamationAffectedToUser1(reclamation.getId());
            if (isReclamationAffectedUser) {
                throw new RuntimeException("la réclamation est déjà affecté au directeur");
            }
        }
        // Récupérer les réclamations précédemment affectées
        List<Reclamation> reclamationsPrecedementAffectees = reclamationRepository.findAffectesAUnUtilisateur(userReclamationDto.getIdUser());
        if (reclamationsAAffectee != null) {
            User user = userRepository.getOne(userReclamationDto.getIdUser());
                boolean dejaAffectee = reclamationsPrecedementAffectees.stream()
                    .anyMatch(r -> r.getId().equals(reclamationsAAffectee.getId()));
                if (!dejaAffectee) {
                    UserReclamation ur = new UserReclamation();
                    ur.setReclamation(reclamationsAAffectee);
                    ur.setUser(user);
                    ur.setUserReclamationPk(new UserReclamationPk(userReclamationDto.getIdUser(), reclamationsAAffectee.getId()));
                    userReclamationRepository.save(ur);
                }
        }
    }

    @Transactional
    public Reclamation changerEtat(Long reclamationId, EtatReclamation nouvelEtat) {

        Reclamation reclamation = reclamationRepository.findById(reclamationId)
            .orElseThrow(() -> new RuntimeException("Réclamation introuvable"));

        if (!EnumSet.of(ACCEPTEE, REJETEE).contains(nouvelEtat)) {
            throw new IllegalArgumentException("Etat non autorisé");
        }
        reclamation.setEtatReclamation(nouvelEtat);
        return reclamationRepository.save(reclamation);
    }


    /**
     * Update a reclamation.
     *
     * @param reclamation the entity to save.
     * @return the persisted entity.
     */
    public Reclamation update(Reclamation reclamation) {
        LOG.debug("Request to update Reclamation : {}", reclamation);
        return reclamationRepository.save(reclamation);
    }

    /**
     * Partially update a reclamation.
     *
     * @param reclamation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reclamation> partialUpdate(Reclamation reclamation) {
        LOG.debug("Request to partially update Reclamation : {}", reclamation);

        return reclamationRepository
            .findById(reclamation.getId())
            .map(existingReclamation -> {
                if (reclamation.getNomReclamant() != null) {
                    existingReclamation.setNomReclamant(reclamation.getNomReclamant());
                }
                if (reclamation.getPrenomReclamant() != null) {
                    existingReclamation.setPrenomReclamant(reclamation.getPrenomReclamant());
                }
                if (reclamation.getNiveauEtude() != null) {
                    existingReclamation.setNiveauEtude(reclamation.getNiveauEtude());
                }
                if (reclamation.getObjetReclamation() != null) {
                    existingReclamation.setObjetReclamation(reclamation.getObjetReclamation());
                }
                if (reclamation.getMotifReclamation() != null) {
                    existingReclamation.setMotifReclamation(reclamation.getMotifReclamation());
                }
                if (reclamation.getJustificatif() != null) {
                    existingReclamation.setJustificatif(reclamation.getJustificatif());
                }
                if (reclamation.getEtatReclamation() != null) {
                    existingReclamation.setEtatReclamation(reclamation.getEtatReclamation());
                }

                return existingReclamation;
            })
            .map(reclamationRepository::save);
    }

    /**
     * Get all the reclamations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Reclamation> findAll(Pageable pageable, Long idUser) {
        Page<Reclamation> page = null;
        LOG.debug("Request to get all Reclamations");

        System.out.println("Les information reçu par le RestController getAllReclamations:" + "  pageable" + pageable+" idAccount "+ idUser);
        User user = idUser != null ? userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found by idAccount"+" "+idUser)) : null;
        if(user instanceof Etudiant){
            page = reclamationRepository.findAllByEtudiantId(pageable, idUser);
        } else if (user instanceof Directeur || user instanceof Professeur) {
            page = reclamationRepository.findAllAffectesAUnUtilisateur(pageable, idUser);
        } else {
            page = reclamationRepository.findAll(pageable);
        }
        return page;
    }

    /**
     * get all the reclamations By Directeur.
     */

    public Page<Reclamation> findAllByDirecteur(Pageable pageable) {
        Page<Reclamation> page = null;
        LOG.debug("Request to get all Reclamations");
        return reclamationRepository.findAll(pageable);
    }

    /**
     *
     * Get Reclamations Non Affectes A Un Utilisateur
     */

    public Page<Reclamation> getReclamationsNonAffectesAUnUtilisateur(Pageable pageable, Long idUser){
        Page<Reclamation> page = null;
        User user = idUser != null ? userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found!")) : null;
        if (user instanceof Directeur) {
            page = reclamationRepository.findNonAffectesAUnAutreUtilisateur(pageable, idUser);
        } else {
            page = reclamationRepository.findNonAffectesAUnUtilisateur(pageable);
        }
        return page;
    }

    /**
     * Get one reclamation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reclamation> findOne(Long id) {
        LOG.debug("Request to get Reclamation : {}", id);
        return reclamationRepository.findById(id);
    }

    /**
     * Delete the reclamation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reclamation : {}", id);
        reclamationRepository.deleteById(id);
    }

    private Reclamation maptoReclamation(ReclamationDTO reclamationDTO){
        Reclamation reclamation = new Reclamation();
        BeanUtils.copyProperties(reclamationDTO,reclamation);
        return reclamation;
    }

    private ReclamationDTO maptoReclamationDTO(Reclamation reclamation){
        ReclamationDTO reclamationDTO = new ReclamationDTO();
        BeanUtils.copyProperties(reclamation,reclamationDTO);
        return reclamationDTO;
    }
}
