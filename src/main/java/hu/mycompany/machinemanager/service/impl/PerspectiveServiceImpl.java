package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.*;
import hu.mycompany.machinemanager.service.util.Util;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private final MachineDetailedMapper machineDetailedMapper;
    private final Util util;
    private final OutOfOrderMapper outOfOrderMapper;

    public PerspectiveServiceImpl(
        MachineRepository machineRepository,
        MachineDetailedMapper machineDetailedMapper,
        Util util,
        OutOfOrderMapper outOfOrderMapper
    ) {
        this.machineRepository = machineRepository;
        this.machineDetailedMapper = machineDetailedMapper;
        this.util = util;
        this.outOfOrderMapper = outOfOrderMapper;
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

    @Override
    public LocalDate getNextDateForMachine(long machineId) {
        Optional<Machine> machineOpt = machineRepository.findById(machineId);

        LocalDate defaultDate = LocalDate.now();
        if (machineOpt.isPresent()) {
            return Stream
                .concat(machineOpt.get().getJobs().stream().map(util::getNextAvailableStartDate), Stream.of(defaultDate))
                .max((a, b) -> a.compareTo(b))
                .get();
        } else {
            return defaultDate;
        }
    }

    @Override
    public List<OutOfOrderDTO> getRelatedOutOfOrder(long machineId) {
        OutOfOrderDTO[] rv = {};
        // TODO Auto-generated method stub
        return machineRepository
            .findById(machineId)
            .orElse(new Machine())
            .getOutOfOrders()
            .stream()
            .map(outOfOrderMapper::toDto)
            .collect(Collectors.toList());
    }
}
