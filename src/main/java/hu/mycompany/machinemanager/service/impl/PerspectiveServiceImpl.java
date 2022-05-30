package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.AnotherJobIsAlreadyRunningException;
import hu.mycompany.machinemanager.service.NoRunningJobException;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.*;
import hu.mycompany.machinemanager.service.util.Interval;
import hu.mycompany.machinemanager.service.util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    private final Util util;
    private final OutOfOrderMapper outOfOrderMapper;
    Predicate<JobWithoutDrawing> isOpen = job -> job.getEndDate() == null;

    public PerspectiveServiceImpl(
        MachineRepository machineRepository,
        Util util,
        OutOfOrderMapper outOfOrderMapper,
        JobRepository jobRepository,
        OutOfOrderRepository outOfOrderRepository
    ) {
        this.machineRepository = machineRepository;
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
        Function<MachineDetailed, MachineDetailed> mapMachine = machine ->
            MachineDetailed.createUsingJobWithoutDrawing(
                machine.getId(),
                machine.getName(),
                machine.getDescription(),
                machine
                    .getJobs()
                    .stream()
                    .filter(this.isOpen)
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toCollection(LinkedHashSet::new)),
                machine.getRunningJob()
            );
        Page<MachineDetailed> page = this.findAll(PageRequest.of(0, 1000));
        return page.getContent().stream().map(mapMachine).collect(Collectors.toList());
    }

    private List<MachineDayDTO> getRunningJobDays(long machineId) {
        Optional<Job> runningJob = jobRepository.findByMachineIdAndStartDateIsNotNullAndEndDateIsNull(machineId);
        return runningJob
            .map(
                job ->
                    LocalDate
                        .now()
                        .datesUntil(job.getStartDate().plusDays(job.getEstimation()))
                        .map(date -> new MachineDayDTO(date, true, job.getWorknumber(), job.getId(),
                            date.getDayOfWeek().getValue()))
            )
            .orElse(Stream.empty())
            .collect(Collectors.toList());
    }

    private List<Job> getRelatedFurtherJobs(long machineId) {
        return jobRepository.findByMachineIdAndStartDateIsNullOrderByPriorityDescCreateDateTimeDesc(machineId);
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

    @Override
    public List<MachineDayDTO> getJobNextDays(long machineId, long days) {
        List<OutOfOrderDTO> outOfOrderDTOList = getRelatedOutOfOrder(machineId);
        List<MachineDayDTO> outUfOrderDays = getOutUfOrderDays(outOfOrderDTOList, LocalDate.now(), LocalDate.now().plusDays(days));
        List<MachineDayDTO> free = getDaysByInterval(LocalDate.now(), LocalDate.now().plusDays(days));
        List<MachineDayDTO> runningJobDays = getRunningJobDays(machineId);
        free.removeAll(outUfOrderDays);
        free.removeAll(runningJobDays);
        List<MachineDayDTO> jobMachineDayList = getRelatedFurtherJobs(machineId)
            .stream()
            .flatMap(
                job ->
                    Stream
                        .generate(() -> new MachineDayDTO(LocalDate.EPOCH, true, job.getWorknumber(), job.getId(), 0))
                        .limit(job.getEstimation())
            )
            .collect(Collectors.toList());

        List<MachineDayDTO> all = new ArrayList<>();
        for (int i = 0; i < free.size(); i++) {
            MachineDayDTO jobMachineDay;
            MachineDayDTO freeMachineDay;
            if (jobMachineDayList.size() > 0) {
                jobMachineDay = jobMachineDayList.remove(0);
                freeMachineDay = free.remove(0);
            } else {
                break;
            }

            all.add(new MachineDayDTO(freeMachineDay.getDate(), true, jobMachineDay.getComment(),
                jobMachineDay.getJobId(), freeMachineDay.getDate().getDayOfWeek().getValue()));
        }
        all.addAll(free);
        all.addAll(outUfOrderDays);
        all.addAll(runningJobDays);
        all = all.stream().sorted().collect(Collectors.toList());
        return all;
    }

    @Override
    public void startNextJob(long machineId) {
        Optional<Machine> machineById = machineRepository.findById(machineId);

        if (machineById.isPresent()) {
            Machine machine = machineById.get();
            if (machine.getRunningJob() != null) {
                throw new AnotherJobIsAlreadyRunningException(machineId);
            }
            Optional<Job> nextJob = jobRepository.findTopByMachineIdAndStartDateIsNullOrderByPriorityDescCreateDateTimeDesc(machineId);

            if (nextJob.isPresent()) {
                Job job = nextJob.get();
                job.startDate(LocalDate.now());
                machine.setRunningJob(job);
                jobRepository.save(job);
                machineRepository.save(machine);
            } else {
                throw new NoFurtherJobsForMachine();
            }
        } else {
            throw new NoSuchEntity();
        }
    }

    @Override
    public void stopRunningJob(long machineId) {
        Optional<Machine> machineById = machineRepository.findById(machineId);

        if (machineById.isPresent()) {
            Machine machine = machineById.get();
            Job job = machine.getRunningJob();
            if (job == null) {
                throw new NoRunningJobException(machineId);
            }
            job.endDate(LocalDate.now());
            job.fact(Period.between(job.getStartDate(), LocalDate.now()).getDays());
            jobRepository.save(job);
            machine.setRunningJob(null);
            machineRepository.save(machine);
        } else {
            throw new NoSuchEntity();
        }
    }


    private List<MachineDayDTO> getDaysByInterval(LocalDate from, LocalDate to) {
        return from.datesUntil(to).map(date -> new MachineDayDTO(date, false, "free", null,
            date.getDayOfWeek().getValue())).collect(Collectors.toList());
    }

    private List<MachineDayDTO> getOutUfOrderDays(List<OutOfOrderDTO> outOfOrderDTOList, LocalDate now, LocalDate endDate) {
        return outOfOrderDTOList
            .stream()
            .flatMap(
                outOfOrder ->
                    outOfOrder
                        .getStart()
                        .datesUntil(outOfOrder.getEnd().plusDays(1))
                        .filter( date ->
                            (date.isEqual(now) || date.isAfter(now)) && (date.isEqual(endDate) || date.isBefore(endDate)))
                        .map(date -> new MachineDayDTO(date, true, outOfOrder.getDescription(), null,
                            date.getDayOfWeek().getValue()))
            )
            .collect(Collectors.toList());
    }


}
