package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import java.io.Serializable;

/**
 * A MachineDetailed.
 */

public class MachineDetailed implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private Job runningJob;

    public MachineDetailed() {}

    public MachineDetailed(Long id, String name, String description, Job runningJob) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.runningJob = runningJob;
    }

    public static MachineDetailed createUsingJobWithoutDrawing(Long id, String name, String description, Job runningJob) {
        MachineDetailed rv = new MachineDetailed();
        rv.id = id;
        rv.name = name;
        rv.description = description;
        rv.setRunningJob(runningJob);
        return rv;
    }

    public static MachineDetailed toDetailed(Machine machine) {
        return new MachineDetailed(machine.getId(), machine.getName(), machine.getDescription(), machine.getRunningJob());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Job getRunningJob() {
        return runningJob;
    }

    public void setRunningJob(Job runningJob) {
        this.runningJob = runningJob;
    }
}
