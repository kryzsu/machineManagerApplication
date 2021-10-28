package hu.mycompany.machinemanager.service.util;

import java.time.Duration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Interval {

    private final LocalDate start;
    private final LocalDate end;
    private final long days;

    public static final String separator = "->";

    public Interval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
        days = Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays() + 1;
    }

    /**
     * a.fit(b) true, if a inside b or equals
     * @param other
     * @return
     */
    public boolean fit(Interval other) {
        return (start.equals(other.start) || start.isAfter(other.start)) && (end.equals(other.end) || end.isBefore(other.end));
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public long getDays() {
        return days;
    }

    @Override
    public String toString() {
        return String.format("Interval{%s %s %s, days=%d}", start, separator, end, days);
    }

    public static Interval of(String text) {
        String[] parts = text.split(separator);

        if (parts.length == 1) {
            throw new IllegalArgumentException("bad input format, no separator found");
        }
        return new Interval(LocalDate.parse(parts[0].trim()), LocalDate.parse(parts[1].trim()));
    }

    public boolean isValid() {
        return days > 0;
    }
}
