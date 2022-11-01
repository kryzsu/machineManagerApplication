package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "estimation")
    private Integer estimation;

    @NotNull
    @Column(name = "product_count", nullable = false)
    private Integer productCount;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "fact")
    private Integer fact;

    @Column(name = "order_number")
    private String orderNumber;

    @Lob
    @Column(name = "drawing")
    private byte[] drawing;

    @Column(name = "drawing_content_type")
    private String drawingContentType;

    @NotNull
    @Column(name = "worknumber", nullable = false, unique = true)
    private String worknumber;

    @Column(name = "priority")
    private Long priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "jobs", "rawmaterial" }, allowSetters = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs", "views", "runningJob" }, allowSetters = true)
    private Machine machine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "jobs" }, allowSetters = true)
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "rel_job__calendar",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs" }, allowSetters = true)
    private Set<Calendar> calendars = new HashSet<>();

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

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

    public Integer getEstimation() {
        return this.estimation;
    }

    public Job estimation(Integer estimation) {
        this.estimation = estimation;
        return this;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }

    public Integer getProductCount() {
        return this.productCount;
    }

    public Job productCount(Integer productCount) {
        this.productCount = productCount;
        return this;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Job startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Job endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getFact() {
        return this.fact;
    }

    public Job fact(Integer fact) {
        this.fact = fact;
        return this;
    }

    public void setFact(Integer fact) {
        this.fact = fact;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public Job orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Job createDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    public byte[] getDrawing() {
        return this.drawing;
    }

    public Job drawing(byte[] drawing) {
        this.drawing = drawing;
        return this;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }

    public String getDrawingContentType() {
        return this.drawingContentType;
    }

    public Job drawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
        return this;
    }

    public void setDrawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
    }

    public String getWorknumber() {
        return this.worknumber;
    }

    public Job worknumber(String worknumber) {
        this.worknumber = worknumber;
        return this;
    }

    public void setWorknumber(String worknumber) {
        this.worknumber = worknumber;
    }

    public Long getPriority() {
        return this.priority;
    }

    public Job priority(Long priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Product getProduct() {
        return this.product;
    }

    public Job product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public Customer getCustomer() {
        return this.customer;
    }

    public Job customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Calendar> getCalendars() {
        return calendars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (!id.equals(job.id)) return false;
        if (estimation != null ? !estimation.equals(job.estimation) : job.estimation != null) return false;
        if (!productCount.equals(job.productCount)) return false;
        if (!createDateTime.equals(job.createDateTime)) return false;
        if (updateDateTime != null ? !updateDateTime.equals(job.updateDateTime) : job.updateDateTime != null) return false;
        if (startDate != null ? !startDate.equals(job.startDate) : job.startDate != null) return false;
        if (endDate != null ? !endDate.equals(job.endDate) : job.endDate != null) return false;
        if (fact != null ? !fact.equals(job.fact) : job.fact != null) return false;
        if (!orderNumber.equals(job.orderNumber)) return false;
        if (!Arrays.equals(drawing, job.drawing)) return false;
        if (drawingContentType != null ? !drawingContentType.equals(job.drawingContentType) : job.drawingContentType != null) return false;
        if (!worknumber.equals(job.worknumber)) return false;
        if (!priority.equals(job.priority)) return false;
        if (product != null ? !product.equals(job.product) : job.product != null) return false;
        if (!machine.equals(job.machine)) return false;
        if (customer != null ? !customer.equals(job.customer) : job.customer != null) return false;
        return calendars.equals(job.calendars);
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
            "id=" + id +
            ", estimation=" + estimation +
            ", productCount=" + productCount +
            ", createDateTime=" + createDateTime +
            ", updateDateTime=" + updateDateTime +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", fact=" + fact +
            ", orderNumber='" + orderNumber + '\'' +
            ", drawing=" + Arrays.toString(drawing) +
            ", drawingContentType='" + drawingContentType + '\'' +
            ", worknumber='" + worknumber + '\'' +
            ", priority=" + priority +
            ", product=" + product +
            ", machine=" + machine +
            ", customer=" + customer +
            '}';
    }
}
