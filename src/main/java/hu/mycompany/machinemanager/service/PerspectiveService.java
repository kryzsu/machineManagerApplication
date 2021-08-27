package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.MachineDetailedDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerspectiveService {
    Page<MachineDetailed> findAll(Pageable pageable);
}
