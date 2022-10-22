package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.service.ExcelExporter;
import hu.mycompany.machinemanager.service.ExcelExporterFactory;
import hu.mycompany.machinemanager.service.ExcelType;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelExporterFactoryImpl implements ExcelExporterFactory {

    private final ExcelExporter excelExporter;

    public ExcelExporterFactoryImpl(ExcelExporter excelExporter) {
        this.excelExporter = excelExporter;
    }

    @Override
    public ExcelExporter create(ExcelType excelType) throws IOException {
        return excelExporter.setWorkbook(getWorkBook(excelType));
    }

    private Workbook getWorkBook(ExcelType excelType) throws IOException {
        String fileName = excelType == ExcelType.GYARTASI_LAP ? "gyartasi_lap.xlsx" : "visszaig.xlsx";
        InputStream file = getClass().getClassLoader().getResourceAsStream(fileName);
        return new XSSFWorkbook(file);
    }
}
