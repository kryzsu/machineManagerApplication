package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A View.
 */
@Entity
@Table(name = "view")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class View implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_view__machine",
        joinColumns = @JoinColumn(name = "view_id"),
        inverseJoinColumns = @JoinColumn(name = "machine_id")
    )
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs", "views" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public View id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public View name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public View machines(Set<Machine> machines) {
        this.setMachines(machines);
        return this;
    }

    public View addMachine(Machine machine) {
        this.machines.add(machine);
        machine.getViews().add(this);
        return this;
    }

    public View removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.getViews().remove(this);
        return this;
    }

    public void setMachines(Set<Machine> machines) {
        this.machines = machines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof View)) {
            return false;
        }
        return id != null && id.equals(((View) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "View{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
