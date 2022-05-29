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
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
    void date() throws Exception {
        List<LocalDate> normalList = LocalDate.of(2000, 1, 1)
            .datesUntil(LocalDate.of(2000, 1, 14))
            .collect(Collectors.toList());

        Assert.assertEquals(13, normalList.size());

        try {
            List<LocalDate> invalidList = LocalDate.of(2000, 1, 10)
                .datesUntil(LocalDate.of(2000, 1, 1))
                .collect(Collectors.toList());
            fail("no exception ????");
        } catch (IllegalArgumentException illegalArgumentException) {

        }

        List<LocalDate> noDayz = LocalDate.of(2000, 1, 1)
            .datesUntil(LocalDate.of(2000, 1, 1))
            .collect(Collectors.toList());
        Assert.assertEquals(0, noDayz.size());

    }

    @Test
    void one() throws Exception {
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate inProgressStartedAt = LocalDate.of(1999, 12, 29);
        LocalDate to = LocalDate.of(2000, 1, 14);

        assert(inProgressStartedAt.toEpochDay() <= from.toEpochDay());
        assert(from.toEpochDay() <= to.toEpochDay());

        Job jobInprogress = new Job()
            .estimation(6)
            .startDate(inProgressStartedAt)
            .productCount(100).id(123L)
            .worknumber("job in")
            .addProduct(new Product().name("productName"));

        List<MachineDay> inProgressDays = new ArrayList<>();

        try {
            inProgressDays = from
                .datesUntil(jobInprogress.getStartDate().plusDays(jobInprogress.getEstimation()))
                .map( date -> new MachineDay(LocalDate.EPOCH, true, jobInprogress.getWorknumber(), jobInprogress.getId()))
                .collect(Collectors.toList());
        } catch ( IllegalArgumentException illegalArgumentException ) {
            System.out.println("nooooo");
        }


        Stream<OutOfOrder> streamOut = Stream.of(
            new OutOfOrder().start(LocalDate.of(2000, 1, 2)).end(LocalDate.of(2000, 1, 3)).description("desc"),
            new OutOfOrder().start(LocalDate.of(2000, 1, 10)).end(LocalDate.of(2000, 1, 12)).description("desc")
        );

        List<MachineDay> freeDays = from
            .datesUntil(to)
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

        freeDays.removeAll(occupied);

        Stream<Job> jobs = Stream.of(
            new Job().estimation(3).productCount(100).id(123L).worknumber("job 1").addProduct(new Product().name("productName")),
            new Job().estimation(2).productCount(100).id(123L).worknumber("job 2").addProduct(new Product().name("productName"))
        );

        // create MachineDay with invalid date
        List<MachineDay> jobMachineDayList = new ArrayList<>();

        List<MachineDay> remainingJobDays = jobs
            .flatMap(
                job ->
                    Stream
                        .generate(() -> new MachineDay(LocalDate.EPOCH, true, job.getWorknumber(), job.getId()))
                        .limit(job.getEstimation())
            )
            .collect(Collectors.toList());

        jobMachineDayList.addAll(inProgressDays);
        jobMachineDayList.addAll(remainingJobDays);

        List<MachineDay> all = new ArrayList<>();
        for (int i = 0; i < freeDays.size(); i++) {
            MachineDay jobMachineDay = null;
            MachineDay freeMachineDay = null;
            if (jobMachineDayList.size() > 0) {
                jobMachineDay = jobMachineDayList.remove(0);
                freeMachineDay = freeDays.remove(0);
            } else {
                break;
            }

            all.add(new MachineDay(freeMachineDay.getDate(), true, jobMachineDay.getComment(), jobMachineDay.getJobId()));
        }
        all.addAll(freeDays);
        all.addAll(occupied);
        all = all.stream().sorted().collect(Collectors.toList());
        System.out.println(all);
    }
}
