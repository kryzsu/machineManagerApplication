package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, MachineMapper.class, CustomerMapper.class })
public interface JobMapper extends EntityMapper<JobDTO, Job> {
    @Mapping(target = "products", source = "products", qualifiedByName = "nameSet")
    @Mapping(target = "machine", source = "machine", qualifiedByName = "name")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "name")
    JobDTO toDto(Job s);

    @Mapping(target = "removeProduct", ignore = true)
    Job toEntity(JobDTO jobDTO);
}
