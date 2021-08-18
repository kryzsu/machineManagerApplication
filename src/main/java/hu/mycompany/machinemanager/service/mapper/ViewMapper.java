package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link View} and its DTO {@link ViewDTO}.
 */
@Mapper(componentModel = "spring", uses = { MachineMapper.class })
public interface ViewMapper extends EntityMapper<ViewDTO, View> {
    @Mapping(target = "removeMachine", ignore = true)
    default View fromId(Long id) {
        if (id == null) {
            return null;
        }
        View view = new View();
        view.setId(id);
        return view;
    }
}
