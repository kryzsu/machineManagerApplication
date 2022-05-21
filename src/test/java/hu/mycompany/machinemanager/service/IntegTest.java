package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.domain.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

class MachineDay implements Comparable {

    private LocalDate date;
    private boolean occupied;
    private String comment;

    private Long jobId;

    public MachineDay(LocalDate date, boolean occupied, String comment, Long jobId) {
        this.date = date;
        this.occupied = occupied;
        this.comment = comment;
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineDay that = (MachineDay) o;
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
        return this.date.compareTo(((MachineDay) o).date);
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

/**
 * Integration tests for {@link MailService}.
 */
@Slf4j
class IntegTest {

    @Test
    void one() throws Exception {
        Stream<OutOfOrder> streamOut = Stream.of(
            new OutOfOrder().start(LocalDate.of(2000, 1, 2)).end(LocalDate.of(2000, 1, 3)).description("desc"),
            new OutOfOrder().start(LocalDate.of(2000, 1, 10)).end(LocalDate.of(2000, 1, 12)).description("desc")
        );
        List<MachineDay> free = LocalDate
            .of(2000, 1, 1)
            .datesUntil(LocalDate.of(2000, 1, 14))
            .map(date -> new MachineDay(date, false, "free", null))
            .collect(Collectors.toList());

        List<MachineDay> occupied = streamOut
            .flatMap(
                outOfOrder ->
                    outOfOrder
                        .getStart()
                        .datesUntil(outOfOrder.getEnd())
                        .map(date -> new MachineDay(date, true, outOfOrder.getDescription(), null))
            )
            .collect(Collectors.toList());

        free.removeAll(occupied);

        Stream<Job> jobs = Stream.of(
            new Job().estimation(3).productCount(100).id(123L).worknumber("job 1").addProduct(new Product().name("productName")),
            new Job().estimation(2).productCount(100).id(123L).worknumber("job 2").addProduct(new Product().name("productName"))
        );

        List<MachineDay> jobMachineDayList = jobs
            .flatMap(
                job ->
                    Stream
                        .generate(() -> new MachineDay(LocalDate.EPOCH, true, job.getWorknumber(), job.getId()))
                        .limit(job.getEstimation())
            )
            .collect(Collectors.toList());

        List<MachineDay> all = new ArrayList<>();
        for (int i = 0; i < free.size(); i++) {
            MachineDay jobMachineDay = null;
            MachineDay freeMachineDay = null;
            if (jobMachineDayList.size() > 0) {
                jobMachineDay = jobMachineDayList.remove(0);
                freeMachineDay = free.remove(0);
            } else {
                break;
            }

            all.add(new MachineDay(freeMachineDay.getDate(), true, jobMachineDay.getComment(), jobMachineDay.getJobId()));
        }

        all.addAll(free);
        all.addAll(occupied);
        all = all.stream().sorted().collect(Collectors.toList());
        System.out.println(all);
    }
}
