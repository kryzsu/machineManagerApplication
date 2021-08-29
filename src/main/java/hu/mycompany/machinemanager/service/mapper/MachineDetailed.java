package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A MachineDetailed.
 */

public class MachineDetailed implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private Set<JobWithoutDrawing> jobs = new HashSet<>();

    public MachineDetailed() {}

    public MachineDetailed(Long id, String name, String description, Set<Job> jobs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.jobs = jobs.stream().map(JobWithoutDrawing::fromJob).collect(Collectors.toSet());
    }

    public static MachineDetailed createUsingJobWithoutDrawing(Long id, String name, String description, Set<JobWithoutDrawing> jobs) {
        MachineDetailed rv = new MachineDetailed();
        rv.id = id;
        rv.name = name;
        rv.description = description;
        rv.jobs = jobs;
        return rv;
    }

    public static MachineDetailed toDetailed(Machine machine) {
        machine.getJobs().forEach(job -> job.getProducts().size());
        return new MachineDetailed(machine.getId(), machine.getName(), machine.getDescription(), machine.getJobs());
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

    public Set<JobWithoutDrawing> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobWithoutDrawing> jobs) {
        this.jobs = jobs;
    }
}
