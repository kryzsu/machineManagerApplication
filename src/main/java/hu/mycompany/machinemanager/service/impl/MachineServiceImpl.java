package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.repository.MachineRepository;
import hu.mycompany.machinemanager.service.MachineService;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import hu.mycompany.machinemanager.service.mapper.MachineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Machine}.
 */
@Service
@Transactional
public class MachineServiceImpl implements MachineService {
    private final Logger log = LoggerFactory.getLogger(MachineServiceImpl.class);

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    public MachineServiceImpl(MachineRepository machineRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    @Override
    public MachineDTO save(MachineDTO machineDTO) {
        log.debug("Request to save Machine : {}", machineDTO);
        Machine machine = machineMapper.toEntity(machineDTO);
        machine = machineRepository.save(machine);
        return machineMapper.toDto(machine);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MachineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAll(pageable).map(machineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MachineDTO> findOne(Long id) {
        log.debug("Request to get Machine : {}", id);
        return machineRepository.findById(id).map(machineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Machine : {}", id);
        machineRepository.deleteById(id);
    }
}
