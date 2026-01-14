package bf.ibam.sgrcore.web.rest;

import static bf.ibam.sgrcore.domain.ReclamationAsserts.*;
import static bf.ibam.sgrcore.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bf.ibam.sgrcore.IntegrationTest;
import bf.ibam.sgrcore.domain.EtatReclamation;
import bf.ibam.sgrcore.domain.Filiere;
import bf.ibam.sgrcore.domain.Matiere;
import bf.ibam.sgrcore.domain.Reclamation;
import bf.ibam.sgrcore.repository.ReclamationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReclamationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReclamationResourceIT {

    private static final String DEFAULT_NOM_RECLAMANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RECLAMANT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_RECLAMANT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_RECLAMANT = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU_ETUDE = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU_ETUDE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJET_RECLAMATION = "AAAAAAAAAA";
    private static final String UPDATED_OBJET_RECLAMATION = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIF_RECLAMATION = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF_RECLAMATION = "BBBBBBBBBB";

//    private static final byte[] DEFAULT_JUSTIFICATIF = "AAAAAAAAAA";
//    private static final String UPDATED_JUSTIFICATIF = "BBBBBBBBBB";

    private static final EtatReclamation DEFAULT_ETAT_RECLAMATION = EtatReclamation.EN_COURS;
    private static final EtatReclamation UPDATED_ETAT_RECLAMATION = EtatReclamation.EN_COURS;

    private static final String ENTITY_API_URL = "/api/reclamations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReclamationMockMvc;

    private Reclamation reclamation;

    private Reclamation insertedReclamation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createEntity(EntityManager em) {
        Reclamation reclamation = new Reclamation()
            .nomReclamant(DEFAULT_NOM_RECLAMANT)
            .prenomReclamant(DEFAULT_PRENOM_RECLAMANT)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .objetReclamation(DEFAULT_OBJET_RECLAMATION)
            .motifReclamation(DEFAULT_MOTIF_RECLAMATION)
//            .justificatif(DEFAULT_JUSTIFICATIF)
            .etatReclamation(DEFAULT_ETAT_RECLAMATION);
        // Add required entity
        Filiere filiere;
        if (TestUtil.findAll(em, Filiere.class).isEmpty()) {
            filiere = FiliereResourceIT.createEntity();
            em.persist(filiere);
            em.flush();
        } else {
            filiere = TestUtil.findAll(em, Filiere.class).get(0);
        }
        reclamation.setFiliere(filiere);
        // Add required entity
        Matiere matiere;
        if (TestUtil.findAll(em, Matiere.class).isEmpty()) {
            matiere = MatiereResourceIT.createEntity();
            em.persist(matiere);
            em.flush();
        } else {
            matiere = TestUtil.findAll(em, Matiere.class).get(0);
        }
        reclamation.setMatiere(matiere);
        return reclamation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reclamation createUpdatedEntity(EntityManager em) {
        Reclamation updatedReclamation = new Reclamation()
            .nomReclamant(UPDATED_NOM_RECLAMANT)
            .prenomReclamant(UPDATED_PRENOM_RECLAMANT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .objetReclamation(UPDATED_OBJET_RECLAMATION)
            .motifReclamation(UPDATED_MOTIF_RECLAMATION);
//            .justificatif(UPDATED_JUSTIFICATIF)
//            .etatReclamation(UPDATED_ETAT_RECLAMATION);
        // Add required entity
        Filiere filiere;
        if (TestUtil.findAll(em, Filiere.class).isEmpty()) {
            filiere = FiliereResourceIT.createUpdatedEntity();
            em.persist(filiere);
            em.flush();
        } else {
            filiere = TestUtil.findAll(em, Filiere.class).get(0);
        }
        updatedReclamation.setFiliere(filiere);
        // Add required entity
        Matiere matiere;
        if (TestUtil.findAll(em, Matiere.class).isEmpty()) {
            matiere = MatiereResourceIT.createUpdatedEntity();
            em.persist(matiere);
            em.flush();
        } else {
            matiere = TestUtil.findAll(em, Matiere.class).get(0);
        }
        updatedReclamation.setMatiere(matiere);
        return updatedReclamation;
    }

    @BeforeEach
    void initTest() {
        reclamation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReclamation != null) {
            reclamationRepository.delete(insertedReclamation);
            insertedReclamation = null;
        }
    }

    @Test
    @Transactional
    void createReclamation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reclamation
        var returnedReclamation = om.readValue(
            restReclamationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Reclamation.class
        );

        // Validate the Reclamation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReclamationUpdatableFieldsEquals(returnedReclamation, getPersistedReclamation(returnedReclamation));

        insertedReclamation = returnedReclamation;
    }

    @Test
    @Transactional
    void createReclamationWithExistingId() throws Exception {
        // Create the Reclamation with an existing ID
        reclamation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomReclamantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setNomReclamant(null);

        // Create the Reclamation, which fails.

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomReclamantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setPrenomReclamant(null);

        // Create the Reclamation, which fails.

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObjetReclamationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setObjetReclamation(null);

        // Create the Reclamation, which fails.

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotifReclamationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setMotifReclamation(null);

        // Create the Reclamation, which fails.

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatReclamationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reclamation.setEtatReclamation(null);

        // Create the Reclamation, which fails.

        restReclamationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReclamations() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get all the reclamationList
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomReclamant").value(hasItem(DEFAULT_NOM_RECLAMANT)))
            .andExpect(jsonPath("$.[*].prenomReclamant").value(hasItem(DEFAULT_PRENOM_RECLAMANT)))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE)))
            .andExpect(jsonPath("$.[*].objetReclamation").value(hasItem(DEFAULT_OBJET_RECLAMATION)))
            .andExpect(jsonPath("$.[*].motifReclamation").value(hasItem(DEFAULT_MOTIF_RECLAMATION)))
//            .andExpect(jsonPath("$.[*].justificatif").value(hasItem(DEFAULT_JUSTIFICATIF)))
            .andExpect(jsonPath("$.[*].etatReclamation").value(hasItem(DEFAULT_ETAT_RECLAMATION)));
    }

    @Test
    @Transactional
    void getReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        // Get the reclamation
        restReclamationMockMvc
            .perform(get(ENTITY_API_URL_ID, reclamation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reclamation.getId().intValue()))
            .andExpect(jsonPath("$.nomReclamant").value(DEFAULT_NOM_RECLAMANT))
            .andExpect(jsonPath("$.prenomReclamant").value(DEFAULT_PRENOM_RECLAMANT))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE))
            .andExpect(jsonPath("$.objetReclamation").value(DEFAULT_OBJET_RECLAMATION))
            .andExpect(jsonPath("$.motifReclamation").value(DEFAULT_MOTIF_RECLAMATION))
//            .andExpect(jsonPath("$.justificatif").value(DEFAULT_JUSTIFICATIF))
            .andExpect(jsonPath("$.etatReclamation").value(DEFAULT_ETAT_RECLAMATION));
    }

    @Test
    @Transactional
    void getNonExistingReclamation() throws Exception {
        // Get the reclamation
        restReclamationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation
        Reclamation updatedReclamation = reclamationRepository.findById(reclamation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReclamation are not directly saved in db
        em.detach(updatedReclamation);
        updatedReclamation
            .nomReclamant(UPDATED_NOM_RECLAMANT)
            .prenomReclamant(UPDATED_PRENOM_RECLAMANT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .objetReclamation(UPDATED_OBJET_RECLAMATION)
            .motifReclamation(UPDATED_MOTIF_RECLAMATION);
//            .justificatif(UPDATED_JUSTIFICATIF)
//            .etatReclamation(UPDATED_ETAT_RECLAMATION);

        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReclamationToMatchAllProperties(updatedReclamation);
    }

    @Test
    @Transactional
    void putNonExistingReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .nomReclamant(UPDATED_NOM_RECLAMANT)
            .prenomReclamant(UPDATED_PRENOM_RECLAMANT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .motifReclamation(UPDATED_MOTIF_RECLAMATION)
            .etatReclamation(UPDATED_ETAT_RECLAMATION);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReclamationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReclamation, reclamation),
            getPersistedReclamation(reclamation)
        );
    }

    @Test
    @Transactional
    void fullUpdateReclamationWithPatch() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reclamation using partial update
        Reclamation partialUpdatedReclamation = new Reclamation();
        partialUpdatedReclamation.setId(reclamation.getId());

        partialUpdatedReclamation
            .nomReclamant(UPDATED_NOM_RECLAMANT)
            .prenomReclamant(UPDATED_PRENOM_RECLAMANT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .objetReclamation(UPDATED_OBJET_RECLAMATION)
            .motifReclamation(UPDATED_MOTIF_RECLAMATION)
//            .justificatif(UPDATED_JUSTIFICATIF)
            .etatReclamation(UPDATED_ETAT_RECLAMATION);

        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReclamation))
            )
            .andExpect(status().isOk());

        // Validate the Reclamation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReclamationUpdatableFieldsEquals(partialUpdatedReclamation, getPersistedReclamation(partialUpdatedReclamation));
    }

    @Test
    @Transactional
    void patchNonExistingReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reclamation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reclamation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReclamation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reclamation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReclamationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reclamation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reclamation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReclamation() throws Exception {
        // Initialize the database
        insertedReclamation = reclamationRepository.saveAndFlush(reclamation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reclamation
        restReclamationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reclamation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reclamationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Reclamation getPersistedReclamation(Reclamation reclamation) {
        return reclamationRepository.findById(reclamation.getId()).orElseThrow();
    }

    protected void assertPersistedReclamationToMatchAllProperties(Reclamation expectedReclamation) {
        assertReclamationAllPropertiesEquals(expectedReclamation, getPersistedReclamation(expectedReclamation));
    }

    protected void assertPersistedReclamationToMatchUpdatableProperties(Reclamation expectedReclamation) {
        assertReclamationAllUpdatablePropertiesEquals(expectedReclamation, getPersistedReclamation(expectedReclamation));
    }
}
