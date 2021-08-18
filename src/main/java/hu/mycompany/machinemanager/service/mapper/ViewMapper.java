package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link View} and its DTO {@link ViewDTO}.
 */
@Mapper(componentModel = "spring", uses = { MachineMapper.class })
public interface ViewMapper extends EntityMapper<ViewDTO, View> {
    @Mapping(target = "machines", source = "machines", qualifiedByName = "nameSet")
    ViewDTO toDto(View s);

    @Mapping(target = "removeMachine", ignore = true)
    View toEntity(ViewDTO viewDTO);
}
