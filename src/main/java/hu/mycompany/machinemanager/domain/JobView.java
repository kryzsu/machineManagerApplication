package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Job.
 */
@Entity
@Table(name = "v_job")
public class JobView implements Serializable {

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
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

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

    public JobView id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getEstimation() {
        return this.estimation;
    }

    public JobView estimation(Integer estimation) {
        this.estimation = estimation;
        return this;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }

    public Integer getProductCount() {
        return this.productCount;
    }

    public JobView productCount(Integer productCount) {
        this.productCount = productCount;
        return this;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public JobView startDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public JobView endDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getFact() {
        return this.fact;
    }

    public JobView fact(Integer fact) {
        this.fact = fact;
        return this;
    }

    public void setFact(Integer fact) {
        this.fact = fact;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public JobView orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDrawingNumber() {
        return this.drawingNumber;
    }

    public JobView drawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
        return this;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public byte[] getDrawing() {
        return this.drawing;
    }

    public JobView drawing(byte[] drawing) {
        this.drawing = drawing;
        return this;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }

    public String getDrawingContentType() {
        return this.drawingContentType;
    }

    public JobView drawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
        return this;
    }

    public void setDrawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public JobView products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public JobView removeProduct(Product product) {
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

    public JobView machine(Machine machine) {
        this.setMachine(machine);
        return this;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public JobView customer(Customer customer) {
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
        if (!(o instanceof JobView)) {
            return false;
        }
        return id != null && id.equals(((JobView) o).id);
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
