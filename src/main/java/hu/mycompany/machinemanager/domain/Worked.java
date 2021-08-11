package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Worked.
 */
@Entity
@Table(name = "worked")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Worked implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day")
    private LocalDate day;

    @Column(name = "comment")
    private String comment;

    @ManyToMany(mappedBy = "workeds")
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

    public Worked id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDay() {
        return this.day;
    }

    public Worked day(LocalDate day) {
        this.day = day;
        return this;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getComment() {
        return this.comment;
    }

    public Worked comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public Worked jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Worked addJob(Job job) {
        this.jobs.add(job);
        job.getWorkeds().add(this);
        return this;
    }

    public Worked removeJob(Job job) {
        this.jobs.remove(job);
        job.getWorkeds().remove(this);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.removeWorked(this));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.addWorked(this));
        }
        this.jobs = jobs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Worked)) {
            return false;
        }
        return id != null && id.equals(((Worked) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Worked{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
