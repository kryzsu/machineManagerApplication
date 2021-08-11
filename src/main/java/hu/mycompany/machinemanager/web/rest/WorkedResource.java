package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.domain.Worked;
import hu.mycompany.machinemanager.repository.WorkedRepository;
import hu.mycompany.machinemanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hu.mycompany.machinemanager.domain.Worked}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkedResource {

    private final Logger log = LoggerFactory.getLogger(WorkedResource.class);

    private static final String ENTITY_NAME = "worked";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkedRepository workedRepository;

    public WorkedResource(WorkedRepository workedRepository) {
        this.workedRepository = workedRepository;
    }

    /**
     * {@code POST  /workeds} : Create a new worked.
     *
     * @param worked the worked to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new worked, or with status {@code 400 (Bad Request)} if the worked has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workeds")
    public ResponseEntity<Worked> createWorked(@RequestBody Worked worked) throws URISyntaxException {
        log.debug("REST request to save Worked : {}", worked);
        if (worked.getId() != null) {
            throw new BadRequestAlertException("A new worked cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Worked result = workedRepository.save(worked);
        return ResponseEntity
            .created(new URI("/api/workeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workeds/:id} : Updates an existing worked.
     *
     * @param id the id of the worked to save.
     * @param worked the worked to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worked,
     * or with status {@code 400 (Bad Request)} if the worked is not valid,
     * or with status {@code 500 (Internal Server Error)} if the worked couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workeds/{id}")
    public ResponseEntity<Worked> updateWorked(@PathVariable(value = "id", required = false) final Long id, @RequestBody Worked worked)
        throws URISyntaxException {
        log.debug("REST request to update Worked : {}, {}", id, worked);
        if (worked.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worked.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Worked result = workedRepository.save(worked);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worked.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workeds/:id} : Partial updates given fields of an existing worked, field will ignore if it is null
     *
     * @param id the id of the worked to save.
     * @param worked the worked to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worked,
     * or with status {@code 400 (Bad Request)} if the worked is not valid,
     * or with status {@code 404 (Not Found)} if the worked is not found,
     * or with status {@code 500 (Internal Server Error)} if the worked couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workeds/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Worked> partialUpdateWorked(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Worked worked
    ) throws URISyntaxException {
        log.debug("REST request to partial update Worked partially : {}, {}", id, worked);
        if (worked.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worked.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Worked> result = workedRepository
            .findById(worked.getId())
            .map(
                existingWorked -> {
                    if (worked.getDay() != null) {
                        existingWorked.setDay(worked.getDay());
                    }
                    if (worked.getComment() != null) {
                        existingWorked.setComment(worked.getComment());
                    }

                    return existingWorked;
                }
            )
            .map(workedRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worked.getId().toString())
        );
    }

    /**
     * {@code GET  /workeds} : get all the workeds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workeds in body.
     */
    @GetMapping("/workeds")
    public List<Worked> getAllWorkeds() {
        log.debug("REST request to get all Workeds");
        return workedRepository.findAll();
    }

    /**
     * {@code GET  /workeds/:id} : get the "id" worked.
     *
     * @param id the id of the worked to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the worked, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workeds/{id}")
    public ResponseEntity<Worked> getWorked(@PathVariable Long id) {
        log.debug("REST request to get Worked : {}", id);
        Optional<Worked> worked = workedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(worked);
    }

    /**
     * {@code DELETE  /workeds/:id} : delete the "id" worked.
     *
     * @param id the id of the worked to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workeds/{id}")
    public ResponseEntity<Void> deleteWorked(@PathVariable Long id) {
        log.debug("REST request to delete Worked : {}", id);
        workedRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
