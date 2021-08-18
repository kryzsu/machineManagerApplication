package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.MachineManagerApplicationApp;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.domain.View;
import hu.mycompany.machinemanager.repository.ViewRepository;
import hu.mycompany.machinemanager.service.ViewQueryService;
import hu.mycompany.machinemanager.service.ViewService;
import hu.mycompany.machinemanager.service.dto.ViewCriteria;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import hu.mycompany.machinemanager.service.mapper.ViewMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ViewResource} REST controller.
 */
@SpringBootTest(classes = MachineManagerApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ViewResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ViewRepository viewRepository;

    @Mock
    private ViewRepository viewRepositoryMock;

    @Autowired
    private ViewMapper viewMapper;

    @Mock
    private ViewService viewServiceMock;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ViewQueryService viewQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViewMockMvc;

    private View view;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static View createEntity(EntityManager em) {
        View view = new View().name(DEFAULT_NAME);
        return view;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static View createUpdatedEntity(EntityManager em) {
        View view = new View().name(UPDATED_NAME);
        return view;
    }

    @BeforeEach
    public void initTest() {
        view = createEntity(em);
    }

    @Test
    @Transactional
    public void createView() throws Exception {
        int databaseSizeBeforeCreate = viewRepository.findAll().size();
        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);
        restViewMockMvc
            .perform(post("/api/views").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isCreated());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate + 1);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createViewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = viewRepository.findAll().size();

        // Create the View with an existing ID
        view.setId(1L);
        ViewDTO viewDTO = viewMapper.toDto(view);

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewMockMvc
            .perform(post("/api/views").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllViews() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList
        restViewMockMvc
            .perform(get("/api/views?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllViewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(viewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewMockMvc.perform(get("/api/views?eagerload=true")).andExpect(status().isOk());

        verify(viewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllViewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(viewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewMockMvc.perform(get("/api/views?eagerload=true")).andExpect(status().isOk());

        verify(viewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get the view
        restViewMockMvc
            .perform(get("/api/views/{id}", view.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(view.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getViewsByIdFiltering() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        Long id = view.getId();

        defaultViewShouldBeFound("id.equals=" + id);
        defaultViewShouldNotBeFound("id.notEquals=" + id);

        defaultViewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultViewShouldNotBeFound("id.greaterThan=" + id);

        defaultViewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultViewShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllViewsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name equals to DEFAULT_NAME
        defaultViewShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the viewList where name equals to UPDATED_NAME
        defaultViewShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllViewsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name not equals to DEFAULT_NAME
        defaultViewShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the viewList where name not equals to UPDATED_NAME
        defaultViewShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllViewsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name in DEFAULT_NAME or UPDATED_NAME
        defaultViewShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the viewList where name equals to UPDATED_NAME
        defaultViewShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllViewsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name is not null
        defaultViewShouldBeFound("name.specified=true");

        // Get all the viewList where name is null
        defaultViewShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllViewsByNameContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name contains DEFAULT_NAME
        defaultViewShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the viewList where name contains UPDATED_NAME
        defaultViewShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllViewsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name does not contain DEFAULT_NAME
        defaultViewShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the viewList where name does not contain UPDATED_NAME
        defaultViewShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllViewsByMachineIsEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);
        Machine machine = MachineResourceIT.createEntity(em);
        em.persist(machine);
        em.flush();
        view.addMachine(machine);
        viewRepository.saveAndFlush(view);
        Long machineId = machine.getId();

        // Get all the viewList where machine equals to machineId
        defaultViewShouldBeFound("machineId.equals=" + machineId);

        // Get all the viewList where machine equals to machineId + 1
        defaultViewShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultViewShouldBeFound(String filter) throws Exception {
        restViewMockMvc
            .perform(get("/api/views?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restViewMockMvc
            .perform(get("/api/views/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultViewShouldNotBeFound(String filter) throws Exception {
        restViewMockMvc
            .perform(get("/api/views?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restViewMockMvc
            .perform(get("/api/views/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingView() throws Exception {
        // Get the view
        restViewMockMvc.perform(get("/api/views/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Update the view
        View updatedView = viewRepository.findById(view.getId()).get();
        // Disconnect from session so that the updates on updatedView are not directly saved in db
        em.detach(updatedView);
        updatedView.name(UPDATED_NAME);
        ViewDTO viewDTO = viewMapper.toDto(updatedView);

        restViewMockMvc
            .perform(put("/api/views").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isOk());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(put("/api/views").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeDelete = viewRepository.findAll().size();

        // Delete the view
        restViewMockMvc
            .perform(delete("/api/views/{id}", view.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
