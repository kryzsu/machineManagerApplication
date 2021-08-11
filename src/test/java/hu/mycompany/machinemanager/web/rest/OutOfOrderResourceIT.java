package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.MachineManagerApplicationApp;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.OutOfOrderQueryService;
import hu.mycompany.machinemanager.service.OutOfOrderService;
import hu.mycompany.machinemanager.service.dto.OutOfOrderCriteria;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OutOfOrderResource} REST controller.
 */
@SpringBootTest(classes = MachineManagerApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OutOfOrderResourceIT {
    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private OutOfOrderRepository outOfOrderRepository;

    @Autowired
    private OutOfOrderMapper outOfOrderMapper;

    @Autowired
    private OutOfOrderService outOfOrderService;

    @Autowired
    private OutOfOrderQueryService outOfOrderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutOfOrderMockMvc;

    private OutOfOrder outOfOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutOfOrder createEntity(EntityManager em) {
        OutOfOrder outOfOrder = new OutOfOrder().date(DEFAULT_DATE).description(DEFAULT_DESCRIPTION);
        return outOfOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutOfOrder createUpdatedEntity(EntityManager em) {
        OutOfOrder outOfOrder = new OutOfOrder().date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        return outOfOrder;
    }

    @BeforeEach
    public void initTest() {
        outOfOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutOfOrder() throws Exception {
        int databaseSizeBeforeCreate = outOfOrderRepository.findAll().size();
        // Create the OutOfOrder
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(outOfOrder);
        restOutOfOrderMockMvc
            .perform(
                post("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OutOfOrder in the database
        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeCreate + 1);
        OutOfOrder testOutOfOrder = outOfOrderList.get(outOfOrderList.size() - 1);
        assertThat(testOutOfOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOutOfOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createOutOfOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outOfOrderRepository.findAll().size();

        // Create the OutOfOrder with an existing ID
        outOfOrder.setId(1L);
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(outOfOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutOfOrderMockMvc
            .perform(
                post("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutOfOrder in the database
        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOfOrderRepository.findAll().size();
        // set the field null
        outOfOrder.setDate(null);

        // Create the OutOfOrder, which fails.
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(outOfOrder);

        restOutOfOrderMockMvc
            .perform(
                post("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOfOrderRepository.findAll().size();
        // set the field null
        outOfOrder.setDescription(null);

        // Create the OutOfOrder, which fails.
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(outOfOrder);

        restOutOfOrderMockMvc
            .perform(
                post("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOutOfOrders() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOfOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getOutOfOrder() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get the outOfOrder
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders/{id}", outOfOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outOfOrder.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getOutOfOrdersByIdFiltering() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        Long id = outOfOrder.getId();

        defaultOutOfOrderShouldBeFound("id.equals=" + id);
        defaultOutOfOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOutOfOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutOfOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOutOfOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutOfOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date equals to DEFAULT_DATE
        defaultOutOfOrderShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date equals to UPDATED_DATE
        defaultOutOfOrderShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date not equals to DEFAULT_DATE
        defaultOutOfOrderShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date not equals to UPDATED_DATE
        defaultOutOfOrderShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date in DEFAULT_DATE or UPDATED_DATE
        defaultOutOfOrderShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the outOfOrderList where date equals to UPDATED_DATE
        defaultOutOfOrderShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date is not null
        defaultOutOfOrderShouldBeFound("date.specified=true");

        // Get all the outOfOrderList where date is null
        defaultOutOfOrderShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date is greater than or equal to DEFAULT_DATE
        defaultOutOfOrderShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date is greater than or equal to UPDATED_DATE
        defaultOutOfOrderShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date is less than or equal to DEFAULT_DATE
        defaultOutOfOrderShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date is less than or equal to SMALLER_DATE
        defaultOutOfOrderShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date is less than DEFAULT_DATE
        defaultOutOfOrderShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date is less than UPDATED_DATE
        defaultOutOfOrderShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where date is greater than DEFAULT_DATE
        defaultOutOfOrderShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the outOfOrderList where date is greater than SMALLER_DATE
        defaultOutOfOrderShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description equals to DEFAULT_DESCRIPTION
        defaultOutOfOrderShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the outOfOrderList where description equals to UPDATED_DESCRIPTION
        defaultOutOfOrderShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description not equals to DEFAULT_DESCRIPTION
        defaultOutOfOrderShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the outOfOrderList where description not equals to UPDATED_DESCRIPTION
        defaultOutOfOrderShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOutOfOrderShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the outOfOrderList where description equals to UPDATED_DESCRIPTION
        defaultOutOfOrderShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description is not null
        defaultOutOfOrderShouldBeFound("description.specified=true");

        // Get all the outOfOrderList where description is null
        defaultOutOfOrderShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description contains DEFAULT_DESCRIPTION
        defaultOutOfOrderShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the outOfOrderList where description contains UPDATED_DESCRIPTION
        defaultOutOfOrderShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOutOfOrdersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        // Get all the outOfOrderList where description does not contain DEFAULT_DESCRIPTION
        defaultOutOfOrderShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the outOfOrderList where description does not contain UPDATED_DESCRIPTION
        defaultOutOfOrderShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutOfOrderShouldBeFound(String filter) throws Exception {
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOfOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutOfOrderShouldNotBeFound(String filter) throws Exception {
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutOfOrderMockMvc
            .perform(get("/api/out-of-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOutOfOrder() throws Exception {
        // Get the outOfOrder
        restOutOfOrderMockMvc.perform(get("/api/out-of-orders/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutOfOrder() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        int databaseSizeBeforeUpdate = outOfOrderRepository.findAll().size();

        // Update the outOfOrder
        OutOfOrder updatedOutOfOrder = outOfOrderRepository.findById(outOfOrder.getId()).get();
        // Disconnect from session so that the updates on updatedOutOfOrder are not directly saved in db
        em.detach(updatedOutOfOrder);
        updatedOutOfOrder.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(updatedOutOfOrder);

        restOutOfOrderMockMvc
            .perform(
                put("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutOfOrder in the database
        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeUpdate);
        OutOfOrder testOutOfOrder = outOfOrderList.get(outOfOrderList.size() - 1);
        assertThat(testOutOfOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOutOfOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingOutOfOrder() throws Exception {
        int databaseSizeBeforeUpdate = outOfOrderRepository.findAll().size();

        // Create the OutOfOrder
        OutOfOrderDTO outOfOrderDTO = outOfOrderMapper.toDto(outOfOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutOfOrderMockMvc
            .perform(
                put("/api/out-of-orders").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outOfOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutOfOrder in the database
        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOutOfOrder() throws Exception {
        // Initialize the database
        outOfOrderRepository.saveAndFlush(outOfOrder);

        int databaseSizeBeforeDelete = outOfOrderRepository.findAll().size();

        // Delete the outOfOrder
        restOutOfOrderMockMvc
            .perform(delete("/api/out-of-orders/{id}", outOfOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutOfOrder> outOfOrderList = outOfOrderRepository.findAll();
        assertThat(outOfOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
