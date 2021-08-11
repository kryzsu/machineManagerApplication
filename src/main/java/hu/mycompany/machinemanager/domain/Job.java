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
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotNull
    @Column(name = "days", nullable = false)
    private Integer days;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_date_time")
    private ZonedDateTime createDateTime;

    @Column(name = "update_date_time")
    private ZonedDateTime updateDateTime;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "in_progress")
    private Boolean inProgress;

    @Column(name = "days_done")
    private Integer daysDone;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_job__worked", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "worked_id"))
    @JsonIgnoreProperties(value = { "jobs" }, allowSetters = true)
    private Set<Worked> workeds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "jobs" }, allowSetters = true)
    private Machine machine;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job id(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Job customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getDays() {
        return this.days;
    }

    public Job days(Integer days) {
        this.days = days;
        return this;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getProductName() {
        return this.productName;
    }

    public Job productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCount() {
        return this.count;
    }

    public Job count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getProductType() {
        return this.productType;
    }

    public Job productType(String productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getComment() {
        return this.comment;
    }

    public Job comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreateDateTime() {
        return this.createDateTime;
    }

    public Job createDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return this.updateDateTime;
    }

    public Job updateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }

    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Job deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getInProgress() {
        return this.inProgress;
    }

    public Job inProgress(Boolean inProgress) {
        this.inProgress = inProgress;
        return this;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getDaysDone() {
        return this.daysDone;
    }

    public Job daysDone(Integer daysDone) {
        this.daysDone = daysDone;
        return this;
    }

    public void setDaysDone(Integer daysDone) {
        this.daysDone = daysDone;
    }

    public Set<Worked> getWorkeds() {
        return this.workeds;
    }

    public Job workeds(Set<Worked> workeds) {
        this.setWorkeds(workeds);
        return this;
    }

    public Job addWorked(Worked worked) {
        this.workeds.add(worked);
        worked.getJobs().add(this);
        return this;
    }

    public Job removeWorked(Worked worked) {
        this.workeds.remove(worked);
        worked.getJobs().remove(this);
        return this;
    }

    public void setWorkeds(Set<Worked> workeds) {
        this.workeds = workeds;
    }

    public Machine getMachine() {
        return this.machine;
    }

    public Job machine(Machine machine) {
        this.setMachine(machine);
        return this;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return id != null && id.equals(((Job) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", days=" + getDays() +
            ", productName='" + getProductName() + "'" +
            ", count=" + getCount() +
            ", productType='" + getProductType() + "'" +
            ", comment='" + getComment() + "'" +
            ", createDateTime='" + getCreateDateTime() + "'" +
            ", updateDateTime='" + getUpdateDateTime() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", inProgress='" + getInProgress() + "'" +
            ", daysDone=" + getDaysDone() +
            "}";
    }
}
