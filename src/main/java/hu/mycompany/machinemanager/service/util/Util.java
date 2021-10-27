package hu.mycompany.machinemanager.service.util;

import hu.mycompany.machinemanager.domain.Job;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class Util {

    public LocalDate getNextAvailableStartDate(Job job) {
        return (job.getEndDate() != null) ? job.getEndDate() : job.getStartDate().plusDays(job.getEstimation()).plusDays(1);
    }

    public Interval findFirstSlot(List<Interval> intervalList, long gapSize) {
        return intervalList.stream().filter(interval -> interval.getDays() >= gapSize).findFirst().get();
    }

    /**
     * The last item of the list is the future (interval_i, LocalDate.MAX)
     * @param futureOccupiedIntervalList
     * @param intervalStart
     * @return
     */
    public List<Interval> getFreeIntervalList(List<Interval> futureOccupiedIntervalList, LocalDate intervalStart) {
        BiConsumer<List<Interval>, Interval> addIntervalToListIfValid = (list, interval) -> {
            if (interval.isValid()) {
                list.add(interval);
            }
        };

        List<Interval> freeIntervalList = new ArrayList<>();

        futureOccupiedIntervalList.sort(new IntervalComparator());

        if (futureOccupiedIntervalList == null || futureOccupiedIntervalList.isEmpty()) {
            addIntervalToListIfValid.accept(freeIntervalList, new Interval(intervalStart, LocalDate.MAX));
            return freeIntervalList;
        }
        for (int i = 0; i < futureOccupiedIntervalList.size(); i++) {
            Interval actualOccupiedInterval = futureOccupiedIntervalList.get(i);

            boolean isFirstItem = i == 0;
            if (isFirstItem) {
                if (actualOccupiedInterval.getStart().isAfter(intervalStart)) {
                    addIntervalToListIfValid.accept(
                        freeIntervalList,
                        new Interval(intervalStart, actualOccupiedInterval.getStart().minusDays(1))
                    );
                }
            } else {
                Interval previousOccupiedInterval = futureOccupiedIntervalList.get(i - 1);
                addIntervalToListIfValid.accept(
                    freeIntervalList,
                    new Interval(previousOccupiedInterval.getEnd().plusDays(1), actualOccupiedInterval.getStart().minusDays(1))
                );
            }
            boolean isLastItem = i == futureOccupiedIntervalList.size() - 1;
            if (isLastItem) {
                freeIntervalList.add(new Interval(actualOccupiedInterval.getEnd().plusDays(1), LocalDate.MAX));
            }
        }

        return freeIntervalList;
    }

    public LocalDate getEndDateOrCalculate(Job job) {
        return job.getEndDate() != null ? job.getEndDate() : job.getStartDate().plusDays(job.getEstimation());
    }
}
