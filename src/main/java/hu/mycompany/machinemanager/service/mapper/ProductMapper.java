package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.ProductDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<ProductDTO> toDtoNameSet(Set<Product> product);
}
