package hu.mycompany.machinemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hu.mycompany.machinemanager.domain.Product} entity. This class is used
 * in {@link hu.mycompany.machinemanager.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter drawingNumber;

    private StringFilter itemNumber;

    private DoubleFilter weight;

    private StringFilter comment;

    private LongFilter jobId;

    private LongFilter rawmaterialId;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.drawingNumber = other.drawingNumber == null ? null : other.drawingNumber.copy();
        this.itemNumber = other.itemNumber == null ? null : other.itemNumber.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.jobId = other.jobId == null ? null : other.jobId.copy();
        this.rawmaterialId = other.rawmaterialId == null ? null : other.rawmaterialId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDrawingNumber() {
        return drawingNumber;
    }

    public StringFilter drawingNumber() {
        if (drawingNumber == null) {
            drawingNumber = new StringFilter();
        }
        return drawingNumber;
    }

    public void setDrawingNumber(StringFilter drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public StringFilter getItemNumber() {
        return itemNumber;
    }

    public StringFilter itemNumber() {
        if (itemNumber == null) {
            itemNumber = new StringFilter();
        }
        return itemNumber;
    }

    public void setItemNumber(StringFilter itemNumber) {
        this.itemNumber = itemNumber;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public DoubleFilter weight() {
        if (weight == null) {
            weight = new DoubleFilter();
        }
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getJobId() {
        return jobId;
    }

    public LongFilter jobId() {
        if (jobId == null) {
            jobId = new LongFilter();
        }
        return jobId;
    }

    public void setJobId(LongFilter jobId) {
        this.jobId = jobId;
    }

    public LongFilter getRawmaterialId() {
        return rawmaterialId;
    }

    public LongFilter rawmaterialId() {
        if (rawmaterialId == null) {
            rawmaterialId = new LongFilter();
        }
        return rawmaterialId;
    }

    public void setRawmaterialId(LongFilter rawmaterialId) {
        this.rawmaterialId = rawmaterialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(drawingNumber, that.drawingNumber) &&
            Objects.equals(itemNumber, that.itemNumber) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(jobId, that.jobId) &&
            Objects.equals(rawmaterialId, that.rawmaterialId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, drawingNumber, itemNumber, weight, comment, jobId, rawmaterialId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (drawingNumber != null ? "drawingNumber=" + drawingNumber + ", " : "") +
            (itemNumber != null ? "itemNumber=" + itemNumber + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (jobId != null ? "jobId=" + jobId + ", " : "") +
            (rawmaterialId != null ? "rawmaterialId=" + rawmaterialId + ", " : "") +
            "}";
    }
}
