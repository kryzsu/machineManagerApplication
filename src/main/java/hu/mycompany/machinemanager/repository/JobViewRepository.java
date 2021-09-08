package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.JobView;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Job entity.
 */
@Repository
public interface JobViewRepository extends ReadOnlyRepository<JobView, Long> {
    @Query(
        value = "select job from JobView job, Machine machine where job.machine = machine " +
        "and machine.id = :machnieId and job.startDate between :start and :end"
    )
    List<JobView> findAllConflicts(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("machnieId") Long machineId);
}
