package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Holiday;
import hu.mycompany.machinemanager.repository.HolidayRepository;
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
 * Integration tests for the {@link HolidayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HolidayResourceIT {

    private static final LocalDate DEFAULT_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createEntity(EntityManager em) {
        Holiday holiday = new Holiday().day(DEFAULT_DAY).comment(DEFAULT_COMMENT).deleted(DEFAULT_DELETED);
        return holiday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createUpdatedEntity(EntityManager em) {
        Holiday holiday = new Holiday().day(UPDATED_DAY).comment(UPDATED_COMMENT).deleted(UPDATED_DELETED);
        return holiday;
    }

    @BeforeEach
    public void initTest() {
        holiday = createEntity(em);
    }

    @Test
    @Transactional
    void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();
        // Create the Holiday
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testHoliday.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testHoliday.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createHolidayWithExistingId() throws Exception {
        // Create the Holiday with an existing ID
        holiday.setId(1L);

        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL_ID, holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        Holiday updatedHoliday = holidayRepository.findById(holiday.getId()).get();
        // Disconnect from session so that the updates on updatedHoliday are not directly saved in db
        em.detach(updatedHoliday);
        updatedHoliday.day(UPDATED_DAY).comment(UPDATED_COMMENT).deleted(UPDATED_DELETED);

        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHoliday.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testHoliday.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testHoliday.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holiday.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holiday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holiday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.day(UPDATED_DAY);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testHoliday.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testHoliday.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.day(UPDATED_DAY).comment(UPDATED_COMMENT).deleted(UPDATED_DELETED);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testHoliday.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testHoliday.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holiday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holiday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holiday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(holiday)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Delete the holiday
        restHolidayMockMvc
            .perform(delete(ENTITY_API_URL_ID, holiday.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
