package hu.mycompany.machinemanager.applicaion.port.in;

import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobInformationUseCase {
    List<MachineDayDTO> getJobNextDays(GetJobNextDaysCommand getJobNextDaysCommand);

    public List<OutOfOrderDTO> getRelatedOutOfOrder(MachineJobCommand machineJobCommand);

    List<MachineDetailed> findAllOpen();
    Page<MachineDetailed> findAll(Pageable pageable);

    public LocalDate getNextDateForMachine(MachineJobPlanningCommand machineJobPlanningCommand);

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
