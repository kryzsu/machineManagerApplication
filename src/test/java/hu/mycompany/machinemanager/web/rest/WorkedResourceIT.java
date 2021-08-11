package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Worked;
import hu.mycompany.machinemanager.repository.WorkedRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkedResourceIT {

    private static final LocalDate DEFAULT_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkedRepository workedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkedMockMvc;

    private Worked worked;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worked createEntity(EntityManager em) {
        Worked worked = new Worked().day(DEFAULT_DAY).comment(DEFAULT_COMMENT);
        return worked;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Worked createUpdatedEntity(EntityManager em) {
        Worked worked = new Worked().day(UPDATED_DAY).comment(UPDATED_COMMENT);
        return worked;
    }

    @BeforeEach
    public void initTest() {
        worked = createEntity(em);
    }

    @Test
    @Transactional
    void createWorked() throws Exception {
        int databaseSizeBeforeCreate = workedRepository.findAll().size();
        // Create the Worked
        restWorkedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(worked)))
            .andExpect(status().isCreated());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeCreate + 1);
        Worked testWorked = workedList.get(workedList.size() - 1);
        assertThat(testWorked.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testWorked.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createWorkedWithExistingId() throws Exception {
        // Create the Worked with an existing ID
        worked.setId(1L);

        int databaseSizeBeforeCreate = workedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(worked)))
            .andExpect(status().isBadRequest());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkeds() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        // Get all the workedList
        restWorkedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(worked.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getWorked() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        // Get the worked
        restWorkedMockMvc
            .perform(get(ENTITY_API_URL_ID, worked.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(worked.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingWorked() throws Exception {
        // Get the worked
        restWorkedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorked() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        int databaseSizeBeforeUpdate = workedRepository.findAll().size();

        // Update the worked
        Worked updatedWorked = workedRepository.findById(worked.getId()).get();
        // Disconnect from session so that the updates on updatedWorked are not directly saved in db
        em.detach(updatedWorked);
        updatedWorked.day(UPDATED_DAY).comment(UPDATED_COMMENT);

        restWorkedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorked.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorked))
            )
            .andExpect(status().isOk());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
        Worked testWorked = workedList.get(workedList.size() - 1);
        assertThat(testWorked.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testWorked.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, worked.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(worked))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(worked))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(worked)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkedWithPatch() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        int databaseSizeBeforeUpdate = workedRepository.findAll().size();

        // Update the worked using partial update
        Worked partialUpdatedWorked = new Worked();
        partialUpdatedWorked.setId(worked.getId());

        partialUpdatedWorked.day(UPDATED_DAY);

        restWorkedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorked.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorked))
            )
            .andExpect(status().isOk());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
        Worked testWorked = workedList.get(workedList.size() - 1);
        assertThat(testWorked.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testWorked.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateWorkedWithPatch() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        int databaseSizeBeforeUpdate = workedRepository.findAll().size();

        // Update the worked using partial update
        Worked partialUpdatedWorked = new Worked();
        partialUpdatedWorked.setId(worked.getId());

        partialUpdatedWorked.day(UPDATED_DAY).comment(UPDATED_COMMENT);

        restWorkedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorked.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorked))
            )
            .andExpect(status().isOk());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
        Worked testWorked = workedList.get(workedList.size() - 1);
        assertThat(testWorked.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testWorked.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, worked.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(worked))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(worked))
            )
            .andExpect(status().isBadRequest());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorked() throws Exception {
        int databaseSizeBeforeUpdate = workedRepository.findAll().size();
        worked.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(worked)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Worked in the database
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorked() throws Exception {
        // Initialize the database
        workedRepository.saveAndFlush(worked);

        int databaseSizeBeforeDelete = workedRepository.findAll().size();

        // Delete the worked
        restWorkedMockMvc
            .perform(delete(ENTITY_API_URL_ID, worked.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Worked> workedList = workedRepository.findAll();
        assertThat(workedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
