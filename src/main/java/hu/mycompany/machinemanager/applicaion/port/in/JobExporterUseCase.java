package hu.mycompany.machinemanager.applicaion.port.in;

import java.io.IOException;
import javax.validation.constraints.NotNull;

public interface JobExporterUseCase {
    public byte[] getGyartasiLap(JobExporterCommand jobExporterCommand) throws IOException;

    public byte[] getVisszaIgazolas(JobExporterCommand jobExporterCommand) throws IOException;

    final class JobExporterCommand extends SelfValidating<JobExporterCommand> {

        @NotNull
        private Long jobId;

        public JobExporterCommand(Long jobId) {
            this.jobId = jobId;
        }

        public Long getJobId() {
            return jobId;
        }
    }
}
