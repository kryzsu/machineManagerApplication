package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Product;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class JobWithoutDrawing implements Comparable {

    private Long id;
    private Integer estimation;
    private Integer productCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer fact;
    private String orderNumber;
    private String drawingNumber;
    private String consumerName;
    private String worknumber;
    private Product product;

    private Long priority;

    public static JobWithoutDrawing fromJob(Job job) {
        return new JobWithoutDrawing(
            job.getId(),
            job.getEstimation(),
            job.getProductCount(),
            job.getStartDate(),
            job.getEndDate(),
            job.getFact(),
            job.getOrderNumber(),
            job.getDrawingNumber(),
            job.getProduct(),
            (job.getCustomer() == null) ? null : job.getCustomer().getName(),
            job.getWorknumber(),
            (job.getPriority() == null) ? 0 : job.getPriority()
        );
    }

    public JobWithoutDrawing(
        Long id,
        Integer estimation,
        Integer productCount,
        LocalDate startDate,
        LocalDate endDate,
        Integer fact,
        String orderNumber,
        String drawingNumber,
        Product product,
        String consumerName,
        String worknumber,
        long priority
    ) {
        this.id = id;
        this.estimation = estimation;
        this.productCount = productCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fact = fact;
        this.orderNumber = orderNumber;
        this.drawingNumber = drawingNumber;
        this.product = product;
        this.consumerName = consumerName;
        this.worknumber = worknumber;
        this.priority = priority;
    }

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getWorknumber() {
        return worknumber;
    }

    public void setWorknumber(String worknumber) {
        this.worknumber = worknumber;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        return this.priority.compareTo(((JobWithoutDrawing) o).priority);
    }
}
