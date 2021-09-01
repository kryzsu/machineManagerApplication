package hu.mycompany.machinemanager.service.util;

import hu.mycompany.machinemanager.domain.Job;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class Util {

    public LocalDate getNextAvailableStartDate(Job job) {
        return (job.getEndDate() != null) ? job.getEndDate() : job.getStartDate().plusDays(job.getEstimation()).plusDays(1);
    }
}
