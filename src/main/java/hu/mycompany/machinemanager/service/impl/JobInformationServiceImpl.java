package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.applicaion.port.in.JobInformationUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.JobManageUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobCommand;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobPlanningCommand;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobBimRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.repository.OutOfOrderBimRepository;
import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.JobWithoutDrawing;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapper;
import hu.mycompany.machinemanager.service.util.Interval;
import hu.mycompany.machinemanager.service.util.JobUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobInformationServiceImpl implements JobInformationUseCase {

    private final JobBimRepository jobRepository;
    private final OutOfOrderBimRepository outOfOrderRepository;
    private final JobUtil jobUtil;
    private final OutOfOrderMapper outOfOrderMapper;
    private final MachineRepository machineRepository;

    private Predicate<JobWithoutDrawing> isOpen = job -> job.getEndDate() == null;

    public JobInformationServiceImpl(
        JobBimRepository jobRepository,
        OutOfOrderBimRepository outOfOrderRepository,
        JobUtil jobUtil,
        OutOfOrderMapper outOfOrderMapper,
        MachineRepository machineRepository
    ) {
        this.jobRepository = jobRepository;
        this.outOfOrderRepository = outOfOrderRepository;
        this.jobUtil = jobUtil;
        this.outOfOrderMapper = outOfOrderMapper;
        this.machineRepository = machineRepository;
    }

    @Override
    public Page<MachineDetailed> findAll(Pageable pageable) {
        return machineRepository.findAll(pageable).map(MachineDetailed::toDetailed);
    }

    @Override
    public LocalDate getNextDateForMachine(MachineJobPlanningCommand machineJobPlanningCommand) {
        Stream<Interval> futureOccupiedIntervalStream = jobRepository
            .findByMachineIdAndStartDateGreaterThanEqual(machineJobPlanningCommand.getMachineId(), LocalDate.now())
            .stream()
            .map(job -> new Interval(job.getStartDate(), jobUtil.getEndDateOrCalculate(job)));

        List<Interval> futureOccupiedIntervalList = Stream
            .concat(
                outOfOrderRepository
                    .findAllByMachineIdAndStartGreaterThanEqual(machineJobPlanningCommand.getMachineId(), LocalDate.now())
                    .stream()
                    .map(out -> new Interval(out.getStart(), out.getEnd())),
                futureOccupiedIntervalStream
            )
            .collect(Collectors.toList());

        LocalDate tomorrow = LocalDateTime.now().plusDays(1).toLocalDate();

        List<Interval> freeIntervalList = jobUtil.getFreeIntervalList(futureOccupiedIntervalList, tomorrow);

        Interval firstInterval = jobUtil.findFirstSlot(freeIntervalList, machineJobPlanningCommand.getEstimation());

        return firstInterval.getStart();
    }

    @Override
    public List<MachineDetailed> findAllOpen() {
        Function<MachineDetailed, MachineDetailed> mapMachine = machine ->
            MachineDetailed.createUsingJobWithoutDrawing(
                machine.getId(),
                machine.getName(),
                machine.getDescription(),
                machine.getRunningJob()
            );
        Page<MachineDetailed> page = this.findAll(PageRequest.of(0, 1000));
        return page.getContent().stream().map(mapMachine).collect(Collectors.toList());
    }

    @Override
    public List<MachineDayDTO> getJobNextDays(GetJobNextDaysCommand getJobNextDaysCommand) {
        List<OutOfOrderDTO> outOfOrderDTOList = getRelatedOutOfOrderForInterval(
            getJobNextDaysCommand.getMachineId(),
            LocalDate.now(),
            LocalDate.now().plusDays(getJobNextDaysCommand.getDays())
        );

        List<MachineDayDTO> outUfOrderDays = getOutUfOrderDays(
            outOfOrderDTOList,
            LocalDate.now(),
            LocalDate.now().plusDays(getJobNextDaysCommand.getDays())
        );
        List<MachineDayDTO> free = getDaysByInterval(LocalDate.now(), LocalDate.now().plusDays(getJobNextDaysCommand.getDays()));
        List<MachineDayDTO> runningJobDays = getRunningJobDays(getJobNextDaysCommand.getMachineId());
        free.removeAll(outUfOrderDays);
        free.removeAll(runningJobDays);
        List<MachineDayDTO> jobMachineDayList = getRelatedFurtherJobs(getJobNextDaysCommand.getMachineId())
            .stream()
            .flatMap(
                job ->
                    Stream
                        .generate(() -> new MachineDayDTO(LocalDate.EPOCH, true, job.getWorknumber(), job.getId(), 0))
                        .limit(job.getEstimation())
            )
            .collect(Collectors.toList());

        List<MachineDayDTO> all = new ArrayList<>();
        int remainingWorkingDayNumber = free.size();
        for (int i = 0; i < remainingWorkingDayNumber; i++) {
            MachineDayDTO jobMachineDay;
            MachineDayDTO freeMachineDay;
            if (!jobMachineDayList.isEmpty()) {
                jobMachineDay = jobMachineDayList.remove(0);
                freeMachineDay = free.remove(0);
            } else {
                break;
            }

            all.add(
                new MachineDayDTO(
                    freeMachineDay.getDate(),
                    true,
                    jobMachineDay.getComment(),
                    jobMachineDay.getJobId(),
                    freeMachineDay.getDate().getDayOfWeek().getValue()
                )
            );
        }
        all.addAll(free);
        all.addAll(outUfOrderDays);
        all.addAll(runningJobDays);
        all = all.stream().sorted().collect(Collectors.toList());
        return all;
    }

    private List<OutOfOrderDTO> getRelatedOutOfOrderForInterval(Long machineId, LocalDate from, LocalDate to) {
        return outOfOrderRepository
            .findAllByMachineIdAndStartGreaterThanEqualAndEndLessThanEqual(machineId, from, to)
            .stream()
            .map(outOfOrderMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<OutOfOrderDTO> getRelatedOutOfOrder(MachineJobCommand machineJobCommand) {
        return outOfOrderRepository
            .findAllByMachineIdAndStartGreaterThanEqual(machineJobCommand.getMachineId(), LocalDate.now())
            .stream()
            .map(outOfOrderMapper::toDto)
            .collect(Collectors.toList());
    }

    private List<MachineDayDTO> getOutUfOrderDays(List<OutOfOrderDTO> outOfOrderDTOList, LocalDate fromDate, LocalDate endDate) {
        return outOfOrderDTOList
            .stream()
            .flatMap(
                outOfOrder ->
                    outOfOrder
                        .getStart()
                        .datesUntil(outOfOrder.getEnd().plusDays(1))
                        .filter(
                            date -> (date.isEqual(fromDate) || date.isAfter(fromDate)) && (date.isEqual(endDate) || date.isBefore(endDate))
                        )
                        .map(date -> new MachineDayDTO(date, true, outOfOrder.getDescription(), null, date.getDayOfWeek().getValue()))
            )
            .collect(Collectors.toList());
    }

    private List<Job> getRelatedFurtherJobs(long machineId) {
        return jobRepository.findByMachineIdAndStartDateIsNullOrderByPriorityDescIdDesc(machineId);
    }

    private List<MachineDayDTO> getDaysByInterval(LocalDate from, LocalDate to) {
        return from
            .datesUntil(to)
            .map(date -> new MachineDayDTO(date, false, "free", null, date.getDayOfWeek().getValue()))
            .collect(Collectors.toList());
    }

    private List<MachineDayDTO> getRunningJobDays(long machineId) {
        Optional<Job> runningJob = jobRepository.findByMachineIdAndStartDateIsNotNullAndEndDateIsNull(machineId);
        return runningJob
            .map(
                job ->
                    LocalDate
                        .now()
                        .datesUntil(job.getStartDate().plusDays(job.getEstimation()))
                        .map(date -> new MachineDayDTO(date, true, job.getWorknumber(), job.getId(), date.getDayOfWeek().getValue()))
            )
            .orElse(Stream.empty())
            .collect(Collectors.toList());
    }
}
