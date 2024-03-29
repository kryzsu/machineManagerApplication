package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.OutOfOrderQueryService;
import hu.mycompany.machinemanager.service.OutOfOrderService;
import hu.mycompany.machinemanager.service.criteria.OutOfOrderCriteria;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
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
 * REST controller for managing {@link hu.mycompany.machinemanager.domain.OutOfOrder}.
 */
@RestController
@RequestMapping("/api")
public class OutOfOrderResource {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OutOfOrderResource.class);

    private static final String ENTITY_NAME = "outOfOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutOfOrderService outOfOrderService;

    private final OutOfOrderRepository outOfOrderRepository;

    private final OutOfOrderQueryService outOfOrderQueryService;

    public OutOfOrderResource(
        OutOfOrderService outOfOrderService,
        OutOfOrderRepository outOfOrderRepository,
        OutOfOrderQueryService outOfOrderQueryService
    ) {
        this.outOfOrderService = outOfOrderService;
        this.outOfOrderRepository = outOfOrderRepository;
        this.outOfOrderQueryService = outOfOrderQueryService;
    }

    /**
     * {@code POST  /out-of-orders} : Create a new outOfOrder.
     *
     * @param outOfOrderDTO the outOfOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outOfOrderDTO, or with status {@code 400 (Bad Request)} if the outOfOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/out-of-orders")
    public ResponseEntity<OutOfOrderDTO> createOutOfOrder(@Valid @RequestBody OutOfOrderDTO outOfOrderDTO) throws URISyntaxException {
        log.debug("REST request to save OutOfOrder : {}", outOfOrderDTO);
        if (outOfOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new outOfOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutOfOrderDTO result = outOfOrderService.save(outOfOrderDTO);
        return ResponseEntity
            .created(new URI("/api/out-of-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /out-of-orders/:id} : Updates an existing outOfOrder.
     *
     * @param id the id of the outOfOrderDTO to save.
     * @param outOfOrderDTO the outOfOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outOfOrderDTO,
     * or with status {@code 400 (Bad Request)} if the outOfOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outOfOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/out-of-orders/{id}")
    public ResponseEntity<OutOfOrderDTO> updateOutOfOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OutOfOrderDTO outOfOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OutOfOrder : {}, {}", id, outOfOrderDTO);
        if (outOfOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outOfOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outOfOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutOfOrderDTO result = outOfOrderService.save(outOfOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, outOfOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /out-of-orders/:id} : Partial updates given fields of an existing outOfOrder, field will ignore if it is null
     *
     * @param id the id of the outOfOrderDTO to save.
     * @param outOfOrderDTO the outOfOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outOfOrderDTO,
     * or with status {@code 400 (Bad Request)} if the outOfOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outOfOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outOfOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/out-of-orders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OutOfOrderDTO> partialUpdateOutOfOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OutOfOrderDTO outOfOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutOfOrder partially : {}, {}", id, outOfOrderDTO);
        if (outOfOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outOfOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outOfOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutOfOrderDTO> result = outOfOrderService.partialUpdate(outOfOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, outOfOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /out-of-orders} : get all the outOfOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outOfOrders in body.
     */
    @GetMapping("/out-of-orders")
    public ResponseEntity<List<OutOfOrderDTO>> getAllOutOfOrders(OutOfOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OutOfOrders by criteria: {}", criteria);
        Page<OutOfOrderDTO> page = outOfOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /out-of-orders/count} : count all the outOfOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/out-of-orders/count")
    public ResponseEntity<Long> countOutOfOrders(OutOfOrderCriteria criteria) {
        log.debug("REST request to count OutOfOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(outOfOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /out-of-orders/:id} : get the "id" outOfOrder.
     *
     * @param id the id of the outOfOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outOfOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/out-of-orders/{id}")
    public ResponseEntity<OutOfOrderDTO> getOutOfOrder(@PathVariable Long id) {
        log.debug("REST request to get OutOfOrder : {}", id);
        Optional<OutOfOrderDTO> outOfOrderDTO = outOfOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outOfOrderDTO);
    }

    /**
     * {@code DELETE  /out-of-orders/:id} : delete the "id" outOfOrder.
     *
     * @param id the id of the outOfOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/out-of-orders/{id}")
    public ResponseEntity<Void> deleteOutOfOrder(@PathVariable Long id) {
        log.debug("REST request to delete OutOfOrder : {}", id);
        outOfOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
