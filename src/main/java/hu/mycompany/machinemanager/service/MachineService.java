package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.MachineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.Machine}.
 */
public interface MachineService {
    /**
     * Save a machine.
     *
     * @param machineDTO the entity to save.
     * @return the persisted entity.
     */
    MachineDTO save(MachineDTO machineDTO);

    /**
     * Partially updates a machine.
     *
     * @param machineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MachineDTO> partialUpdate(MachineDTO machineDTO);

    /**
     * Get all the machines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MachineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" machine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MachineDTO> findOne(Long id);

    /**
     * Delete the "id" machine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
