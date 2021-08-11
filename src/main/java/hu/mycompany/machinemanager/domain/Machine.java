package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @ManyToMany(mappedBy = "machines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<OutOfOrder> outOfOrders = new HashSet<>();

    @OneToMany(mappedBy = "machine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Machine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Machine description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OutOfOrder> getOutOfOrders() {
        return outOfOrders;
    }

    public Machine outOfOrders(Set<OutOfOrder> outOfOrders) {
        this.outOfOrders = outOfOrders;
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
        this.outOfOrders = outOfOrders;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public Machine jobs(Set<Job> jobs) {
        this.jobs = jobs;
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
        this.jobs = jobs;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Machine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
