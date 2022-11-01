package hu.mycompany.machinemanager.applicaion.port.in;

import hu.mycompany.machinemanager.service.dto.IdWithPriorityDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

public interface JobManageUseCase {
    void startNextJob(MachineJobCommand machineJobCommand);
    void stopRunningJob(MachineJobCommand machineJobCommand);

    void savePriorities(SavePrioritiesCommand savePrioritiesCommand);

    final class SavePrioritiesCommand extends SelfValidating<SavePrioritiesCommand> {

        @NotNull
        private List<JobIdWithPriority> jobIdWithPriorityList;

        public SavePrioritiesCommand(List<JobIdWithPriority> jobIdWithPriorityList) {
            this.jobIdWithPriorityList = jobIdWithPriorityList;
        }

        public List<JobIdWithPriority> getJobIdWithPriorityList() {
            return jobIdWithPriorityList;
        }
    }

    final class GetJobNextDaysCommand extends SelfValidating<GetJobNextDaysCommand> {

        @NotNull
        private Long machineId;

        @NotNull
        Long days;

        public GetJobNextDaysCommand(Long machineId, Long days) {
            this.machineId = machineId;
            this.days = days;
        }

        public Long getMachineId() {
            return machineId;
        }

        public Long getDays() {
            return days;
        }
    }
}
