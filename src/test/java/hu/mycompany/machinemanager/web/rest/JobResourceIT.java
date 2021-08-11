package hu.mycompany.machinemanager.web.rest;

import static hu.mycompany.machinemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobResourceIT {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAYS = 1;
    private static final Integer UPDATED_DAYS = 2;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Boolean DEFAULT_IN_PROGRESS = false;
    private static final Boolean UPDATED_IN_PROGRESS = true;

    private static final Integer DEFAULT_DAYS_DONE = 1;
    private static final Integer UPDATED_DAYS_DONE = 2;

    private static final String ENTITY_API_URL = "/api/jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobRepository jobRepository;

    @Mock
    private JobRepository jobRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobMockMvc;

    private Job job;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .days(DEFAULT_DAYS)
            .productName(DEFAULT_PRODUCT_NAME)
            .count(DEFAULT_COUNT)
            .productType(DEFAULT_PRODUCT_TYPE)
            .comment(DEFAULT_COMMENT)
            .createDateTime(DEFAULT_CREATE_DATE_TIME)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME)
            .deleted(DEFAULT_DELETED)
            .inProgress(DEFAULT_IN_PROGRESS)
            .daysDone(DEFAULT_DAYS_DONE);
        return job;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity(EntityManager em) {
        Job job = new Job()
            .customerName(UPDATED_CUSTOMER_NAME)
            .days(UPDATED_DAYS)
            .productName(UPDATED_PRODUCT_NAME)
            .count(UPDATED_COUNT)
            .productType(UPDATED_PRODUCT_TYPE)
            .comment(UPDATED_COMMENT)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED)
            .inProgress(UPDATED_IN_PROGRESS)
            .daysDone(UPDATED_DAYS_DONE);
        return job;
    }

    @BeforeEach
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();
        // Create the Job
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testJob.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testJob.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testJob.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testJob.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
        assertThat(testJob.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testJob.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testJob.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testJob.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testJob.getInProgress()).isEqualTo(DEFAULT_IN_PROGRESS);
        assertThat(testJob.getDaysDone()).isEqualTo(DEFAULT_DAYS_DONE);
    }

    @Test
    @Transactional
    void createJobWithExistingId() throws Exception {
        // Create the Job with an existing ID
        job.setId(1L);

        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setCustomerName(null);

        // Create the Job, which fails.

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setDays(null);

        // Create the Job, which fails.

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setProductName(null);

        // Create the Job, which fails.

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setCount(null);

        // Create the Job, which fails.

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createDateTime").value(hasItem(sameInstant(DEFAULT_CREATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].inProgress").value(hasItem(DEFAULT_IN_PROGRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].daysDone").value(hasItem(DEFAULT_DAYS_DONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc
            .perform(get(ENTITY_API_URL_ID, job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.createDateTime").value(sameInstant(DEFAULT_CREATE_DATE_TIME)))
            .andExpect(jsonPath("$.updateDateTime").value(sameInstant(DEFAULT_UPDATE_DATE_TIME)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.inProgress").value(DEFAULT_IN_PROGRESS.booleanValue()))
            .andExpect(jsonPath("$.daysDone").value(DEFAULT_DAYS_DONE));
    }

    @Test
    @Transactional
    void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .customerName(UPDATED_CUSTOMER_NAME)
            .days(UPDATED_DAYS)
            .productName(UPDATED_PRODUCT_NAME)
            .count(UPDATED_COUNT)
            .productType(UPDATED_PRODUCT_TYPE)
            .comment(UPDATED_COMMENT)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED)
            .inProgress(UPDATED_IN_PROGRESS)
            .daysDone(UPDATED_DAYS_DONE);

        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJob.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testJob.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testJob.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testJob.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testJob.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testJob.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testJob.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testJob.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testJob.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testJob.getInProgress()).isEqualTo(UPDATED_IN_PROGRESS);
        assertThat(testJob.getDaysDone()).isEqualTo(UPDATED_DAYS_DONE);
    }

    @Test
    @Transactional
    void putNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, job.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobWithPatch() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob
            .days(UPDATED_DAYS)
            .productType(UPDATED_PRODUCT_TYPE)
            .comment(UPDATED_COMMENT)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .daysDone(UPDATED_DAYS_DONE);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testJob.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testJob.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testJob.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testJob.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testJob.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testJob.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testJob.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testJob.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testJob.getInProgress()).isEqualTo(DEFAULT_IN_PROGRESS);
        assertThat(testJob.getDaysDone()).isEqualTo(UPDATED_DAYS_DONE);
    }

    @Test
    @Transactional
    void fullUpdateJobWithPatch() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob
            .customerName(UPDATED_CUSTOMER_NAME)
            .days(UPDATED_DAYS)
            .productName(UPDATED_PRODUCT_NAME)
            .count(UPDATED_COUNT)
            .productType(UPDATED_PRODUCT_TYPE)
            .comment(UPDATED_COMMENT)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED)
            .inProgress(UPDATED_IN_PROGRESS)
            .daysDone(UPDATED_DAYS_DONE);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testJob.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testJob.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testJob.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testJob.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
        assertThat(testJob.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testJob.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testJob.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testJob.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testJob.getInProgress()).isEqualTo(UPDATED_IN_PROGRESS);
        assertThat(testJob.getDaysDone()).isEqualTo(UPDATED_DAYS_DONE);
    }

    @Test
    @Transactional
    void patchNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, job.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(job))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Delete the job
        restJobMockMvc.perform(delete(ENTITY_API_URL_ID, job.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
