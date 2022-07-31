package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobBimRepository;
import hu.mycompany.machinemanager.service.AnotherJobIsAlreadyRunningException;
import hu.mycompany.machinemanager.service.JobBimService;
import hu.mycompany.machinemanager.service.NoRunningJobException;
import hu.mycompany.machinemanager.service.dto.IdWithPriorityDTO;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import hu.mycompany.machinemanager.service.mapper.JobMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Job}.
 */
@Service
@Primary
@Transactional
public class JobBimServiceImpl extends JobServiceImpl implements JobBimService {

    private final Logger log = LoggerFactory.getLogger(JobBimServiceImpl.class);

    private final JobBimRepository jobRepository;

    private final JobMapper jobMapper;

    public JobBimServiceImpl(JobBimRepository jobRepository, JobMapper jobMapper) {
        super(jobRepository, jobMapper);
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public Page<JobDTO> findAllOpenJobsForMachine(Pageable pageable, Long machineId) {
        return jobRepository.findByMachineIdAndStartDateIsNotNullOrderByPriorityDescIdDesc(machineId, pageable).map(jobMapper::toDto);
    }

    @Override
    public Page<JobDTO> findAllInProgressJobsForMachine(Pageable pageable, Long machineId) {
        return jobRepository.findByMachineIdAndEndDateIsNullOrderByPriorityDescIdDesc(machineId, pageable).map(jobMapper::toDto);
    }

    @Override
    public Optional<JobDTO> getHighestPriorityJobForMachine(Long machineId) {
        return jobRepository.findFirstByMachineIdOrderByPriorityDesc(machineId).map(jobMapper::toDto);
    }

    @Override
    public Optional<JobDTO> getLowestPriorityJobForMachine(Long machineId) {
        return jobRepository.findFirstByMachineIdOrderByPriority(machineId).map(jobMapper::toDto);
    }

    @Override
    public void startHighestPriorityJobInMachine(Long machineId) {
        Optional<Job> runningJob = getRunningJobInMachine(machineId);
        if (runningJob.isEmpty()) {
            throw new AnotherJobIsAlreadyRunningException(machineId);
        }

        Optional<Job> highestPriorityJobForMachine = jobRepository.findFirstByMachineIdOrderByPriorityDesc(machineId);
        if (highestPriorityJobForMachine.isPresent()) {
            Job job = highestPriorityJobForMachine.get();
            job.setStartDate(LocalDate.now());
            jobRepository.save(job);
        }
    }

    @Override
    public void stopRunningJobInMachine(Long machineId) {
        Job job = getRunningJobInMachine(machineId).orElseThrow(() -> new NoRunningJobException(machineId));
        job.setEndDate(LocalDate.now());
    }

    @Override
    public Optional<Job> getRunningJobInMachine(Long machineId) {
        return jobRepository.findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(machineId);
    }

    @Override
    public void updatePriorities(List<IdWithPriorityDTO> idWithPriorityDTOList) {
        idWithPriorityDTOList.forEach(
            idWithPriorityDTO -> {
                Job job = jobRepository.findById(idWithPriorityDTO.getId()).get().priority(idWithPriorityDTO.getPriority());
                jobRepository.save(job);
            }
        );
    }
}
