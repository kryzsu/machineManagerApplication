package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
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

    @Column(name = "create_date_time")
    private ZonedDateTime createDateTime;

    @Column(name = "update_date_time")
    private ZonedDateTime updateDateTime;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "machine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "workeds", "machine" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

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

    public ZonedDateTime getCreateDateTime() {
        return this.createDateTime;
    }

    public Machine createDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return this.updateDateTime;
    }

    public Machine updateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }

    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Machine deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
        return "Machine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createDateTime='" + getCreateDateTime() + "'" +
            ", updateDateTime='" + getUpdateDateTime() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
