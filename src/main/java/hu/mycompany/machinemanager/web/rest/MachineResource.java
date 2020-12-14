package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link hu.mycompany.machinemanager.domain.Machine}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MachineResource {

    private final Logger log = LoggerFactory.getLogger(MachineResource.class);

    private static final String ENTITY_NAME = "machine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MachineRepository machineRepository;

    public MachineResource(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    /**
     * {@code POST  /machines} : Create a new machine.
     *
     * @param machine the machine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machine, or with status {@code 400 (Bad Request)} if the machine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/machines")
    public ResponseEntity<Machine> createMachine(@Valid @RequestBody Machine machine) throws URISyntaxException {
        log.debug("REST request to save Machine : {}", machine);
        if (machine.getId() != null) {
            throw new BadRequestAlertException("A new machine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Machine result = machineRepository.save(machine);
        return ResponseEntity.created(new URI("/api/machines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /machines} : Updates an existing machine.
     *
     * @param machine the machine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machine,
     * or with status {@code 400 (Bad Request)} if the machine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/machines")
    public ResponseEntity<Machine> updateMachine(@Valid @RequestBody Machine machine) throws URISyntaxException {
        log.debug("REST request to update Machine : {}", machine);
        if (machine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Machine result = machineRepository.save(machine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, machine.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /machines} : get all the machines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of machines in body.
     */
    @GetMapping("/machines")
    public List<Machine> getAllMachines() {
        log.debug("REST request to get all Machines");
        return machineRepository.findAll();
    }

    /**
     * {@code GET  /machines/:id} : get the "id" machine.
     *
     * @param id the id of the machine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/machines/{id}")
    public ResponseEntity<Machine> getMachine(@PathVariable Long id) {
        log.debug("REST request to get Machine : {}", id);
        Optional<Machine> machine = machineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(machine);
    }

    /**
     * {@code DELETE  /machines/:id} : delete the "id" machine.
     *
     * @param id the id of the machine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/machines/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id) {
        log.debug("REST request to delete Machine : {}", id);
        machineRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
