package hu.mycompany.machinemanager.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OutOfOrder.
 */
@Entity
@Table(name = "out_of_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OutOfOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(min = 5)
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "out_of_order_machine",
        joinColumns = @JoinColumn(name = "out_of_order_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "machine_id", referencedColumnName = "id")
    )
    private Set<Machine> machines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public OutOfOrder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public OutOfOrder description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Machine> getMachines() {
        return machines;
    }

    public OutOfOrder machines(Set<Machine> machines) {
        this.machines = machines;
        return this;
    }

    public OutOfOrder addMachine(Machine machine) {
        this.machines.add(machine);
        machine.getOutOfOrders().add(this);
        return this;
    }

    public OutOfOrder removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.getOutOfOrders().remove(this);
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
        if (!(o instanceof OutOfOrder)) {
            return false;
        }
        return id != null && id.equals(((OutOfOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutOfOrder{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
