package hu.mycompany.machinemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public OutOfOrder() {}

    public OutOfOrder(LocalDate start, LocalDate end, String description, Set<Calendar> calendars, Set<Machine> machines) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.calendars = calendars;
        this.machines = machines;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start", nullable = false)
    private LocalDate start;

    @NotNull
    @Column(name = "end", nullable = false)
    private LocalDate end;

    @NotNull
    @Size(min = 5)
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "rel_out_of_order__calendar",
        joinColumns = @JoinColumn(name = "out_of_order_id"),
        inverseJoinColumns = @JoinColumn(name = "calendar_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs" }, allowSetters = true)
    private Set<Calendar> calendars = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_out_of_order__machine",
        joinColumns = @JoinColumn(name = "out_of_order_id"),
        inverseJoinColumns = @JoinColumn(name = "machine_id")
    )
    @JsonIgnoreProperties(value = { "outOfOrders", "jobs", "views", "runningJob" }, allowSetters = true)
    private Set<Machine> machines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OutOfOrder id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public OutOfOrder start(LocalDate start) {
        this.start = start;
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public OutOfOrder end(LocalDate end) {
        this.end = end;
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getDescription() {
        return this.description;
    }

    public OutOfOrder description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Machine> getMachines() {
        return this.machines;
    }

    public OutOfOrder machines(Set<Machine> machines) {
        this.setMachines(machines);
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

    public Set<Calendar> getCalendars() {
        return calendars;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutOfOrder{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
