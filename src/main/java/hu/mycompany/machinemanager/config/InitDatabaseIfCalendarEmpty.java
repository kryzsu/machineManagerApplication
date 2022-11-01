package hu.mycompany.machinemanager.config;

import hu.mycompany.machinemanager.domain.Calendar;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.CalendarRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class InitDatabaseIfCalendarEmpty {

    private final CalendarRepository calendarRepository;
    private final OutOfOrderRepository outOfOrderRepository;
    private final MachineRepository machineRepository;
    public final int nextYears = 20;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InitDatabaseIfCalendarEmpty.class);

    public InitDatabaseIfCalendarEmpty(
        CalendarRepository calendarRepository,
        OutOfOrderRepository outOfOrderRepository,
        MachineRepository machineRepository
    ) {
        this.calendarRepository = calendarRepository;
        this.outOfOrderRepository = outOfOrderRepository;
        this.machineRepository = machineRepository;
    }

    @PostConstruct
    public void init() {
        if (calendarRepository.count() != 0) {
            return;
        }
        log.debug("InitCalendarIfEmpty::init");

        insertDataToCalendarForNextYears();
        insertWeekendsForNextYear();
    }

    private void insertDataToCalendarForNextYears() {
        List<Calendar> calendarList = LocalDate
            .now()
            .datesUntil(LocalDate.now().plusYears(nextYears))
            .map(date -> new Calendar(date))
            .collect(Collectors.toList());

        calendarRepository.saveAll(calendarList);
    }

    private void insertWeekendsForNextYear() {
        Predicate<LocalDate> isSaturday = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY;
        List<OutOfOrder> outOfOrderList = LocalDate
            .now()
            .datesUntil(LocalDate.now().plusYears(nextYears))
            .filter(isSaturday)
            .map(this::createWeekendOutOfOrder)
            .collect(Collectors.toList());

        outOfOrderRepository.saveAll(outOfOrderList);
    }

    private OutOfOrder createWeekendOutOfOrder(final LocalDate start) {
        final LocalDate end = start.plusDays(1);
        List<Machine> allMachineList = machineRepository.findAll();
        OutOfOrder rv = new OutOfOrder();
        rv.setDescription("Weekend");
        rv.setMachines(new HashSet<>(allMachineList));
        rv.setStart(start);
        rv.setEnd(end);

        Set<Calendar> allByDayBetween = calendarRepository
            .findAllByDayBetween(start, end)
            .stream()
            .map(
                calendar -> {
                    calendar.getOutOfOrders().add(rv);
                    return calendar;
                }
            )
            .collect(Collectors.toSet());

        rv.getCalendars().addAll(allByDayBetween);
        return rv;
    }
}
