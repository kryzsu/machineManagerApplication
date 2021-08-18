package hu.mycompany.machinemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.mycompany.machinemanager.IntegrationTest;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.domain.View;
import hu.mycompany.machinemanager.repository.ViewRepository;
import hu.mycompany.machinemanager.service.ViewService;
import hu.mycompany.machinemanager.service.criteria.ViewCriteria;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import hu.mycompany.machinemanager.service.mapper.ViewMapper;
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
 * Integration tests for the {@link ViewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ViewResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/views";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ViewRepository viewRepository;

    @Mock
    private ViewRepository viewRepositoryMock;

    @Autowired
    private ViewMapper viewMapper;

    @Mock
    private ViewService viewServiceMock;

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
    void createView() throws Exception {
        int databaseSizeBeforeCreate = viewRepository.findAll().size();
        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);
        restViewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isCreated());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate + 1);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createViewWithExistingId() throws Exception {
        // Create the View with an existing ID
        view.setId(1L);
        ViewDTO viewDTO = viewMapper.toDto(view);

        int databaseSizeBeforeCreate = viewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllViews() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList
        restViewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(viewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(viewServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viewServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get the view
        restViewMockMvc
            .perform(get(ENTITY_API_URL_ID, view.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(view.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getViewsByIdFiltering() throws Exception {
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
    void getAllViewsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name equals to DEFAULT_NAME
        defaultViewShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the viewList where name equals to UPDATED_NAME
        defaultViewShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllViewsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name not equals to DEFAULT_NAME
        defaultViewShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the viewList where name not equals to UPDATED_NAME
        defaultViewShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllViewsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name in DEFAULT_NAME or UPDATED_NAME
        defaultViewShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the viewList where name equals to UPDATED_NAME
        defaultViewShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllViewsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name is not null
        defaultViewShouldBeFound("name.specified=true");

        // Get all the viewList where name is null
        defaultViewShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllViewsByNameContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name contains DEFAULT_NAME
        defaultViewShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the viewList where name contains UPDATED_NAME
        defaultViewShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllViewsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        // Get all the viewList where name does not contain DEFAULT_NAME
        defaultViewShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the viewList where name does not contain UPDATED_NAME
        defaultViewShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllViewsByMachineIsEqualToSomething() throws Exception {
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

        // Get all the viewList where machine equals to (machineId + 1)
        defaultViewShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultViewShouldBeFound(String filter) throws Exception {
        restViewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(view.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restViewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultViewShouldNotBeFound(String filter) throws Exception {
        restViewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restViewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingView() throws Exception {
        // Get the view
        restViewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewView() throws Exception {
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
            .perform(
                put(ENTITY_API_URL_ID, viewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewDTO))
            )
            .andExpect(status().isOk());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewWithPatch() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Update the view using partial update
        View partialUpdatedView = new View();
        partialUpdatedView.setId(view.getId());

        restViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedView.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedView))
            )
            .andExpect(status().isOk());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateViewWithPatch() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeUpdate = viewRepository.findAll().size();

        // Update the view using partial update
        View partialUpdatedView = new View();
        partialUpdatedView.setId(view.getId());

        partialUpdatedView.name(UPDATED_NAME);

        restViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedView.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedView))
            )
            .andExpect(status().isOk());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
        View testView = viewList.get(viewList.size() - 1);
        assertThat(testView.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamView() throws Exception {
        int databaseSizeBeforeUpdate = viewRepository.findAll().size();
        view.setId(count.incrementAndGet());

        // Create the View
        ViewDTO viewDTO = viewMapper.toDto(view);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(viewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the View in the database
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteView() throws Exception {
        // Initialize the database
        viewRepository.saveAndFlush(view);

        int databaseSizeBeforeDelete = viewRepository.findAll().size();

        // Delete the view
        restViewMockMvc
            .perform(delete(ENTITY_API_URL_ID, view.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<View> viewList = viewRepository.findAll();
        assertThat(viewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
