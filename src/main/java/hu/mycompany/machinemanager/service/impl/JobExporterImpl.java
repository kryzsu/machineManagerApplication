package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.applicaion.port.in.JobExporterUseCase;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.service.ExcelExporterFactory;
import hu.mycompany.machinemanager.service.ExcelType;
import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JobExporterImpl implements JobExporterUseCase {

    private final JobRepository jobRepository;
    private final ExcelExporterFactory excelExporterFactory;

    public JobExporterImpl(JobRepository jobRepository, ExcelExporterFactory excelExporterFactory) {
        this.jobRepository = jobRepository;
        this.excelExporterFactory = excelExporterFactory;
    }

    @Override
    public byte[] getGyartasiLap(JobExporterCommand jobExporterCommand) throws IOException {
        Optional<Job> jobOptional = jobRepository.findById(jobExporterCommand.getJobId());
        return excelExporterFactory.create(ExcelType.GYARTASI_LAP).getGyartasiLap(jobOptional.get()).toByteArray();
    }

    @Override
    public byte[] getVisszaIgazolas(JobExporterCommand jobExporterCommand) throws IOException {
        Optional<Job> jobOptional = jobRepository.findById(jobExporterCommand.getJobId());
        return excelExporterFactory.create(ExcelType.VISSZA_IGAZOLAS).getVisszaIgazolas(jobOptional.get()).toByteArray();
    }
}
