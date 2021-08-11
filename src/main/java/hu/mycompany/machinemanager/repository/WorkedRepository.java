package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Worked;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Worked entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkedRepository extends JpaRepository<Worked, Long> {}
