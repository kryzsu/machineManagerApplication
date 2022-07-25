package hu.mycompany.machinemanager.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import hu.mycompany.machinemanager.domain.Customer;
import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.service.ExcelExporter;
import java.io.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

class ExcelExporterImplTest {

    private ExcelExporter excelExporter;

    @Test
    void jobSheet() throws IOException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("gyartasi_lap.xlsx");
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        excelExporter = new ExcelExporterImpl(workbook);

        Job job = new Job();
        job.setWorknumber("asdasdsd-19");
        job.setProductCount(1000);
        job.setDrawingNumber("1234-23");
        Customer customer = new Customer();
        customer.setName("megrendel jeno");
        job.setCustomer(customer);
        excelExporter.writeJobData(job);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.close();
    }
}
