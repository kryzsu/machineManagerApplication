package hu.mycompany.machinemanager.service.mapper;

import hu.mycompany.machinemanager.domain.*;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, MachineMapper.class })
public interface JobMapper extends EntityMapper<JobDTO, Job> {
    @Mapping(source = "machine.id", target = "machineId")
    @Mapping(source = "machine.name", target = "machineName")
    JobDTO toDto(Job job);

    @Mapping(target = "removeProduct", ignore = true)
    @Mapping(source = "machineId", target = "machine")
    Job toEntity(JobDTO jobDTO);

    default Job fromId(Long id) {
        if (id == null) {
            return null;
        }
        Job job = new Job();
        job.setId(id);
        return job;
    }
}
