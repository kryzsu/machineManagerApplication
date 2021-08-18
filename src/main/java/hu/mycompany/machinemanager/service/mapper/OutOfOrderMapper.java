package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutOfOrder} and its DTO {@link OutOfOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { MachineMapper.class })
public interface OutOfOrderMapper extends EntityMapper<OutOfOrderDTO, OutOfOrder> {
    @Mapping(target = "machines", source = "machines", qualifiedByName = "nameSet")
    OutOfOrderDTO toDto(OutOfOrder s);

    @Mapping(target = "removeMachine", ignore = true)
    OutOfOrder toEntity(OutOfOrderDTO outOfOrderDTO);
}
