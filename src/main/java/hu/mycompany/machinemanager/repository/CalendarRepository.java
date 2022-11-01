package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Calendar;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Calendar findByDay(LocalDate date);
    Set<Calendar> findAllByDayBetween(LocalDate from, LocalDate to);
}
