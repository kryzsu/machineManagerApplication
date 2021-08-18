package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.OutOfOrder} entity.
 */
public class OutOfOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(min = 5)
    private String description;

    private Set<MachineDTO> machines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MachineDTO> getMachines() {
        return machines;
    }

    public void setMachines(Set<MachineDTO> machines) {
        this.machines = machines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutOfOrderDTO)) {
            return false;
        }

        OutOfOrderDTO outOfOrderDTO = (OutOfOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outOfOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutOfOrderDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", machines=" + getMachines() +
            "}";
    }
}
