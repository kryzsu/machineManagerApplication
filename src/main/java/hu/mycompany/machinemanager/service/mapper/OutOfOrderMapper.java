package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutOfOrder} and its DTO {@link OutOfOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OutOfOrderMapper extends EntityMapper<OutOfOrderDTO, OutOfOrder> {
    default OutOfOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        OutOfOrder outOfOrder = new OutOfOrder();
        outOfOrder.setId(id);
        return outOfOrder;
    }
}
