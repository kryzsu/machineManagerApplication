package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.service.ViewQueryService;
import hu.mycompany.machinemanager.service.ViewService;
import hu.mycompany.machinemanager.service.dto.ViewCriteria;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import hu.mycompany.machinemanager.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link hu.mycompany.machinemanager.domain.View}.
 */
@RestController
@RequestMapping("/api")
public class ViewResource {
    private final Logger log = LoggerFactory.getLogger(ViewResource.class);

    private static final String ENTITY_NAME = "view";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewService viewService;

    private final ViewQueryService viewQueryService;

    public ViewResource(ViewService viewService, ViewQueryService viewQueryService) {
        this.viewService = viewService;
        this.viewQueryService = viewQueryService;
    }

    /**
     * {@code POST  /views} : Create a new view.
     *
     * @param viewDTO the viewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewDTO, or with status {@code 400 (Bad Request)} if the view has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/views")
    public ResponseEntity<ViewDTO> createView(@RequestBody ViewDTO viewDTO) throws URISyntaxException {
        log.debug("REST request to save View : {}", viewDTO);
        if (viewDTO.getId() != null) {
            throw new BadRequestAlertException("A new view cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ViewDTO result = viewService.save(viewDTO);
        return ResponseEntity
            .created(new URI("/api/views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /views} : Updates an existing view.
     *
     * @param viewDTO the viewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewDTO,
     * or with status {@code 400 (Bad Request)} if the viewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/views")
    public ResponseEntity<ViewDTO> updateView(@RequestBody ViewDTO viewDTO) throws URISyntaxException {
        log.debug("REST request to update View : {}", viewDTO);
        if (viewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ViewDTO result = viewService.save(viewDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /views} : get all the views.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of views in body.
     */
    @GetMapping("/views")
    public ResponseEntity<List<ViewDTO>> getAllViews(ViewCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Views by criteria: {}", criteria);
        Page<ViewDTO> page = viewQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /views/count} : count all the views.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/views/count")
    public ResponseEntity<Long> countViews(ViewCriteria criteria) {
        log.debug("REST request to count Views by criteria: {}", criteria);
        return ResponseEntity.ok().body(viewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /views/:id} : get the "id" view.
     *
     * @param id the id of the viewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/views/{id}")
    public ResponseEntity<ViewDTO> getView(@PathVariable Long id) {
        log.debug("REST request to get View : {}", id);
        Optional<ViewDTO> viewDTO = viewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewDTO);
    }

    /**
     * {@code DELETE  /views/:id} : delete the "id" view.
     *
     * @param id the id of the viewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/views/{id}")
    public ResponseEntity<Void> deleteView(@PathVariable Long id) {
        log.debug("REST request to delete View : {}", id);
        viewService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
