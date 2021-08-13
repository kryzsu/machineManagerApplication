package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.ViewDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.View}.
 */
public interface ViewService {
    /**
     * Save a view.
     *
     * @param viewDTO the entity to save.
     * @return the persisted entity.
     */
    ViewDTO save(ViewDTO viewDTO);

    /**
     * Get all the views.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ViewDTO> findAll(Pageable pageable);

    /**
     * Get all the views with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ViewDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" view.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ViewDTO> findOne(Long id);

    /**
     * Delete the "id" view.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
