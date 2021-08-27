package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.service.MachineService;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import hu.mycompany.machinemanager.service.dto.MachineDetailedDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<MachineDetailed>> getDetailedMachineList() {
        log.debug("REST request to get a page of Machines detailed");
        Page<MachineDetailed> page = perspectiveService.findAll(PageRequest.of(0, 1000));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
