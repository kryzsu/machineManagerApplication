package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.applicaion.port.in.JobInformationUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobCommand;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobPlanningCommand;
import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PerspectiveResource controller
 */
@RestController
@RequestMapping("/api/perspective")
public class JobInformationResource {

    private final JobInformationUseCase jobInformationUseCase;

    private final Logger log = LoggerFactory.getLogger(JobInformationResource.class);

    @Autowired
    CacheManager cacheManager;

    public JobInformationResource(JobInformationUseCase jobInformationUseCase) {
        this.jobInformationUseCase = jobInformationUseCase;
    }

    @GetMapping("/get-detailed-machine-list")
    public ResponseEntity<List<MachineDetailed>> getDetailedMachineList() {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getDetailedMachineList");
        return ResponseEntity.ok().body(jobInformationUseCase.findAllOpen());
    }

    @GetMapping("/get-next-start-date-4-machine")
    public LocalDate getNextDateForMachine(@RequestParam long machineId, @RequestParam int estimation) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getNextDateForMachine");
        return jobInformationUseCase.getNextDateForMachine(new MachineJobPlanningCommand(machineId, estimation));
    }

    @GetMapping("/get-related-out-of-order")
    public List<OutOfOrderDTO> getRelatedOutOfOrder(@RequestParam long machineId) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getRelatedOutOfOrder");
        return jobInformationUseCase.getRelatedOutOfOrder(new MachineJobCommand(machineId));
    }

    @GetMapping("/get-job-next-days")
    public List<MachineDayDTO> getJobNextDays(@RequestParam long machineId, @RequestParam long days) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the RequestParam");
        return jobInformationUseCase.getJobNextDays(new JobInformationUseCase.GetJobNextDaysCommand(machineId, days));
    }
}
