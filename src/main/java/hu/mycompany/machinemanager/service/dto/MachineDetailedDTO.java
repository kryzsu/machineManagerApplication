package hu.mycompany.machinemanager.service.dto;

import hu.mycompany.machinemanager.domain.Job;
import java.util.HashSet;
import java.util.Set;

public class MachineDetailedDTO extends MachineDTO {

    private Set<Job> jobs = new HashSet<>();
}
