package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.service.JobService;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import hu.mycompany.machinemanager.service.mapper.JobMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobResourceIT {

    private static final Integer DEFAULT_ESTIMATION = 1;
    private static final Integer UPDATED_ESTIMATION = 2;

    private static final Integer DEFAULT_PRODUCT_COUNT = 1;
    private static final Integer UPDATED_PRODUCT_COUNT = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_FACT = 1;
    private static final Integer UPDATED_FACT = 2;

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DRAWING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DRAWING_NUMBER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DRAWING = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DRAWING = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DRAWING_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DRAWING_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_WORKNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_WORKNUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobRepository jobRepository;

    @Mock
    private JobRepository jobRepositoryMock;

    @Autowired
    private JobMapper jobMapper;

    @Mock
    private JobService jobServiceMock;

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
            .estimation(DEFAULT_ESTIMATION)
            .productCount(DEFAULT_PRODUCT_COUNT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fact(DEFAULT_FACT)
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .drawingNumber(DEFAULT_DRAWING_NUMBER)
            .drawing(DEFAULT_DRAWING)
            .drawingContentType(DEFAULT_DRAWING_CONTENT_TYPE)
            .worknumber(DEFAULT_WORKNUMBER);
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
            .estimation(UPDATED_ESTIMATION)
            .productCount(UPDATED_PRODUCT_COUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fact(UPDATED_FACT)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .drawingNumber(UPDATED_DRAWING_NUMBER)
            .drawing(UPDATED_DRAWING)
            .drawingContentType(UPDATED_DRAWING_CONTENT_TYPE)
            .worknumber(UPDATED_WORKNUMBER);
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
        JobDTO jobDTO = jobMapper.toDto(job);
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getEstimation()).isEqualTo(DEFAULT_ESTIMATION);
        assertThat(testJob.getProductCount()).isEqualTo(DEFAULT_PRODUCT_COUNT);
        assertThat(testJob.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJob.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJob.getFact()).isEqualTo(DEFAULT_FACT);
        assertThat(testJob.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testJob.getDrawingNumber()).isEqualTo(DEFAULT_DRAWING_NUMBER);
        assertThat(testJob.getDrawing()).isEqualTo(DEFAULT_DRAWING);
        assertThat(testJob.getDrawingContentType()).isEqualTo(DEFAULT_DRAWING_CONTENT_TYPE);
        assertThat(testJob.getWorknumber()).isEqualTo(DEFAULT_WORKNUMBER);
    }

    @Test
    @Transactional
    void createJobWithExistingId() throws Exception {
        // Create the Job with an existing ID
        job.setId(1L);
        JobDTO jobDTO = jobMapper.toDto(job);

        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setProductCount(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.toDto(job);

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorknumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setWorknumber(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.toDto(job);

        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
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
            .andExpect(jsonPath("$.[*].estimation").value(hasItem(DEFAULT_ESTIMATION)))
            .andExpect(jsonPath("$.[*].productCount").value(hasItem(DEFAULT_PRODUCT_COUNT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fact").value(hasItem(DEFAULT_FACT)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].drawingNumber").value(hasItem(DEFAULT_DRAWING_NUMBER)))
            .andExpect(jsonPath("$.[*].drawingContentType").value(hasItem(DEFAULT_DRAWING_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].drawing").value(hasItem(Base64Utils.encodeToString(DEFAULT_DRAWING))))
            .andExpect(jsonPath("$.[*].worknumber").value(hasItem(DEFAULT_WORKNUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.estimation").value(DEFAULT_ESTIMATION))
            .andExpect(jsonPath("$.productCount").value(DEFAULT_PRODUCT_COUNT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fact").value(DEFAULT_FACT))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.drawingNumber").value(DEFAULT_DRAWING_NUMBER))
            .andExpect(jsonPath("$.drawingContentType").value(DEFAULT_DRAWING_CONTENT_TYPE))
            .andExpect(jsonPath("$.drawing").value(Base64Utils.encodeToString(DEFAULT_DRAWING)))
            .andExpect(jsonPath("$.worknumber").value(DEFAULT_WORKNUMBER));
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
            .estimation(UPDATED_ESTIMATION)
            .productCount(UPDATED_PRODUCT_COUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fact(UPDATED_FACT)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .drawingNumber(UPDATED_DRAWING_NUMBER)
            .drawing(UPDATED_DRAWING)
            .drawingContentType(UPDATED_DRAWING_CONTENT_TYPE)
            .worknumber(UPDATED_WORKNUMBER);
        JobDTO jobDTO = jobMapper.toDto(updatedJob);

        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getEstimation()).isEqualTo(UPDATED_ESTIMATION);
        assertThat(testJob.getProductCount()).isEqualTo(UPDATED_PRODUCT_COUNT);
        assertThat(testJob.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJob.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testJob.getFact()).isEqualTo(UPDATED_FACT);
        assertThat(testJob.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testJob.getDrawingNumber()).isEqualTo(UPDATED_DRAWING_NUMBER);
        assertThat(testJob.getDrawing()).isEqualTo(UPDATED_DRAWING);
        assertThat(testJob.getDrawingContentType()).isEqualTo(UPDATED_DRAWING_CONTENT_TYPE);
        assertThat(testJob.getWorknumber()).isEqualTo(UPDATED_WORKNUMBER);
    }

    @Test
    @Transactional
    void putNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
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

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
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

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
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
            .productCount(UPDATED_PRODUCT_COUNT)
            .fact(UPDATED_FACT)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .drawingNumber(UPDATED_DRAWING_NUMBER);

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
        assertThat(testJob.getEstimation()).isEqualTo(DEFAULT_ESTIMATION);
        assertThat(testJob.getProductCount()).isEqualTo(UPDATED_PRODUCT_COUNT);
        assertThat(testJob.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJob.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testJob.getFact()).isEqualTo(UPDATED_FACT);
        assertThat(testJob.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testJob.getDrawingNumber()).isEqualTo(UPDATED_DRAWING_NUMBER);
        assertThat(testJob.getDrawing()).isEqualTo(DEFAULT_DRAWING);
        assertThat(testJob.getDrawingContentType()).isEqualTo(DEFAULT_DRAWING_CONTENT_TYPE);
        assertThat(testJob.getWorknumber()).isEqualTo(DEFAULT_WORKNUMBER);
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
            .estimation(UPDATED_ESTIMATION)
            .productCount(UPDATED_PRODUCT_COUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fact(UPDATED_FACT)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .drawingNumber(UPDATED_DRAWING_NUMBER)
            .drawing(UPDATED_DRAWING)
            .drawingContentType(UPDATED_DRAWING_CONTENT_TYPE)
            .worknumber(UPDATED_WORKNUMBER);

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
        assertThat(testJob.getEstimation()).isEqualTo(UPDATED_ESTIMATION);
        assertThat(testJob.getProductCount()).isEqualTo(UPDATED_PRODUCT_COUNT);
        assertThat(testJob.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJob.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testJob.getFact()).isEqualTo(UPDATED_FACT);
        assertThat(testJob.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testJob.getDrawingNumber()).isEqualTo(UPDATED_DRAWING_NUMBER);
        assertThat(testJob.getDrawing()).isEqualTo(UPDATED_DRAWING);
        assertThat(testJob.getDrawingContentType()).isEqualTo(UPDATED_DRAWING_CONTENT_TYPE);
        assertThat(testJob.getWorknumber()).isEqualTo(UPDATED_WORKNUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
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

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
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

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobDTO)))
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
