package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.Job} entity.
 */
public class JobDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer estimation;

    @NotNull
    private Integer productCount;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private Integer fact;

    private String orderNumber;

    private String drawingNumber;

    private Long priority;

    private Long manualOrder;

    @Lob
    private byte[] drawing;

    private String drawingContentType;

    @NotNull
    private String worknumber;

    private Set<ProductDTO> products = new HashSet<>();

    private MachineDTO machine;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getFact() {
        return fact;
    }

    public void setFact(Integer fact) {
        this.fact = fact;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
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

    public String getWorknumber() {
        return worknumber;
    }

    public void setWorknumber(String worknumber) {
        this.worknumber = worknumber;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public MachineDTO getMachine() {
        return machine;
    }

    public void setMachine(MachineDTO machine) {
        this.machine = machine;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getManualOrder() {
        return manualOrder;
    }

    public void setManualOrder(Long manualOrder) {
        this.manualOrder = manualOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDTO)) {
            return false;
        }

        JobDTO jobDTO = (JobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return MessageFormat.format("JobDTO'{'id={0}, estimation={1}, priority={2}, manualOrder={3}, " +
            "productCount={4}, startDate=''{5}'', endDate=''{6}'', fact={7}, orderNumber=''{8}'', drawingNumber=''{9}''" +
            ", drawing=''{10}'', worknumber=''{11}'', products={12}, machine={13}, customer={14}'}'",
            getId(), getEstimation(), priority, manualOrder, getProductCount(), getStartDate(), getEndDate(),
            getFact(), getOrderNumber(), getDrawingNumber(), getDrawing(), getWorknumber(), getProducts(),
            getMachine(), getCustomer());
    }
}
