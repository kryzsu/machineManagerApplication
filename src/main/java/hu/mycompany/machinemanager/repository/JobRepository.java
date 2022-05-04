package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Job;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Job entity.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(value = "select distinct job from Job job left join fetch job.products", countQuery = "select count(distinct job) from Job job")
    Page<Job> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct job from Job job left join fetch job.products")
    List<Job> findAllWithEagerRelationships();

    @Query("select job from Job job left join fetch job.products where job.id =:id")
    Optional<Job> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        "SELECT job FROM Job job join fetch job.machine " +
        "WHERE job.machine.id =:machineId " +
        "AND job.endDate is NULL " +
        "AND job.startDate > :date"
    )
    List<Job> findForMachineFutureOpenJobs(@Param("machineId") Long machineId, @Param("date") Date date);

    List<Job> findByMachineIdAndStartDateGreaterThanEqual(Long machineId, LocalDate date);

    List<Job> findByMachineIdAndStartDateIsNullOrderByPriorityDescCreateDateTimeDesc(Long machineId);

    Page<Job> findByMachineIdAndStartDateIsNotNullOrderByPriorityDescCreateDateTimeDesc(Long machineId, Pageable pageable);

    Page<Job> findByMachineIdAndEndDateIsNullOrderByPriorityDescCreateDateTimeDesc(Long machineId, Pageable pageable);

    Optional<Job> findFirstByMachineIdOrderByPriorityDesc(@Param("id") Long id);
    Optional<Job> findFirstByMachineIdOrderByPriority(@Param("id") Long id);
    Optional<Job> findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(@Param("machineId") Long machineId);

}
