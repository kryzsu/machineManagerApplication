package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Builder
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    @ManyToMany(mappedBy = "machines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "machines" }, allowSetters = true)
    private Set<OutOfOrder> outOfOrders = new HashSet<>();

    @OneToMany(mappedBy = "machine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "machine", "customer" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "running_job_id", referencedColumnName = "id")
    private Job runningJob;

    @ManyToMany(mappedBy = "machines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "machines" }, allowSetters = true)
    private Set<View> views = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Machine id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Machine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Machine description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OutOfOrder> getOutOfOrders() {
        return this.outOfOrders;
    }

    public Machine outOfOrders(Set<OutOfOrder> outOfOrders) {
        this.setOutOfOrders(outOfOrders);
        return this;
    }

    public Machine addOutOfOrder(OutOfOrder outOfOrder) {
        this.outOfOrders.add(outOfOrder);
        outOfOrder.getMachines().add(this);
        return this;
    }

    public Machine removeOutOfOrder(OutOfOrder outOfOrder) {
        this.outOfOrders.remove(outOfOrder);
        outOfOrder.getMachines().remove(this);
        return this;
    }

    public void setOutOfOrders(Set<OutOfOrder> outOfOrders) {
        if (this.outOfOrders != null) {
            this.outOfOrders.forEach(i -> i.removeMachine(this));
        }
        if (outOfOrders != null) {
            outOfOrders.forEach(i -> i.addMachine(this));
        }
        this.outOfOrders = outOfOrders;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public Machine jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Machine addJob(Job job) {
        this.jobs.add(job);
        job.setMachine(this);
        return this;
    }

    public Machine removeJob(Job job) {
        this.jobs.remove(job);
        job.setMachine(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.setMachine(null));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.setMachine(this));
        }
        this.jobs = jobs;
    }

    public Set<View> getViews() {
        return this.views;
    }

    public Machine views(Set<View> views) {
        this.setViews(views);
        return this;
    }

    public Machine addView(View view) {
        this.views.add(view);
        view.getMachines().add(this);
        return this;
    }

    public Machine removeView(View view) {
        this.views.remove(view);
        view.getMachines().remove(this);
        return this;
    }

    public void setViews(Set<View> views) {
        if (this.views != null) {
            this.views.forEach(i -> i.removeMachine(this));
        }
        if (views != null) {
            views.forEach(i -> i.addMachine(this));
        }
        this.views = views;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Machine)) {
            return false;
        }
        return id != null && id.equals(((Machine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return String.format("Machine{id=%d, name='%s', description='%s'}", getId(), getName(), getDescription());
    }

    public Job getRunningJob() {
        return runningJob;
    }

    public void setRunningJob(Job runningJob) {
        this.runningJob = runningJob;
    }
}
