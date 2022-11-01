package hu.mycompany.machinemanager.applicaion.port.in;

import javax.validation.constraints.NotNull;

public class MachineJobPlanningCommand extends SelfValidating<MachineJobPlanningCommand> {

    @NotNull
    private Long machineId;

    @NotNull
    private Integer estimation;

    public MachineJobPlanningCommand(Long machineId, Integer estimation) {
        this.machineId = machineId;
        this.estimation = estimation;
    }

    public Long getMachineId() {
        return machineId;
    }

    public Integer getEstimation() {
        return estimation;
    }
}
