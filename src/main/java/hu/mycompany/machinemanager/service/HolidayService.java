package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.HolidayDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.Holiday}.
 */
public interface HolidayService {
    /**
     * Save a holiday.
     *
     * @param holidayDTO the entity to save.
     * @return the persisted entity.
     */
    HolidayDTO save(HolidayDTO holidayDTO);

    /**
     * Get all the holidays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HolidayDTO> findAll(Pageable pageable);

    /**
     * Get the "id" holiday.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HolidayDTO> findOne(Long id);

    /**
     * Delete the "id" holiday.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
