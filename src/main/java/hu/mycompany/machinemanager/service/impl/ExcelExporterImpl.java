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

    private Workbook workbook;

    @Override
    public void createOrderConfirmation(Job job) {}

    public ExcelExporter setWorkbook(Workbook workbook) {
        this.workbook = workbook;

        return this;
    }

    @Override
    public ByteArrayOutputStream getGyartasiLap(Job job) throws IOException {
        if (workbook == null) {
            throw new RuntimeException("workbook is null, pls use the setWorkbook");
        }

        Sheet sheet = workbook.getSheetAt(0);

        if (job == null) {
            throw new IllegalStateException("Job can not be null");
        }

        if (job.getCreateDateTime() != null) {
            Row row = sheet.getRow(4);
            Cell cell = row.getCell(3);
            cell.setCellValue(job.getCreateDateTime());
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

        if (job.getProduct() != null && job.getProduct().getDrawingNumber() != null) {
            Row row = sheet.getRow(5);
            Cell cell = row.getCell(5);
            cell.setCellValue(job.getProduct().getDrawingNumber());
        }

        if (job.getProductCount() != null) {
            Row row = sheet.getRow(5);
            Cell cell = row.getCell(7);
            cell.setCellValue(job.getProductCount().toString());
        }

        if (job.getProduct() != null) {
            String productName = job.getProduct().getName();

            Row row = sheet.getRow(7);
            Cell cell = row.getCell(3);
            cell.setCellValue(productName);

            if (job.getProduct().getRawmaterial() != null) {
                cell = row.getCell(6);
                cell.setCellValue(job.getProduct().getRawmaterial().getGrade());
            }
        }

        if (job.getMachine() != null) {
            Row row = sheet.getRow(16);
            Cell cell = row.getCell(3);
            cell.setCellValue(job.getMachine().getName());
        }

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        workbook.write(bOutput);

        return bOutput;
    }

    public ByteArrayOutputStream getVisszaIgazolas(Job job) throws IOException {
        if (workbook == null) {
            throw new RuntimeException("workbook is null, pls use the setWorkbook");
        }

        Sheet sheet = workbook.getSheetAt(0);

        if (job == null) {
            throw new IllegalStateException("Job can not be null");
        }

        if (job.getCustomer() != null) {
            Row row = sheet.getRow(11);
            Cell cell = row.getCell(6);
            cell.setCellValue(job.getCustomer().getName());
        }

        if (job.getWorknumber() != null) {
            Row row = sheet.getRow(16);
            Cell cell = row.getCell(6);
            cell.setCellValue(job.getWorknumber());
        }

        if (job.getProduct() != null && job.getProduct().getDrawingNumber() != null) {
            Row row = sheet.getRow(16);
            Cell cell = row.getCell(7);
            cell.setCellValue(job.getProduct().getDrawingNumber());
        }

        if (job.getProductCount() != null) {
            Row row = sheet.getRow(16);
            Cell cell = row.getCell(9);
            cell.setCellValue(job.getProductCount().toString());
        }

        if (job.getProduct() != null) {
            String productName = job.getProduct().getName();

            Row row = sheet.getRow(16);
            Cell cell = row.getCell(8);
            cell.setCellValue(productName);
        }

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        workbook.write(bOutput);

        return bOutput;
    }
}
