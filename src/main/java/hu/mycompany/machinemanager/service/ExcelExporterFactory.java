package hu.mycompany.machinemanager.service;

import java.io.IOException;

public interface ExcelExporterFactory {
    public ExcelExporter create(ExcelType excelType) throws IOException;
}
