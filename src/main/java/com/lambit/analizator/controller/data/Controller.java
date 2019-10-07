package com.lambit.analizator.controller.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

@Data
@NoArgsConstructor
public class Controller {

    private File file;
    private ModelFormatImpl model;

    public Controller(File file) {
        this.file = file;
    }

    private XSSFWorkbook readWorkbookXLSX() {
        File fileName = this.file;
        try {
            return new XSSFWorkbook(OPCPackage.open(fileName));
        } catch (Exception e) {
            return null;
        }
    }//Получение книги с таблтцой из файла xmls.

    private HSSFWorkbook readWorkbookXLS() {
        File fileName = this.file;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
            return new HSSFWorkbook(fs);
        } catch (Exception e) {
            return null;
        }
    }//Получение книги с таблтцой из файла.

    public void startModel() {
        if (FilenameUtils.getExtension(file.getName()).equals("xlsx")) {
            Workbook workbook = readWorkbookXLSX();
            model = new ModelFormatImpl(workbook, file.getName());
            model.writeTable();
        } else {
            Workbook workbook = readWorkbookXLS();
            model = new ModelFormatImpl(workbook, file.getName());
            model.writeTable();
        }
    }
}
