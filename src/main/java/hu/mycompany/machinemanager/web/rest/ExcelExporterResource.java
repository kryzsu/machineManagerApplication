package hu.mycompany.machinemanager.web.rest;

import hu.mycompany.machinemanager.applicaion.port.in.JobExporterUseCase;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * PerspectiveResource controller
 */
@RestController
@RequestMapping("/api/perspective")
public class ExcelExporterResource {

    private final JobExporterUseCase jobExporterUseCase;

    private final Logger log = LoggerFactory.getLogger(ExcelExporterResource.class);

    public ExcelExporterResource(JobExporterUseCase jobExporterUseCase) {
        this.jobExporterUseCase = jobExporterUseCase;
    }

    @GetMapping(value = "/get-job-gyartasi-lap", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] getGyartasiLap(@RequestParam @NotNull Long jobId) throws IOException {
        return jobExporterUseCase.getGyartasiLap(new JobExporterUseCase.JobExporterCommand(jobId));
    }

    @GetMapping(value = "/get-job-vissza-igazolas", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] getVisszaIgazolas(@RequestParam @NotNull Long jobId) throws IOException {
        return jobExporterUseCase.getVisszaIgazolas(new JobExporterUseCase.JobExporterCommand(jobId));
    }
}
