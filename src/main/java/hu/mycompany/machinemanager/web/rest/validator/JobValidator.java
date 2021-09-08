package hu.mycompany.machinemanager.web.rest.validator;

import hu.mycompany.machinemanager.repository.JobViewRepository;
import hu.mycompany.machinemanager.service.dto.JobDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class JobValidator implements org.springframework.validation.Validator {

    private final JobViewRepository jobViewRepository;

    public JobValidator(JobViewRepository jobViewRepository) {
        this.jobViewRepository = jobViewRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JobDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        JobDTO jobDTO = (JobDTO) o;
        //ValidationUtils.
        if (jobDTO.getStartDate() == null || jobDTO.getEndDate() == null) {
            return;
        }

        if (jobDTO.getStartDate().isAfter(jobDTO.getEndDate())) {
            errors.rejectValue("startDate", "START_DATE_AFTER_END_DATE", "A kezdő dátum a vég dátum után nem lehet");
            errors.rejectValue("endDate", "START_DATE_AFTER_END_DATE", "A kezdő dátum a vég dátum után nem lehet");
        }

        if (
            jobDTO.getMachine() != null &&
            jobDTO.getMachine().getId() != null &&
            jobViewRepository.findAllConflicts(jobDTO.getStartDate(), jobDTO.getEndDate(), jobDTO.getMachine().getId()).isEmpty()
        ) {
            errors.rejectValue("startDate", "CONFLICT_JOB", "Már létezik ütköző feladat");
            errors.rejectValue("endDate", "CONFLICT_JOB", "Már létezik ütköző feladat");
        }
    }
}
