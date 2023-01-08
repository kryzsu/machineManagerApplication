package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Machine;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Machine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    @Query(
        value = "SELECT m FROM Machine m " + "LEFT JOIN FETCH m.openJobs",
        countQuery = "select count(m) from Machine m " + "LEFT JOIN FETCH m.openJobs"
    )
    List<Machine> findAllWithJobs();
}
