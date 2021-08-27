package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.MachineDetailedDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import hu.mycompany.machinemanager.service.mapper.MachineDetailedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
}
