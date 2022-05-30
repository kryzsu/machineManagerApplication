package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.repository.RawmaterialRepository;
import hu.mycompany.machinemanager.service.RawmaterialService;
import hu.mycompany.machinemanager.service.dto.RawmaterialDTO;
import hu.mycompany.machinemanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link hu.mycompany.machinemanager.domain.Rawmaterial}.
 */
@RestController
@RequestMapping("/api")
public class RawmaterialResource {

    private final Logger log = LoggerFactory.getLogger(RawmaterialResource.class);

    private static final String ENTITY_NAME = "rawmaterial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RawmaterialService rawmaterialService;

    private final RawmaterialRepository rawmaterialRepository;

    public RawmaterialResource(RawmaterialService rawmaterialService, RawmaterialRepository rawmaterialRepository) {
        this.rawmaterialService = rawmaterialService;
        this.rawmaterialRepository = rawmaterialRepository;
    }

    /**
     * {@code POST  /rawmaterials} : Create a new rawmaterial.
     *
     * @param rawmaterialDTO the rawmaterialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rawmaterialDTO, or with status {@code 400 (Bad Request)} if the rawmaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rawmaterials")
    public ResponseEntity<RawmaterialDTO> createRawmaterial(@Valid @RequestBody RawmaterialDTO rawmaterialDTO) throws URISyntaxException {
        log.debug("REST request to save Rawmaterial : {}", rawmaterialDTO);
        if (rawmaterialDTO.getId() != null) {
            throw new BadRequestAlertException("A new rawmaterial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RawmaterialDTO result = rawmaterialService.save(rawmaterialDTO);
        return ResponseEntity
            .created(new URI("/api/rawmaterials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rawmaterials/:id} : Updates an existing rawmaterial.
     *
     * @param id the id of the rawmaterialDTO to save.
     * @param rawmaterialDTO the rawmaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rawmaterialDTO,
     * or with status {@code 400 (Bad Request)} if the rawmaterialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rawmaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rawmaterials/{id}")
    public ResponseEntity<RawmaterialDTO> updateRawmaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RawmaterialDTO rawmaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Rawmaterial : {}, {}", id, rawmaterialDTO);
        if (rawmaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rawmaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rawmaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RawmaterialDTO result = rawmaterialService.save(rawmaterialDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rawmaterialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rawmaterials/:id} : Partial updates given fields of an existing rawmaterial, field will ignore if it is null
     *
     * @param id the id of the rawmaterialDTO to save.
     * @param rawmaterialDTO the rawmaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rawmaterialDTO,
     * or with status {@code 400 (Bad Request)} if the rawmaterialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rawmaterialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rawmaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rawmaterials/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RawmaterialDTO> partialUpdateRawmaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RawmaterialDTO rawmaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rawmaterial partially : {}, {}", id, rawmaterialDTO);
        if (rawmaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rawmaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rawmaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RawmaterialDTO> result = rawmaterialService.partialUpdate(rawmaterialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rawmaterialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rawmaterials} : get all the rawmaterials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rawmaterials in body.
     */
    @GetMapping("/rawmaterials")
    public ResponseEntity<List<RawmaterialDTO>> getAllRawmaterials(Pageable pageable) {
        log.debug("REST request to get a page of Rawmaterials");
        Page<RawmaterialDTO> page = rawmaterialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rawmaterials/:id} : get the "id" rawmaterial.
     *
     * @param id the id of the rawmaterialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rawmaterialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rawmaterials/{id}")
    public ResponseEntity<RawmaterialDTO> getRawmaterial(@PathVariable Long id) {
        log.debug("REST request to get Rawmaterial : {}", id);
        Optional<RawmaterialDTO> rawmaterialDTO = rawmaterialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rawmaterialDTO);
    }

    /**
     * {@code DELETE  /rawmaterials/:id} : delete the "id" rawmaterial.
     *
     * @param id the id of the rawmaterialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rawmaterials/{id}")
    public ResponseEntity<Void> deleteRawmaterial(@PathVariable Long id) {
        log.debug("REST request to delete Rawmaterial : {}", id);
        rawmaterialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
