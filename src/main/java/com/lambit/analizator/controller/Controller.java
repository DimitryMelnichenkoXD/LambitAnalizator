package com.lambit.analizator.controller;

import com.lambit.analizator.model.ModelXLS;
import com.lambit.analizator.model.ModelXLSX;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

@Data
@NoArgsConstructor
public class Controller {
    //Получкние информации
    private double maxDiv;
    private double minDiv;
    private double expectedValue;
    private File file;

    public Controller(String maxDiv, String minDiv, String expectedValue, File file) {
        this.maxDiv = Double.valueOf(maxDiv);
        this.minDiv = Double.valueOf(minDiv);
        this.expectedValue = Double.valueOf(expectedValue);
        this.file = file;
    }

    private XSSFWorkbook readWorkbookXLSX() {
        File fileName = this.file;
        try {
            return new XSSFWorkbook(OPCPackage.open(fileName));
        } catch (Exception e) {
            System.out.println("Оштбка не удалось считать книгу(com.lambit.analizator.controller.Controller.java---->48-56)");
            return null;
        }
    }//Получение книги с таблтцой из файла xmls.

    private HSSFWorkbook readWorkbookXLS() {
        File fileName = this.file;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
            return new HSSFWorkbook(fs);
        } catch (Exception e) {
            System.out.println("Оштбка не удалось считать книгу(com.lambit.analizator.controller.Controller.java---->48-56)");
            return null;
        }
    }//Получение книги с таблтцой из файла.

    public void startModel() {
        if (FilenameUtils.getExtension(file.getName()).equals("xlsx")) {
            XSSFWorkbook workbook = readWorkbookXLSX();
            ModelXLSX model = new ModelXLSX(workbook, maxDiv, minDiv, expectedValue, file.getName());
            model.startView();
        } else {
            HSSFWorkbook workbook = readWorkbookXLS();
            ModelXLS model = new ModelXLS(workbook, maxDiv, minDiv, expectedValue, file.getName());
            model.startView();
        }
    }
}
