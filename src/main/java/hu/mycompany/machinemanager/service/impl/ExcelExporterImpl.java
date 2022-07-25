package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.service.ExcelExporter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelExporterImpl implements ExcelExporter {

    private final Workbook workbook;

    public ExcelExporterImpl(Workbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public void createOrderConfirmation(Job job) {}

    @Override
    public ByteArrayOutputStream writeJobData(Job job) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);

        if (job == null) {
            throw new IllegalStateException("Job can not be null");
        }

        if (job.getCustomer() != null) {
            Row row = sheet.getRow(4);
            Cell cell = row.getCell(5);
            cell.setCellValue(job.getCustomer().getName());
        }

        if (job.getWorknumber() != null) {
            Row row = sheet.getRow(4);
            Cell cell = row.getCell(7);
            cell.setCellValue(job.getWorknumber());
        }

        if (job.getDrawingNumber() != null) {
            Row row = sheet.getRow(5);
            Cell cell = row.getCell(5);
            cell.setCellValue(job.getDrawingNumber());
        }

        if (job.getProductCount() != null) {
            Row row = sheet.getRow(5);
            Cell cell = row.getCell(7);
            cell.setCellValue(job.getProductCount().toString());
        }

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        workbook.write(bOutput);

        return bOutput;
        /*
        this.appendHeaderAndValue("Kiadva:", "xxx", row);
        this.appendHeaderAndValue("Megrendelő:", job.getCustomer().getName(), row);
        this.appendHeaderAndValue("Munkaszám:", job.getWorknumber(), row);

        this.appendHeaderAndValue("Határidő:", "xxx", row);
        this.appendHeaderAndValue("Rajzszám:", job.getDrawingNumber(), row);
        this.appendHeaderAndValue("Rendelt mennyiség:", job.getProductCount().toString(), row);
        */
    }
}
