package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Calendar;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.CalendarRepository;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import hu.mycompany.machinemanager.service.mapper.JobMapper;
import hu.mycompany.machinemanager.service.mapper.MachineMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComplexScenario {

    private static final LocalDateTime DEFAULT_CREATE_DATETIME = LocalDateTime.now();

    private static final String JOB_ENTITY_API_URL = "/api/jobs";

    private static final String MACHINE_ENTITY_API_URL = "/api/machines";

    private static final String OUT_ENTITY_API_URL = "/api/out-of-orders";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private MachineMapper machineMapper;

    @Autowired
    private MockMvc restJobMockMvc;

    @Autowired
    private CalendarRepository calendarRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createJobEntity(int estimation, String orderNumber, String worknumber, Machine machine) {
        Job job = new Job()
            .estimation(estimation)
            .productCount(1)
            .orderNumber(orderNumber)
            .createDateTime(DEFAULT_CREATE_DATETIME)
            .machine(machine)
            .worknumber(worknumber);
        return job;
    }

    public static Machine createMachineEntity(String name) {
        Machine machine = new Machine().name(name).description("Description");
        return machine;
    }

    @BeforeEach
    public void initTest() {}

    private void createWekEnds() {
        List<Calendar> calendarList = LocalDate
            .now()
            .datesUntil(LocalDate.now().plusYears(1))
            .map(date -> new Calendar(date))
            .collect(Collectors.toList());

        calendarRepository.saveAll(calendarList);
    }

    private void createMachineAndJob() throws Exception {
        // Create the Machine
        MachineDTO machine1 = machineMapper.toDto(createMachineEntity("machine1"));
        restJobMockMvc
            .perform(
                post(MACHINE_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine1))
            )
            .andExpect(status().isCreated());

        // Create the Machine
        MachineDTO machine2 = machineMapper.toDto(createMachineEntity("machine2"));
        restJobMockMvc
            .perform(
                post(MACHINE_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(machine1))
            )
            .andExpect(status().isCreated());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(createJobEntity(1, "on1", "wn1", machineMapper.toEntity(machine1)));
        restJobMockMvc
            .perform(post(JOB_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        jobDTO = jobMapper.toDto(createJobEntity(8, "on2", "wn2", machineMapper.toEntity(machine2)));
        restJobMockMvc
            .perform(post(JOB_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        jobDTO = jobMapper.toDto(createJobEntity(3, "on3", "wn3", machineMapper.toEntity(machine1)));
        restJobMockMvc
            .perform(post(JOB_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        jobDTO = jobMapper.toDto(createJobEntity(6, "on4", "wn4", machineMapper.toEntity(machine2)));
        restJobMockMvc
            .perform(post(JOB_ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void createJob() throws Exception {
        createWekEnds();
        createMachineAndJob();

        MvcResult mvcResult = restJobMockMvc
            .perform(get("/api/perspective/get-detailed-machine-list").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isNotNull();
        // Validate the Job in the database
        //        List<Job> jobList = jobRepository.findAll();

        //      List<Machine> machineList = machineRepository.findAll();
    }
}
