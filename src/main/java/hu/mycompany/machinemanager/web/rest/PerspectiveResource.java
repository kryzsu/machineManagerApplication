package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.debug("REST request to get a page of Machines detailed list");
        return ResponseEntity.ok().body(perspectiveService.findAllOpenInInterval(startDate, endDate));
    }

    @GetMapping("/get-next-start-date-4-machine")
    public LocalDate getNextDateForMachine(@RequestParam long machineId) {
        log.debug("REST request to get the getNextDateForMachine");
        return perspectiveService.getNextDateForMachine(machineId);
    }
}
