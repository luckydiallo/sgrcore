package bf.ibam.sgrcore.web.rest;

import static bf.ibam.sgrcore.domain.FiliereAsserts.*;
import static bf.ibam.sgrcore.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bf.ibam.sgrcore.IntegrationTest;
import bf.ibam.sgrcore.domain.Filiere;
import bf.ibam.sgrcore.repository.FiliereRepository;
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
 * Integration tests for the {@link FiliereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiliereResourceIT {

    private static final String DEFAULT_NOM_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FILIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiliereMockMvc;

    private Filiere filiere;

    private Filiere insertedFiliere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createEntity() {
        return new Filiere().nomFiliere(DEFAULT_NOM_FILIERE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filiere createUpdatedEntity() {
        return new Filiere().nomFiliere(UPDATED_NOM_FILIERE);
    }

    @BeforeEach
    void initTest() {
        filiere = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFiliere != null) {
            filiereRepository.delete(insertedFiliere);
            insertedFiliere = null;
        }
    }

    @Test
    @Transactional
    void createFiliere() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Filiere
        var returnedFiliere = om.readValue(
            restFiliereMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiere)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Filiere.class
        );

        // Validate the Filiere in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFiliereUpdatableFieldsEquals(returnedFiliere, getPersistedFiliere(returnedFiliere));

        insertedFiliere = returnedFiliere;
    }

    @Test
    @Transactional
    void createFiliereWithExistingId() throws Exception {
        // Create the Filiere with an existing ID
        filiere.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiliereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiere)))
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilieres() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        // Get all the filiereList
        restFiliereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFiliere").value(hasItem(DEFAULT_NOM_FILIERE)));
    }

    @Test
    @Transactional
    void getFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        // Get the filiere
        restFiliereMockMvc
            .perform(get(ENTITY_API_URL_ID, filiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filiere.getId().intValue()))
            .andExpect(jsonPath("$.nomFiliere").value(DEFAULT_NOM_FILIERE));
    }

    @Test
    @Transactional
    void getNonExistingFiliere() throws Exception {
        // Get the filiere
        restFiliereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere
        Filiere updatedFiliere = filiereRepository.findById(filiere.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiliere are not directly saved in db
        em.detach(updatedFiliere);
        updatedFiliere.nomFiliere(UPDATED_NOM_FILIERE);

        restFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFiliere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFiliere))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFiliereToMatchAllProperties(updatedFiliere);
    }

    @Test
    @Transactional
    void putNonExistingFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(put(ENTITY_API_URL_ID, filiere.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiere)))
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filiere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiliereWithPatch() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere using partial update
        Filiere partialUpdatedFiliere = new Filiere();
        partialUpdatedFiliere.setId(filiere.getId());

        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliere.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiliere))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiliereUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFiliere, filiere), getPersistedFiliere(filiere));
    }

    @Test
    @Transactional
    void fullUpdateFiliereWithPatch() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filiere using partial update
        Filiere partialUpdatedFiliere = new Filiere();
        partialUpdatedFiliere.setId(filiere.getId());

        partialUpdatedFiliere.nomFiliere(UPDATED_NOM_FILIERE);

        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiliere.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFiliere))
            )
            .andExpect(status().isOk());

        // Validate the Filiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFiliereUpdatableFieldsEquals(partialUpdatedFiliere, getPersistedFiliere(partialUpdatedFiliere));
    }

    @Test
    @Transactional
    void patchNonExistingFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filiere.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(filiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiliere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filiere.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiliereMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filiere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiliere() throws Exception {
        // Initialize the database
        insertedFiliere = filiereRepository.saveAndFlush(filiere);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the filiere
        restFiliereMockMvc
            .perform(delete(ENTITY_API_URL_ID, filiere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return filiereRepository.count();
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

    protected Filiere getPersistedFiliere(Filiere filiere) {
        return filiereRepository.findById(filiere.getId()).orElseThrow();
    }

    protected void assertPersistedFiliereToMatchAllProperties(Filiere expectedFiliere) {
        assertFiliereAllPropertiesEquals(expectedFiliere, getPersistedFiliere(expectedFiliere));
    }

    protected void assertPersistedFiliereToMatchUpdatableProperties(Filiere expectedFiliere) {
        assertFiliereAllUpdatablePropertiesEquals(expectedFiliere, getPersistedFiliere(expectedFiliere));
    }
}
