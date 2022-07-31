package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDTO}.
 */
@Mapper(componentModel = "spring", uses = { JobMapper.class })
public interface MachineMapper extends EntityMapper<MachineDTO, Machine> {
    @Mapping(target = "runningJob", source = "runningJob", qualifiedByName = "orderNumber")
    MachineDTO toDto(Machine s);

    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<MachineDTO> toDtoNameSet(Set<Machine> machine);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MachineDTO toDtoName(Machine machine);
}
