package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.Rawmaterial} entity.
 */
public class RawmaterialDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    private String comment;

    @NotNull
    private String grade;

    @NotNull
    private String dimension;

    @NotNull
    private String coating;

    @NotNull
    private String supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getCoating() {
        return coating;
    }

    public void setCoating(String coating) {
        this.coating = coating;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RawmaterialDTO)) {
            return false;
        }

        RawmaterialDTO rawmaterialDTO = (RawmaterialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rawmaterialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RawmaterialDTO{" +
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
