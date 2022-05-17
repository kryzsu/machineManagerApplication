package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.*;
import hu.mycompany.machinemanager.service.util.Interval;
import hu.mycompany.machinemanager.service.util.Util;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PerspectiveServiceImpl implements PerspectiveService {

    private final Logger log = LoggerFactory.getLogger(PerspectiveServiceImpl.class);
    private final MachineRepository machineRepository;
    private final JobRepository jobRepository;
    private final OutOfOrderRepository outOfOrderRepository;

    private final MachineDetailedMapper machineDetailedMapper;
    private final Util util;
    private final OutOfOrderMapper outOfOrderMapper;

    public PerspectiveServiceImpl(
        MachineRepository machineRepository,
        MachineDetailedMapper machineDetailedMapper,
        Util util,
        OutOfOrderMapper outOfOrderMapper,
        JobRepository jobRepository,
        OutOfOrderRepository outOfOrderRepository
    ) {
        this.machineRepository = machineRepository;
        this.machineDetailedMapper = machineDetailedMapper;
        this.util = util;
        this.outOfOrderMapper = outOfOrderMapper;
        this.jobRepository = jobRepository;
        this.outOfOrderRepository = outOfOrderRepository;
    }

    @Override
    public Page<MachineDetailed> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAll(pageable).map(MachineDetailed::toDetailed);
    }

    @Override
    public List<MachineDetailed> findAllOpen() {
        Predicate<JobWithoutDrawing> isOpen = job -> job.getEndDate() == null;

        Function<MachineDetailed, MachineDetailed> mapMachine = machine ->
            MachineDetailed.createUsingJobWithoutDrawing(
                machine.getId(),
                machine.getName(),
                machine.getDescription(),
                machine.getJobs().stream().filter(isOpen).collect(Collectors.toCollection(LinkedHashSet::new))
            );
        Page<MachineDetailed> page = this.findAll(PageRequest.of(0, 1000));
        return page.getContent().stream().map(mapMachine).collect(Collectors.toList());
    }

    @Override
    public LocalDate getNextDateForMachine(long machineId, int estimation) {
        Stream<Interval> futureOccupiedIntervalStream = jobRepository
            .findByMachineIdAndStartDateGreaterThanEqual(machineId, LocalDate.now())
            .stream()
            .map(job -> new Interval(job.getStartDate(), util.getEndDateOrCalculate(job)));

        List<Interval> futureOccupiedIntervalList = Stream
            .concat(
                outOfOrderRepository
                    .findAllByMachineIdAndStartGreaterThanEqual(machineId, LocalDate.now())
                    .stream()
                    .map(out -> new Interval(out.getStart(), out.getEnd())),
                futureOccupiedIntervalStream
            )
            .collect(Collectors.toList());

        LocalDate tomorrow = LocalDateTime.now().plusDays(1).toLocalDate();

        List<Interval> freeIntervalList = util.getFreeIntervalList(futureOccupiedIntervalList, tomorrow);

        Interval firstInterval = util.findFirstSlot(freeIntervalList, estimation);

        return firstInterval.getStart();
    }

    @Override
    public List<OutOfOrderDTO> getRelatedOutOfOrder(long machineId) {
        return outOfOrderRepository
            .findAllByMachineIdAndStartGreaterThanEqual(machineId, LocalDate.now())
            .stream()
            .map(outOfOrderMapper::toDto)
            .collect(Collectors.toList());
    }
}
