package bf.ibam.sgrcore.web.rest;

import bf.ibam.sgrcore.domain.DTO.ReclamationDTO;
import bf.ibam.sgrcore.domain.DTO.UserReclamationDto;
import bf.ibam.sgrcore.domain.EtatReclamation;
import bf.ibam.sgrcore.domain.Reclamation;
import bf.ibam.sgrcore.repository.ReclamationRepository;
import bf.ibam.sgrcore.service.ReclamationService;
import bf.ibam.sgrcore.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bf.ibam.sgrcore.domain.Reclamation}.
 */
@RestController
@RequestMapping("/api/reclamations")
public class ReclamationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReclamationResource.class);

    private static final String ENTITY_NAME = "sgrcoreReclamation";

//    @Value("${jhipster.clientApp.name}")
    private String applicationName = "reclamation";

    private final ReclamationService reclamationService;

    private final ReclamationRepository reclamationRepository;

    public ReclamationResource(ReclamationService reclamationService, ReclamationRepository reclamationRepository) {
        this.reclamationService = reclamationService;
        this.reclamationRepository = reclamationRepository;
    }

    /**
     * Affectation de reclamation à l'utilisateur.
     */

    @PostMapping("/affecter-reclamations-user")
    public ResponseEntity<UserReclamationDto> affecterReclamationsAUser(@RequestBody UserReclamationDto userReclamationDto, @RequestParam Long idAccount) throws URISyntaxException {
        LOG.debug("REST request to affect reclamations : {}", userReclamationDto);
        reclamationService.affecterReclamationsAUser(userReclamationDto, idAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "Opération exécutée avec succès", ""))
            .body(userReclamationDto);
    }

    /**
     * {@code POST  /reclamations} : Create a new reclamation.
     *
     * @param reclamationDTO the reclamation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reclamation, or with status {@code 400 (Bad Request)} if the reclamation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReclamationDTO> createReclamation(@Valid @RequestBody ReclamationDTO reclamationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Reclamation : {}", reclamationDTO);
        if (reclamationDTO.getId() != null) {
            throw new BadRequestAlertException("A new reclamation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reclamationDTO = reclamationService.save(reclamationDTO);
        return ResponseEntity.created(new URI("/api/reclamations/" + reclamationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, reclamationDTO.getId().toString()))
            .body(reclamationDTO);
    }

    @PutMapping("/{id}/etat")
    public ResponseEntity<Reclamation> changerEtat(
        @PathVariable Long id,
        @RequestParam EtatReclamation etat) {

        LOG.debug("REST request pour changer l'état de la réclamation {} vers {}", id, etat);

        Reclamation result = reclamationService.changerEtat(id, etat);
        return ResponseEntity.ok(result);
    }


    /**
     * {@code PUT  /reclamations/:id} : Updates an existing reclamation.
     *
     * @param id the id of the reclamation to save.
     * @param reclamation the reclamation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamation,
     * or with status {@code 400 (Bad Request)} if the reclamation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reclamation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Reclamation reclamation
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reclamation : {}, {}", id, reclamation);
        if (reclamation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reclamation = reclamationService.update(reclamation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reclamation.getId().toString()))
            .body(reclamation);
    }

    /**
     * {@code PATCH  /reclamations/:id} : Partial updates given fields of an existing reclamation, field will ignore if it is null
     *
     * @param id the id of the reclamation to save.
     * @param reclamation the reclamation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reclamation,
     * or with status {@code 400 (Bad Request)} if the reclamation is not valid,
     * or with status {@code 404 (Not Found)} if the reclamation is not found,
     * or with status {@code 500 (Internal Server Error)} if the reclamation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reclamation> partialUpdateReclamation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Reclamation reclamation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reclamation partially : {}, {}", id, reclamation);
        if (reclamation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reclamation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reclamationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reclamation> result = reclamationService.partialUpdate(reclamation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reclamation.getId().toString())
        );
    }

    /**
     * {@code GET  /reclamations} : get all the reclamations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reclamations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Reclamation>> getAllReclamations(@ParameterObject Pageable pageable, @RequestParam Long idAccount) {
        LOG.debug("REST request to get a page of Reclamations");
        LOG.debug("REST request to get reclamations for account {}", idAccount);
        Page<Reclamation> page = reclamationService.findAll(pageable, idAccount);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     *  Download Justificatif
     */

    @GetMapping("/{idReclamation}/justificatif")
    public ResponseEntity<byte[]> downloadJustificatif(@PathVariable Long id) {
        return reclamationService
            .findOne(id)
            .filter(r -> r.getJustificatif() != null)
            .map(r ->
                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(r.getJustificatifContentType()))
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"justificatif-" + id + "\""
                    )
                    .body(r.getJustificatif())
            )
            .orElse(ResponseEntity.notFound().build());
    }


    /**
     * {@code GET  /reclamations} : get all the reclamations By Directeur.
     */

    @GetMapping("/query-all-reclamations-directeur")
    public ResponseEntity<List<Reclamation>> findAllByDirecteur(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Reclamations");
        Page<Reclamation> page = reclamationService.findAllByDirecteur(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reclamations} : obtenir les reclamations Non Affectées A Un Utilisateur
     */

    @GetMapping("/non-affecte-a-un-user")
    public ResponseEntity<List<Reclamation>> getReclamationsNonAffectesAUnUser (@ParameterObject Pageable pageable, @RequestParam Long idAccount) {
        LOG.debug("REST request to get all reclamations non affectes à un utilisateur");
        Page<Reclamation> page = reclamationService.getReclamationsNonAffectesAUnUtilisateur(pageable, idAccount);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reclamations/:id} : get the "id" reclamation.
     *
     * @param id the id of the reclamation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reclamation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reclamation : {}", id);
        Optional<Reclamation> reclamation = reclamationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reclamation);
    }

    /**
     * {@code DELETE  /reclamations/:id} : delete the "id" reclamation.
     *
     * @param id the id of the reclamation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reclamation : {}", id);
        reclamationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
