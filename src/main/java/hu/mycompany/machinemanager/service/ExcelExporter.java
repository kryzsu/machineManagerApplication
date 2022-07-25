package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.domain.Job;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface ExcelExporter {
    void createOrderConfirmation(Job job);
    ByteArrayOutputStream writeJobData(Job job) throws IOException;
}
