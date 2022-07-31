package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.RawmaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rawmaterial} and its DTO {@link RawmaterialDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RawmaterialMapper extends EntityMapper<RawmaterialDTO, Rawmaterial> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RawmaterialDTO toDtoName(Rawmaterial rawmaterial);
}
