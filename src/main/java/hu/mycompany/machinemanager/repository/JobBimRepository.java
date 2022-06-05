package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobBimRepository extends JobRepository {

    @Query(
        "SELECT job FROM Job job join fetch job.machine " +
            "WHERE job.machine.id =:machineId " +
            "AND job.endDate is NULL " +
            "AND job.startDate > :date"
    )
    List<Job> findForMachineFutureOpenJobs(@Param("machineId") Long machineId, @Param("date") Date date);

    List<Job> findByMachineIdAndStartDateGreaterThanEqual(Long machineId, LocalDate date);

    List<Job> findByMachineIdAndStartDateIsNullOrderByPriorityDescIdDesc(Long machineId);
    Optional<Job> findTopByMachineIdAndStartDateIsNullOrderByPriorityDescIdDesc(Long machineId);

    Page<Job> findByMachineIdAndStartDateIsNotNullOrderByPriorityDescIdDesc(Long machineId, Pageable pageable);

    Page<Job> findByMachineIdAndEndDateIsNullOrderByPriorityDescIdDesc(Long machineId, Pageable pageable);

    Optional<Job> findFirstByMachineIdOrderByPriorityDesc(@Param("id") Long id);
    Optional<Job> findFirstByMachineIdOrderByPriority(@Param("id") Long id);
    Optional<Job> findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(@Param("machineId") Long machineId);

    Optional<Job> findByMachineIdAndStartDateIsNotNullAndEndDateIsNull(Long machineId);

}
