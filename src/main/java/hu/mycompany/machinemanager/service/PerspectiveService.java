package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.MachineDetailed;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerspectiveService {
    Page<MachineDetailed> findAll(Pageable pageable);
    List<MachineDetailed> findAllOpen();
    LocalDate getNextDateForMachine(long machineId, int estimation);
    List<OutOfOrderDTO> getRelatedOutOfOrder(long machineId);

    List<MachineDayDTO> getJobNextDays(long machineId, long days);
}
