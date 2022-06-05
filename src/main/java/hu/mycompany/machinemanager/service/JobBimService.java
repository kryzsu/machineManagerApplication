package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.service.dto.IdWithPriorityDTO;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link hu.mycompany.machinemanager.domain.Job}.
 */
public interface JobBimService extends JobService{

    Page<JobDTO> findAllOpenJobsForMachine(Pageable pageable, Long machineId);

    Page<JobDTO> findAllInProgressJobsForMachine(Pageable pageable, Long machineId);
    Optional<JobDTO> getHighestPriorityJobForMachine(Long machineId);
    Optional<JobDTO> getLowestPriorityJobForMachine(Long machineId);
    void startHighestPriorityJobInMachine(Long machineId);
    void stopRunningJobInMachine(Long machineId);
    Optional<Job> getRunningJobInMachine(Long machineId);

    void updatePriorities(List<IdWithPriorityDTO> idWithPriorityDTOList);

}
