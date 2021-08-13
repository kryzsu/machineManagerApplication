package hu.mycompany.machinemanager.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link hu.mycompany.machinemanager.domain.View} entity.
 */
public class ViewDTO implements Serializable {
    private Long id;

    private String name;

    private Set<MachineDTO> machines = new HashSet<>();

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
        if (!(o instanceof ViewDTO)) {
            return false;
        }

        return id != null && id.equals(((ViewDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", machines='" + getMachines() + "'" +
            "}";
    }
}
