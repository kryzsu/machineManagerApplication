package hu.mycompany.machinemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link hu.mycompany.machinemanager.domain.OutOfOrder} entity. This class is used
 * in {@link hu.mycompany.machinemanager.web.rest.OutOfOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /out-of-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OutOfOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter start;

    private LocalDateFilter end;

    private StringFilter description;

    private LongFilter machineId;

    public OutOfOrderCriteria() {}

    public OutOfOrderCriteria(OutOfOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.start = other.start == null ? null : other.start.copy();
        this.end = other.end == null ? null : other.end.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.machineId = other.machineId == null ? null : other.machineId.copy();
    }

    @Override
    public OutOfOrderCriteria copy() {
        return new OutOfOrderCriteria(this);
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

    public LocalDateFilter getStart() {
        return start;
    }

    public LocalDateFilter start() {
        if (start == null) {
            start = new LocalDateFilter();
        }
        return start;
    }

    public void setStart(LocalDateFilter start) {
        this.start = start;
    }

    public LocalDateFilter getEnd() {
        return end;
    }

    public LocalDateFilter end() {
        if (end == null) {
            end = new LocalDateFilter();
        }
        return end;
    }

    public void setEnd(LocalDateFilter end) {
        this.end = end;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getMachineId() {
        return machineId;
    }

    public LongFilter machineId() {
        if (machineId == null) {
            machineId = new LongFilter();
        }
        return machineId;
    }

    public void setMachineId(LongFilter machineId) {
        this.machineId = machineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OutOfOrderCriteria that = (OutOfOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(description, that.description) &&
            Objects.equals(machineId, that.machineId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, description, machineId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutOfOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (start != null ? "start=" + start + ", " : "") +
            (end != null ? "end=" + end + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (machineId != null ? "machineId=" + machineId + ", " : "") +
            "}";
    }
}
