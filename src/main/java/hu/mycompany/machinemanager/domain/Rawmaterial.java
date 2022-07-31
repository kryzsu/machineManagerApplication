package hu.mycompany.machinemanager.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rawmaterial.
 */
@Entity
@Table(name = "rawmaterial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rawmaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "grade", nullable = false)
    private String grade;

    @NotNull
    @Column(name = "dimension", nullable = false)
    private String dimension;

    @NotNull
    @Column(name = "coating", nullable = false)
    private String coating;

    @NotNull
    @Column(name = "supplier", nullable = false)
    private String supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rawmaterial id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Rawmaterial name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return this.comment;
    }

    public Rawmaterial comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGrade() {
        return this.grade;
    }

    public Rawmaterial grade(String grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDimension() {
        return this.dimension;
    }

    public Rawmaterial dimension(String dimension) {
        this.dimension = dimension;
        return this;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getCoating() {
        return this.coating;
    }

    public Rawmaterial coating(String coating) {
        this.coating = coating;
        return this;
    }

    public void setCoating(String coating) {
        this.coating = coating;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Rawmaterial supplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rawmaterial)) {
            return false;
        }
        return id != null && id.equals(((Rawmaterial) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rawmaterial{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", comment='" + getComment() + "'" +
            ", grade='" + getGrade() + "'" +
            ", dimension='" + getDimension() + "'" +
            ", coating='" + getCoating() + "'" +
            ", supplier='" + getSupplier() + "'" +
            "}";
    }
}
