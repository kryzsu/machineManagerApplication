package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "drawing_number", nullable = false)
    private String drawingNumber;

    @Column(name = "item_number")
    private String itemNumber;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "comment")
    private String comment;

    @Lob
    @Column(name = "drawing")
    private byte[] drawing;

    @Column(name = "drawing_content_type")
    private String drawingContentType;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "machine", "customer" }, allowSetters = true)
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    private Rawmaterial rawmaterial;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrawingNumber() {
        return this.drawingNumber;
    }

    public Product drawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
        return this;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public String getItemNumber() {
        return this.itemNumber;
    }

    public Product itemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
        return this;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Double getWeight() {
        return this.weight;
    }

    public Product weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getComment() {
        return this.comment;
    }

    public Product comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getDrawing() {
        return this.drawing;
    }

    public Product drawing(byte[] drawing) {
        this.drawing = drawing;
        return this;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }

    public String getDrawingContentType() {
        return this.drawingContentType;
    }

    public Product drawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
        return this;
    }

    public void setDrawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
    }

    public Set<Job> getJobs() {
        return this.jobs;
    }

    public Product jobs(Set<Job> jobs) {
        this.setJobs(jobs);
        return this;
    }

    public Product addJob(Job job) {
        this.jobs.add(job);
        job.setProduct(this);
        return this;
    }

    public Product removeJob(Job job) {
        this.jobs.remove(job);
        job.setProduct(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        if (this.jobs != null) {
            this.jobs.forEach(i -> i.setProduct(null));
        }
        if (jobs != null) {
            jobs.forEach(i -> i.setProduct(this));
        }
        this.jobs = jobs;
    }

    public Rawmaterial getRawmaterial() {
        return this.rawmaterial;
    }

    public Product rawmaterial(Rawmaterial rawmaterial) {
        this.setRawmaterial(rawmaterial);
        return this;
    }

    public void setRawmaterial(Rawmaterial rawmaterial) {
        this.rawmaterial = rawmaterial;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", drawingNumber='" + getDrawingNumber() + "'" +
            ", itemNumber='" + getItemNumber() + "'" +
            ", weight=" + getWeight() +
            ", comment='" + getComment() + "'" +
            ", drawing='" + getDrawing() + "'" +
            ", drawingContentType='" + getDrawingContentType() + "'" +
            "}";
    }
}
