package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, MachineMapper.class, CustomerMapper.class })
public interface JobMapper extends EntityMapper<JobDTO, Job> {
    @Mapping(target = "product", source = "product", qualifiedByName = "name")
    @Mapping(target = "machine", source = "machine", qualifiedByName = "name")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "name")
    JobDTO toDto(Job s);

    @Named("orderNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderNumber", source = "orderNumber")
    JobDTO toDtoOrderNumber(Job job);
}
