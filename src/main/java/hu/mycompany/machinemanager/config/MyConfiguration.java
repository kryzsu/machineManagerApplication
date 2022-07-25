package hu.mycompany.machinemanager.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    Workbook workbook() throws IOException {
        InputStream file = getClass().getClassLoader().getResourceAsStream("gyartasi_lap.xlsx");
        return new XSSFWorkbook(file);
    }
}
