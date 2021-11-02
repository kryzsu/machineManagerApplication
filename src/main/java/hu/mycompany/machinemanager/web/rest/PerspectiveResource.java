package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.service.PerspectiveService;
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
public class PerspectiveResource {

    private final PerspectiveService perspectiveService;
    private final Logger log = LoggerFactory.getLogger(PerspectiveResource.class);

    @Autowired
    CacheManager cacheManager;

    public PerspectiveResource(PerspectiveService perspectiveService) {
        this.perspectiveService = perspectiveService;
    }

    /**
     * GET getDetailedMachineList
     */
    @GetMapping("/get-detailed-machine-list")
    public ResponseEntity<List<MachineDetailed>> getDetailedMachineList(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
    ) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getDetailedMachineList");
        return ResponseEntity.ok().body(perspectiveService.findAllOpenInInterval(startDate, endDate));
    }

    @GetMapping("/get-next-start-date-4-machine")
    public LocalDate getNextDateForMachine(@RequestParam long machineId, @RequestParam int estimation) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getNextDateForMachine");
        return perspectiveService.getNextDateForMachine(machineId, estimation);
    }

    @GetMapping("/get-related-out-of-order")
    public List<OutOfOrderDTO> getRelatedOutOfOrder(@RequestParam long machineId) {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.debug("REST request to get the getRelatedOutOfOrder");
        return perspectiveService.getRelatedOutOfOrder(machineId);
    }
}
