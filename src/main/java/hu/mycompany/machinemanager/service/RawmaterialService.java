package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.RawmaterialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.Rawmaterial}.
 */
public interface RawmaterialService {
    /**
     * Save a rawmaterial.
     *
     * @param rawmaterialDTO the entity to save.
     * @return the persisted entity.
     */
    RawmaterialDTO save(RawmaterialDTO rawmaterialDTO);

    /**
     * Partially updates a rawmaterial.
     *
     * @param rawmaterialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RawmaterialDTO> partialUpdate(RawmaterialDTO rawmaterialDTO);

    /**
     * Get all the rawmaterials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RawmaterialDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rawmaterial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RawmaterialDTO> findOne(Long id);

    /**
     * Delete the "id" rawmaterial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
