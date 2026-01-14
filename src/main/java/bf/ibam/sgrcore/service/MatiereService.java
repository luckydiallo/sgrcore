package bf.ibam.sgrcore.service;

import bf.ibam.sgrcore.domain.Matiere;
import bf.ibam.sgrcore.repository.MatiereRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bf.ibam.sgrcore.domain.Matiere}.
 */
@Service
@Transactional
public class MatiereService {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereService.class);

    private final MatiereRepository matiereRepository;

    public MatiereService(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    /**
     * Save a matiere.
     *
     * @param matiere the entity to save.
     * @return the persisted entity.
     */
    public Matiere save(Matiere matiere) {
        LOG.debug("Request to save Matiere : {}", matiere);
        return matiereRepository.save(matiere);
    }

    /**
     * Update a matiere.
     *
     * @param matiere the entity to save.
     * @return the persisted entity.
     */
    public Matiere update(Matiere matiere) {
        LOG.debug("Request to update Matiere : {}", matiere);
        return matiereRepository.save(matiere);
    }

    /**
     * Partially update a matiere.
     *
     * @param matiere the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Matiere> partialUpdate(Matiere matiere) {
        LOG.debug("Request to partially update Matiere : {}", matiere);

        return matiereRepository
            .findById(matiere.getId())
            .map(existingMatiere -> {
                if (matiere.getNomMatiere() != null) {
                    existingMatiere.setNomMatiere(matiere.getNomMatiere());
                }

                return existingMatiere;
            })
            .map(matiereRepository::save);
    }

    /**
     * Get all the matieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Matiere> findAll(Pageable pageable) {
        LOG.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable);
    }

    /**
     * Get one matiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Matiere> findOne(Long id) {
        LOG.debug("Request to get Matiere : {}", id);
        return matiereRepository.findById(id);
    }

    /**
     * Delete the matiere by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
