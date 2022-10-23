package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Customer;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.service.ExcelExporter;
import hu.mycompany.machinemanager.service.ExcelType;
import java.io.*;
import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

class ExcelExporterImplTest {

    private ExcelExporter excelExporter;

    @Test
    void jobSheet() throws IOException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("gyartasi_lap.xlsx");
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        excelExporter = new ExcelExporterFactoryImpl(new ExcelExporterImpl()).create(ExcelType.GYARTASI_LAP);

        Job job = new Job();
        job.setWorknumber("asdasdsd-19");
        job.setProductCount(1000);
        job.createDateTime(LocalDateTime.now());
        Customer customer = new Customer();
        customer.setName("megrendel jeno");
        job.setCustomer(customer);
        excelExporter.getGyartasiLap(job);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.close();
    }
}
