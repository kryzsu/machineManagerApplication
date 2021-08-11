package hu.mycompany.machinemanager.web.rest;

import static hu.mycompany.machinemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link MachineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MachineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/machines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineMockMvc;

    private Machine machine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createEntity(EntityManager em) {
        Machine machine = new Machine()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createDateTime(DEFAULT_CREATE_DATE_TIME)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME)
            .deleted(DEFAULT_DELETED);
        return machine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createUpdatedEntity(EntityManager em) {
        Machine machine = new Machine()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED);
        return machine;
    }

    @BeforeEach
    public void initTest() {
        machine = createEntity(em);
    }

    @Test
    @Transactional
    void createMachine() throws Exception {
        int databaseSizeBeforeCreate = machineRepository.findAll().size();
        // Create the Machine
        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isCreated());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate + 1);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachine.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testMachine.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testMachine.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void createMachineWithExistingId() throws Exception {
        // Create the Machine with an existing ID
        machine.setId(1L);

        int databaseSizeBeforeCreate = machineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineRepository.findAll().size();
        // set the field null
        machine.setName(null);

        // Create the Machine, which fails.

        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isBadRequest());

        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = machineRepository.findAll().size();
        // set the field null
        machine.setDescription(null);

        // Create the Machine, which fails.

        restMachineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isBadRequest());

        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMachines() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createDateTime").value(hasItem(sameInstant(DEFAULT_CREATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE_TIME))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get the machine
        restMachineMockMvc
            .perform(get(ENTITY_API_URL_ID, machine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createDateTime").value(sameInstant(DEFAULT_CREATE_DATE_TIME)))
            .andExpect(jsonPath("$.updateDateTime").value(sameInstant(DEFAULT_UPDATE_DATE_TIME)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMachine() throws Exception {
        // Get the machine
        restMachineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine
        Machine updatedMachine = machineRepository.findById(machine.getId()).get();
        // Disconnect from session so that the updates on updatedMachine are not directly saved in db
        em.detach(updatedMachine);
        updatedMachine
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED);

        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMachine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testMachine.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testMachine.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine.description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testMachine.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
        assertThat(testMachine.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME)
            .deleted(UPDATED_DELETED);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testMachine.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
        assertThat(testMachine.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeDelete = machineRepository.findAll().size();

        // Delete the machine
        restMachineMockMvc
            .perform(delete(ENTITY_API_URL_ID, machine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
