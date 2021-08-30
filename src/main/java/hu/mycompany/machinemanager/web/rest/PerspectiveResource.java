package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.mapper.JobWithoutDrawing;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

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
        log.debug("REST request to get a page of Machines detailed a");
        return ResponseEntity.ok().body(perspectiveService.findAllOpenInInterval(startDate, endDate));
    }
}
