package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {
    @Mapping(target = "outOfOrders", ignore = true)
    @Mapping(target = "removeOutOfOrder", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "removeJob", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "removeView", ignore = true)
    Machine toEntity(MachineDTO machineDTO);

    default Machine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Machine machine = new Machine();
        machine.setId(id);
        return machine;
    }
}
