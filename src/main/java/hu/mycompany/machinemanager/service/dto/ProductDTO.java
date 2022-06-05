package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String name;

    @NotNull
    private String drawingNumber;

    private String itemNumber;

    @NotNull
    private Double weight;

    private String comment;

    @Lob
    private byte[] drawing;

    private String drawingContentType;
    private RawmaterialDTO rawmaterial;

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

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getDrawing() {
        return drawing;
    }

    public void setDrawing(byte[] drawing) {
        this.drawing = drawing;
    }

    public String getDrawingContentType() {
        return drawingContentType;
    }

    public void setDrawingContentType(String drawingContentType) {
        this.drawingContentType = drawingContentType;
    }

    public RawmaterialDTO getRawmaterial() {
        return rawmaterial;
    }

    public void setRawmaterial(RawmaterialDTO rawmaterial) {
        this.rawmaterial = rawmaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", drawingNumber='" + getDrawingNumber() + "'" +
            ", itemNumber='" + getItemNumber() + "'" +
            ", weight=" + getWeight() +
            ", comment='" + getComment() + "'" +
            ", drawing='" + getDrawing() + "'" +
            ", rawmaterial=" + getRawmaterial() +
            "}";
    }
}
