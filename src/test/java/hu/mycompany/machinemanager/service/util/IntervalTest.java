package hu.mycompany.machinemanager.service.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IntervalTest {

    @Test
    void happyPathTest() {
        // GIVEN
        long days = 3L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = LocalDateTime.now().plusDays(days);

        // WHEN
        Interval interval = new Interval(now.toLocalDate(), later.toLocalDate());

        //THEN
        assertThat(interval.getDays(), equalTo(days + 1));
    }

    @Test
    void fit_aEqualsbTest() {
        // GIVEN
        Interval a = new Interval(LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1));
        Interval b = new Interval(LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1));

        // WHEN THEN
        assertTrue(a.fit(b));
    }

    @Test
    void fit_aInBTest() {
        // GIVEN
        Interval a = new Interval(LocalDate.of(2000, 2, 1), LocalDate.of(2001, 1, 1));
        Interval b = new Interval(LocalDate.of(2000, 1, 1), LocalDate.of(2001, 2, 1));

        // WHEN THEN
        assertTrue(a.fit(b));
    }

    @Test
    void fit_aOutBTest() {
        // GIVEN
        Interval a = new Interval(LocalDate.of(2000, 2, 1), LocalDate.of(2001, 1, 1));
        Interval b = new Interval(LocalDate.of(2002, 1, 1), LocalDate.of(2003, 2, 1));

        // WHEN THEN
        assertFalse(a.fit(b));
    }

    @Test
    void fit_aIntersectBTest() {
        // GIVEN
        Interval a = new Interval(LocalDate.of(2000, 1, 1), LocalDate.of(2002, 1, 1));
        Interval b = new Interval(LocalDate.of(2001, 1, 1), LocalDate.of(2003, 1, 1));

        // WHEN THEN
        assertFalse(a.fit(b));
    }

    @Test
    void ofValid() {
        // GIVEN
        String goodInput = "2000-01-01->2001-01-01";

        // WHEN
        Interval rv = Interval.of(goodInput);

        // THEN
        assertThat(rv.getStart(), equalTo(LocalDate.of(2000, 1, 1)));
        assertThat(rv.getEnd(), equalTo(LocalDate.of(2001, 1, 1)));
    }

    @Test
    void ofInvalid() {
        // GIVEN
        String badInput = "2000-01-01 2001-01-01";

        // WHEN THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.of(badInput));
    }
}
