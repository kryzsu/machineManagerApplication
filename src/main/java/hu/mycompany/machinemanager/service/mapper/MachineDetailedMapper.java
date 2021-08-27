package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.Machine;
import hu.mycompany.machinemanager.service.dto.MachineDTO;
import hu.mycompany.machinemanager.service.dto.MachineDetailedDTO;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Machine} and its DTO {@link MachineDetailedDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MachineDetailedMapper extends EntityMapper<MachineDetailedDTO, MachineDetailed> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MachineDTO toDtoName(Machine machine);

    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<MachineDTO> toDtoNameSet(Set<Machine> machine);
}
