package hu.mycompany.machinemanager.applicaion.port.in;

import javax.validation.constraints.NotNull;

public class MachineJobCommand extends SelfValidating<MachineJobCommand> {

    @NotNull
    private Long machineId;

    public MachineJobCommand(Long machineId) {
        this.machineId = machineId;
    }

    public Long getMachineId() {
        return machineId;
    }
}
