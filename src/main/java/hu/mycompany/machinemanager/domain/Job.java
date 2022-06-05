package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
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

    @Column(name = "estimation")
    private Integer estimation;

    @NotNull
    @Column(name = "product_count", nullable = false)
    private Integer productCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "fact")
    private Integer fact;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "drawing_number")
    private String drawingNumber;

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

    public String getDrawingNumber() {
        return this.drawingNumber;
    }

    public Job drawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
        return this;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
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
            ", estimation=" + getEstimation() +
            ", productCount=" + getProductCount() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fact=" + getFact() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", drawingNumber='" + getDrawingNumber() + "'" +
            ", drawing='" + getDrawing() + "'" +
            ", drawingContentType='" + getDrawingContentType() + "'" +
            ", worknumber='" + getWorknumber() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
