package hu.mycompany.machinemanager.service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class MachineDayDTO implements Comparable {

    private LocalDate date;
    private boolean occupied;
    private String comment;

    private Long jobId;

    public MachineDayDTO(LocalDate date, boolean occupied, String comment, Long jobId) {
        this.date = date;
        this.occupied = occupied;
        this.comment = comment;
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineDayDTO that = (MachineDayDTO) o;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return "MachineDay{" + "date=" + date + ", occupied=" + occupied + ", comment='" + comment + '\'' + '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.date.compareTo(((MachineDayDTO) o).date);
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getComment() {
        return comment;
    }

    public Long getJobId() {
        return jobId;
    }
}
