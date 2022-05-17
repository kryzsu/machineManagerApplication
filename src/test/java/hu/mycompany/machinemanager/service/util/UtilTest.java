package hu.mycompany.machinemanager.service.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void getFreeIntervalList_withEmptyTest() {
        // GIVEN
        List<Interval> futureOccupiedIntervalList = new ArrayList<>();
        LocalDate intervalStart = LocalDate.of(2000, 1, 1);

        // WHEN
        List<Interval> freeIntervalList = new Util().getFreeIntervalList(futureOccupiedIntervalList, intervalStart);
        // THEN

        assertThat(freeIntervalList.get(0).getStart(), equalTo(intervalStart));
        assertThat(freeIntervalList.get(0).getEnd(), equalTo(LocalDate.MAX));
    }

    @Test
    void getFreeIntervalList_withOneIntervalTest() {
        // GIVEN
        List<Interval> futureOccupiedIntervalList = new ArrayList<>();
        LocalDate intervalStart = LocalDate.of(2000, 1, 1);
        LocalDate firstIntervalStart = intervalStart.plusDays(5);
        LocalDate firstIntervalEnd = intervalStart.plusDays(10);

        futureOccupiedIntervalList.add(new Interval(firstIntervalStart, firstIntervalEnd));

        // WHEN
        List<Interval> freeIntervalList = new Util().getFreeIntervalList(futureOccupiedIntervalList, intervalStart);
        // THEN

        assertThat(freeIntervalList.get(0).getStart(), equalTo(intervalStart));
        assertThat(freeIntervalList.get(0).getEnd(), equalTo(firstIntervalStart.minusDays(1)));

        assertThat(freeIntervalList.get(1).getStart(), equalTo(firstIntervalEnd.plusDays(1)));
        assertThat(freeIntervalList.get(1).getEnd(), equalTo(LocalDate.MAX));
    }

    @Test
    void getFreeIntervalList_withTwoIntervalsTest() {
        // GIVEN
        List<Interval> futureOccupiedIntervalList = new ArrayList<>();
        LocalDate intervalStart = LocalDate.of(2000, 1, 1);
        LocalDate firstIntervalStart = intervalStart.plusDays(5);
        LocalDate firstIntervalEnd = intervalStart.plusDays(10);
        futureOccupiedIntervalList.add(new Interval(firstIntervalStart, firstIntervalEnd));

        LocalDate secondIntervalStart = intervalStart.plusDays(15);
        LocalDate secondIntervalEnd = intervalStart.plusDays(20);
        futureOccupiedIntervalList.add(new Interval(secondIntervalStart, secondIntervalEnd));

        // WHEN
        List<Interval> freeIntervalList = new Util().getFreeIntervalList(futureOccupiedIntervalList, intervalStart);

        // THEN
        assertThat(freeIntervalList.get(0).getStart(), equalTo(intervalStart));
        assertThat(freeIntervalList.get(0).getEnd(), equalTo(firstIntervalStart.minusDays(1)));

        assertThat(freeIntervalList.get(1).getStart(), equalTo(firstIntervalEnd.plusDays(1)));
        assertThat(freeIntervalList.get(1).getEnd(), equalTo(secondIntervalStart.minusDays(1)));

        assertThat(freeIntervalList.get(2).getStart(), equalTo(secondIntervalEnd.plusDays(1)));
        assertThat(freeIntervalList.get(2).getEnd(), equalTo(LocalDate.MAX));
    }

    @Test
    void getFreeIntervalList_withTwoIntervals2Test() {
        // GIVEN
        List<Interval> futureOccupiedIntervalList = new ArrayList<>();
        LocalDate intervalStart = LocalDate.of(2000, 1, 1);
        LocalDate firstIntervalStart = intervalStart;
        LocalDate firstIntervalEnd = intervalStart.plusDays(10);
        futureOccupiedIntervalList.add(new Interval(firstIntervalStart, firstIntervalEnd));

        LocalDate secondIntervalStart = intervalStart.plusDays(15);
        LocalDate secondIntervalEnd = intervalStart.plusDays(20);
        futureOccupiedIntervalList.add(new Interval(secondIntervalStart, secondIntervalEnd));

        // WHEN
        List<Interval> freeIntervalList = new Util().getFreeIntervalList(futureOccupiedIntervalList, intervalStart);

        // THEN
        assertThat(freeIntervalList.get(0).getStart(), equalTo(firstIntervalEnd.plusDays(1)));
        assertThat(freeIntervalList.get(0).getEnd(), equalTo(secondIntervalStart.minusDays(1)));

        assertThat(freeIntervalList.get(1).getStart(), equalTo(secondIntervalEnd.plusDays(1)));
        assertThat(freeIntervalList.get(1).getEnd(), equalTo(LocalDate.MAX));
    }
}
