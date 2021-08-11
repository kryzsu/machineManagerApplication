package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.Holiday} entity.
 */
public class HolidayDTO implements Serializable {
    private Long id;

    private LocalDate day;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidayDTO)) {
            return false;
        }

        return id != null && id.equals(((HolidayDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidayDTO{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
