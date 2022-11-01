package hu.mycompany.machinemanager.applicaion.port.in;

public class JobIdWithPriority {

    private Long jobId;
    private Long priority;

    public JobIdWithPriority(Long jobId, Long priority) {
        this.jobId = jobId;
        this.priority = priority;
    }

    public Long getJobId() {
        return jobId;
    }

    public Long getPriority() {
        return priority;
    }
}
