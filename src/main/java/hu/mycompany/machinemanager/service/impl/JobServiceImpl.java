package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.service.AnotherJobIsAlreadyRunningException;
import hu.mycompany.machinemanager.service.JobService;
import hu.mycompany.machinemanager.service.NoRunningJobException;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import hu.mycompany.machinemanager.service.mapper.JobMapper;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Job}.
 */
@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public JobDTO save(JobDTO jobDTO) {
        log.debug("Request to save Job : {}", jobDTO);
        Job job = jobMapper.toEntity(jobDTO);
        log.debug("Request to save Job entity : {}", job);
        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    @Override
    public Optional<JobDTO> partialUpdate(JobDTO jobDTO) {
        log.debug("Request to partially update Job : {}", jobDTO);

        return jobRepository
            .findById(jobDTO.getId())
            .map(
                existingJob -> {
                    jobMapper.partialUpdate(existingJob, jobDTO);

                    return existingJob;
                }
            )
            .map(jobRepository::save)
            .map(jobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        return jobRepository.findAll(pageable).map(jobMapper::toDto);
    }

    public Page<JobDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jobRepository.findAllWithEagerRelationships(pageable).map(jobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobDTO> findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        return jobRepository.findOneWithEagerRelationships(id).map(jobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.deleteById(id);
    }

    @Override
    public Page<JobDTO> findAllOpenJobsForMachine(Pageable pageable, Long machineId) {
        return jobRepository
            .findByMachineIdAndStartDateIsNotNullOrderByPriorityDescCreateDateTimeDesc(machineId, pageable)
            .map(jobMapper::toDto);
    }

    @Override
    public Page<JobDTO> findAllInProgressJobsForMachine(Pageable pageable, Long machineId) {
        return jobRepository
            .findByMachineIdAndEndDateIsNullOrderByPriorityDescCreateDateTimeDesc(machineId, pageable)
            .map(jobMapper::toDto);
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
        if (!runningJob.isPresent()) {
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
        Optional<Job> job = jobRepository.findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(machineId);
        return job;
    }
}
