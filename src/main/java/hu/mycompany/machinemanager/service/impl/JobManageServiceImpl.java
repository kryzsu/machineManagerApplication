package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.applicaion.port.in.JobInformationUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.JobManageUseCase;
import hu.mycompany.machinemanager.applicaion.port.in.MachineJobCommand;
import hu.mycompany.machinemanager.domain.Calendar;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.CalendarRepository;
import hu.mycompany.machinemanager.repository.JobBimRepository;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.AnotherJobIsAlreadyRunningException;
import hu.mycompany.machinemanager.service.NoRunningJobException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class JobManageServiceImpl implements JobManageUseCase {

    private final JobBimRepository jobRepository;
    private final MachineRepository machineRepository;
    private final JobInformationUseCase jobInformationUseCase;
    private final CalendarRepository calendarRepository;

    public JobManageServiceImpl(
        JobBimRepository jobRepository,
        MachineRepository machineRepository,
        JobInformationUseCase jobInformationUseCase,
        CalendarRepository calendarRepository
    ) {
        this.jobRepository = jobRepository;
        this.machineRepository = machineRepository;
        this.jobInformationUseCase = jobInformationUseCase;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public void startNextJob(MachineJobCommand machineJobCommand) {
        Optional<Machine> machineById = machineRepository.findById(machineJobCommand.getMachineId());

        assertValidMachine(machineById);

        Machine machine = machineById.get();

        assertNoRunningJobOnMachine(machine);

        List<Job> openJobs = jobRepository.getOpenJobsForMachine(machineJobCommand.getMachineId());

        if (openJobs.isEmpty()) {
            throw new NoFurtherJobsForMachine();
        }

        LocalDate startDate = LocalDate.now();
        Job job = openJobs.get(0);
        job.startDate(startDate);
        Calendar calendar = calendarRepository.findByDay(startDate);
        job.getCalendars().add(calendar);
        machine.setRunningJob(job);
        jobRepository.save(job);
        machineRepository.save(machine);
    }

    private void assertValidMachine(Optional<Machine> machineById) {
        if (machineById.isEmpty() || machineById.get() == null) {
            throw new NoSuchEntity();
        }
    }

    private void assertNoRunningJobOnMachine(Machine machine) {
        if (machine.getRunningJob() != null) {
            throw new AnotherJobIsAlreadyRunningException(machine.getId());
        }
    }

    private void assertRunningJobOnMachine(Machine machine) {
        if (machine.getRunningJob() == null) {
            throw new NoRunningJobException(machine.getId());
        }
    }

    @Override
    public void stopRunningJob(MachineJobCommand machineJobCommand) {
        Optional<Machine> machineById = machineRepository.findById(machineJobCommand.getMachineId());
        assertValidMachine(machineById);

        Machine machine = machineById.get();

        assertRunningJobOnMachine(machine);

        Job job = machine.getRunningJob();

        LocalDate startDate = job.getStartDate();
        LocalDate endDate = LocalDate.now();

        job.endDate(endDate);
        job.fact(Period.between(startDate, endDate).getDays());
        Set<Calendar> calendarList = calendarRepository.findAllByDayBetween(startDate, endDate);
        // job.getCalendars().addAll(calendarList);
        jobRepository.save(job);
        machine.setRunningJob(null);
        machineRepository.save(machine);
        calendarRepository.saveAll(calendarList);
    }

    @Override
    public void savePriorities(SavePrioritiesCommand savePrioritiesCommand) {
        List<Job> jobList = savePrioritiesCommand
            .getJobIdWithPriorityList()
            .stream()
            .map(jobIdWithPriority -> jobRepository.findById(jobIdWithPriority.getJobId()).get().priority(jobIdWithPriority.getPriority()))
            .collect(Collectors.toList());

        jobRepository.saveAll(jobList);
    }
}
