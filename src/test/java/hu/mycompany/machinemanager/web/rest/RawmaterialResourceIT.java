package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Rawmaterial;
import hu.mycompany.machinemanager.repository.RawmaterialRepository;
import hu.mycompany.machinemanager.service.dto.RawmaterialDTO;
import hu.mycompany.machinemanager.service.mapper.RawmaterialMapper;
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
 * Integration tests for the {@link RawmaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RawmaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_GRADE = "AAAAAAAAAA";
    private static final String UPDATED_GRADE = "BBBBBBBBBB";

    private static final String DEFAULT_DIMENSION = "AAAAAAAAAA";
    private static final String UPDATED_DIMENSION = "BBBBBBBBBB";

    private static final String DEFAULT_COATING = "AAAAAAAAAA";
    private static final String UPDATED_COATING = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rawmaterials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RawmaterialRepository rawmaterialRepository;

    @Autowired
    private RawmaterialMapper rawmaterialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRawmaterialMockMvc;

    private Rawmaterial rawmaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rawmaterial createEntity(EntityManager em) {
        Rawmaterial rawmaterial = new Rawmaterial()
            .name(DEFAULT_NAME)
            .comment(DEFAULT_COMMENT)
            .grade(DEFAULT_GRADE)
            .dimension(DEFAULT_DIMENSION)
            .coating(DEFAULT_COATING)
            .supplier(DEFAULT_SUPPLIER);
        return rawmaterial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rawmaterial createUpdatedEntity(EntityManager em) {
        Rawmaterial rawmaterial = new Rawmaterial()
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .grade(UPDATED_GRADE)
            .dimension(UPDATED_DIMENSION)
            .coating(UPDATED_COATING)
            .supplier(UPDATED_SUPPLIER);
        return rawmaterial;
    }

    @BeforeEach
    public void initTest() {
        rawmaterial = createEntity(em);
    }

    @Test
    @Transactional
    void createRawmaterial() throws Exception {
        int databaseSizeBeforeCreate = rawmaterialRepository.findAll().size();
        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);
        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeCreate + 1);
        Rawmaterial testRawmaterial = rawmaterialList.get(rawmaterialList.size() - 1);
        assertThat(testRawmaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRawmaterial.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRawmaterial.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testRawmaterial.getDimension()).isEqualTo(DEFAULT_DIMENSION);
        assertThat(testRawmaterial.getCoating()).isEqualTo(DEFAULT_COATING);
        assertThat(testRawmaterial.getSupplier()).isEqualTo(DEFAULT_SUPPLIER);
    }

    @Test
    @Transactional
    void createRawmaterialWithExistingId() throws Exception {
        // Create the Rawmaterial with an existing ID
        rawmaterial.setId(1L);
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        int databaseSizeBeforeCreate = rawmaterialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawmaterialRepository.findAll().size();
        // set the field null
        rawmaterial.setName(null);

        // Create the Rawmaterial, which fails.
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawmaterialRepository.findAll().size();
        // set the field null
        rawmaterial.setGrade(null);

        // Create the Rawmaterial, which fails.
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDimensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawmaterialRepository.findAll().size();
        // set the field null
        rawmaterial.setDimension(null);

        // Create the Rawmaterial, which fails.
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawmaterialRepository.findAll().size();
        // set the field null
        rawmaterial.setCoating(null);

        // Create the Rawmaterial, which fails.
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSupplierIsRequired() throws Exception {
        int databaseSizeBeforeTest = rawmaterialRepository.findAll().size();
        // set the field null
        rawmaterial.setSupplier(null);

        // Create the Rawmaterial, which fails.
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        restRawmaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRawmaterials() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        // Get all the rawmaterialList
        restRawmaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rawmaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)))
            .andExpect(jsonPath("$.[*].dimension").value(hasItem(DEFAULT_DIMENSION)))
            .andExpect(jsonPath("$.[*].coating").value(hasItem(DEFAULT_COATING)))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)));
    }

    @Test
    @Transactional
    void getRawmaterial() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        // Get the rawmaterial
        restRawmaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, rawmaterial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rawmaterial.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE))
            .andExpect(jsonPath("$.dimension").value(DEFAULT_DIMENSION))
            .andExpect(jsonPath("$.coating").value(DEFAULT_COATING))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER));
    }

    @Test
    @Transactional
    void getNonExistingRawmaterial() throws Exception {
        // Get the rawmaterial
        restRawmaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRawmaterial() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();

        // Update the rawmaterial
        Rawmaterial updatedRawmaterial = rawmaterialRepository.findById(rawmaterial.getId()).get();
        // Disconnect from session so that the updates on updatedRawmaterial are not directly saved in db
        em.detach(updatedRawmaterial);
        updatedRawmaterial
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .grade(UPDATED_GRADE)
            .dimension(UPDATED_DIMENSION)
            .coating(UPDATED_COATING)
            .supplier(UPDATED_SUPPLIER);
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(updatedRawmaterial);

        restRawmaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rawmaterialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
        Rawmaterial testRawmaterial = rawmaterialList.get(rawmaterialList.size() - 1);
        assertThat(testRawmaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRawmaterial.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRawmaterial.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testRawmaterial.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testRawmaterial.getCoating()).isEqualTo(UPDATED_COATING);
        assertThat(testRawmaterial.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void putNonExistingRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rawmaterialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRawmaterialWithPatch() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();

        // Update the rawmaterial using partial update
        Rawmaterial partialUpdatedRawmaterial = new Rawmaterial();
        partialUpdatedRawmaterial.setId(rawmaterial.getId());

        partialUpdatedRawmaterial
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .grade(UPDATED_GRADE)
            .dimension(UPDATED_DIMENSION)
            .coating(UPDATED_COATING)
            .supplier(UPDATED_SUPPLIER);

        restRawmaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRawmaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRawmaterial))
            )
            .andExpect(status().isOk());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
        Rawmaterial testRawmaterial = rawmaterialList.get(rawmaterialList.size() - 1);
        assertThat(testRawmaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRawmaterial.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRawmaterial.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testRawmaterial.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testRawmaterial.getCoating()).isEqualTo(UPDATED_COATING);
        assertThat(testRawmaterial.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void fullUpdateRawmaterialWithPatch() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();

        // Update the rawmaterial using partial update
        Rawmaterial partialUpdatedRawmaterial = new Rawmaterial();
        partialUpdatedRawmaterial.setId(rawmaterial.getId());

        partialUpdatedRawmaterial
            .name(UPDATED_NAME)
            .comment(UPDATED_COMMENT)
            .grade(UPDATED_GRADE)
            .dimension(UPDATED_DIMENSION)
            .coating(UPDATED_COATING)
            .supplier(UPDATED_SUPPLIER);

        restRawmaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRawmaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRawmaterial))
            )
            .andExpect(status().isOk());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
        Rawmaterial testRawmaterial = rawmaterialList.get(rawmaterialList.size() - 1);
        assertThat(testRawmaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRawmaterial.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRawmaterial.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testRawmaterial.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testRawmaterial.getCoating()).isEqualTo(UPDATED_COATING);
        assertThat(testRawmaterial.getSupplier()).isEqualTo(UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void patchNonExistingRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rawmaterialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRawmaterial() throws Exception {
        int databaseSizeBeforeUpdate = rawmaterialRepository.findAll().size();
        rawmaterial.setId(count.incrementAndGet());

        // Create the Rawmaterial
        RawmaterialDTO rawmaterialDTO = rawmaterialMapper.toDto(rawmaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRawmaterialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rawmaterialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rawmaterial in the database
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRawmaterial() throws Exception {
        // Initialize the database
        rawmaterialRepository.saveAndFlush(rawmaterial);

        int databaseSizeBeforeDelete = rawmaterialRepository.findAll().size();

        // Delete the rawmaterial
        restRawmaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, rawmaterial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rawmaterial> rawmaterialList = rawmaterialRepository.findAll();
        assertThat(rawmaterialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
