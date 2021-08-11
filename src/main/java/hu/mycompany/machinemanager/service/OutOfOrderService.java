package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.OutOfOrder}.
 */
public interface OutOfOrderService {
    /**
     * Save a outOfOrder.
     *
     * @param outOfOrderDTO the entity to save.
     * @return the persisted entity.
     */
    OutOfOrderDTO save(OutOfOrderDTO outOfOrderDTO);

    /**
     * Get all the outOfOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutOfOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" outOfOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutOfOrderDTO> findOne(Long id);

    /**
     * Delete the "id" outOfOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
