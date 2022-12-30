package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.applicaion.port.in.JobIdWithPriority;
import hu.mycompany.machinemanager.applicaion.port.in.JobInformationUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.JobManageUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobCommand;
import hu.mycompany.machinemanager.service.dto.IdWithPriorityDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

/**
 * PerspectiveResource controller
 */
@RestController
@RequestMapping("/api/perspective")
public class JobManageResource {

    private final JobManageUseCase jobManageUseCase;
    private final JobInformationUseCase jobInformationUseCase;

    private final Logger log = LoggerFactory.getLogger(JobManageResource.class);

    @Autowired
    CacheManager cacheManager;

    public JobManageResource(JobManageUseCase jobManageUseCase, JobInformationUseCase jobInformationUseCase) {
        this.jobManageUseCase = jobManageUseCase;
        this.jobInformationUseCase = jobInformationUseCase;
    }

    @PostMapping("/start-next-job")
    public void startNextJob(@RequestBody long machineId) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("/start-next-job", machineId);
        jobManageUseCase.startNextJob(new MachineJobCommand(machineId));
    }

    @PostMapping("/stop-running-job")
    public void stopRunningJob(@RequestBody long machineId) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("/stop-running-job", machineId);
        jobManageUseCase.stopRunningJob(new MachineJobCommand(machineId));
    }

    @PostMapping("/save-priorities")
    public void savePriorities(@RequestBody List<IdWithPriorityDTO> priorities) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("/save-priorities", priorities);
        jobManageUseCase.savePriorities(
            new JobManageUseCase.SavePrioritiesCommand(
                priorities
                    .stream()
                    .map(idWithPriorityDTO -> new JobIdWithPriority(idWithPriorityDTO.getId(), idWithPriorityDTO.getPriority()))
                    .collect(Collectors.toList())
            )
        );
    }
}
