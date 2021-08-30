package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.MachineDetailedDTO;
import hu.mycompany.machinemanager.service.mapper.JobWithoutDrawing;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import hu.mycompany.machinemanager.service.mapper.MachineDetailedMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
    private final MachineDetailedMapper machineDetailedMapper;

    public PerspectiveServiceImpl(MachineRepository machineRepository, MachineDetailedMapper machineDetailedMapper) {
        this.machineRepository = machineRepository;
        this.machineDetailedMapper = machineDetailedMapper;
    }

    @Override
    public Page<MachineDetailed> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAll(pageable).map(MachineDetailed::toDetailed);
    }

    @Override
    public List<MachineDetailed> findAllOpenInInterval(LocalDate startDate, LocalDate endDate) {
        Predicate<JobWithoutDrawing> isBetweenAndOpen = job ->
            job.getStartDate().isAfter(startDate) && job.getStartDate().isBefore(endDate) && job.getEndDate() == null;

        Function<MachineDetailed, MachineDetailed> mapMachine = machine ->
            MachineDetailed.createUsingJobWithoutDrawing(
                machine.getId(),
                machine.getName(),
                machine.getDescription(),
                machine.getJobs().stream().filter(isBetweenAndOpen).collect(Collectors.toSet())
            );
        Page<MachineDetailed> page = this.findAll(PageRequest.of(0, 1000));
        return page.getContent().stream().map(mapMachine).collect(Collectors.toList());
    }
}
