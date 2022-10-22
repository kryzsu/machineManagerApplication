package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.domain.Job;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelExporter {
    void createOrderConfirmation(Job job);
    ByteArrayOutputStream getGyartasiLap(Job job) throws IOException;
    ByteArrayOutputStream getVisszaIgazolas(Job job) throws IOException;
    ExcelExporter setWorkbook(Workbook workbook);
}
