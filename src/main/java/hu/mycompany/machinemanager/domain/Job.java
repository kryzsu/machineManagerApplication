package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_job__product", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonIgnoreProperties(value = { "jobs" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs", "views" }, allowSetters = true)
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

    public Set<Product> getProducts() {
        return this.products;
    }

    public Job products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Job addProduct(Product product) {
        this.products.add(product);
        product.getJobs().add(this);
        return this;
    }

    public Job removeProduct(Product product) {
        this.products.remove(product);
        product.getJobs().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
            "}";
    }
}
