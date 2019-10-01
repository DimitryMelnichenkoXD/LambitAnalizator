package com.lambit.analizator.model;

import com.lambit.analizator.interfaces.View;
import com.lambit.analizator.table.Cell;
import com.lambit.analizator.table.Column;
import com.lambit.analizator.table.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

@Data
@NoArgsConstructor
public class ModelXLSX {
    public static final int NECESSARY_INFO_IN_COLUMS = 36;
    //Логика обработки
    private Table table = new Table();
    private XSSFWorkbook workbook;

    private double maxDiv;
    private double minDiv;
    private double expectedValue;
    private String fileName;

    public ModelXLSX(XSSFWorkbook workbook, double maxDiv, double minDiv, double expectedValue, String fileName) {
        this.workbook = workbook;
        this.maxDiv = maxDiv;
        this.minDiv = minDiv;
        this.expectedValue = expectedValue;
        this.fileName = fileName;
    }

    public void startView() {
        val page = workbook.getSheetAt(0);
        createColumnsInTable(page);
        addCellInColumns(page);
        findScatter();
        Column columnWithMaxScatter = maxScatter();
        String outputText = table.showTable();
        View view = new View(columnWithMaxScatter, outputText, table.getTable(), fileName);
        view.makeStandartRezultFrame();
    }//Метод для вывода из com.lambit.analizator.interfaces.View.

    private void createColumnsInTable(XSSFSheet page) {
        XSSFRow row = page.getRow(0);
        Iterator iter = row.cellIterator();
        int i = 0;
        while (iter.hasNext()) {
            XSSFCell cell = (XSSFCell) iter.next();
            if (i < NECESSARY_INFO_IN_COLUMS) {
                String nameColumn = cell.getRichStringCellValue().getString();
                table.addColumn(new Column(nameColumn));
                i++;
            }
        }
    }//Создание столбцов для хранения информации и соответствий(1)

    private void addCellInColumns(XSSFSheet page) {
        Iterator rowIter = page.rowIterator();
        while (rowIter.hasNext()) {
            XSSFRow row = (XSSFRow) rowIter.next();
            if (row.getRowNum()!=0) {
                createCell(row);
            }
        }
    }//Добавить ячейки в колонки(2)

    private void createCell(XSSFRow row) {
        LocalDateTime date = getLocalDateTame(row);
        Iterator cellIter = row.cellIterator();
        int index = 0;
        if (date!=null){
            while (cellIter.hasNext()){
                val cells = (XSSFCell) cellIter.next();
                if(index >1&&index< NECESSARY_INFO_IN_COLUMS){
                    double value = cells.getNumericCellValue();
                    Cell cell = new Cell(value, date);
                    if(value<=expectedValue+maxDiv&&value>=expectedValue-minDiv){
                        cell.setDiviation(true);
                    }else{
                        cell.setDiviation(false);
                    }
                    table.getColumn(index).addCells(cell);
                }
                index++;
            }
        }
    }//Создаем ячейки для таблтцы

    private LocalDateTime getLocalDateTame(XSSFRow r){
        val cell = r.getCell(0);
        if (cell!=null){
            val dateCell = cell.getDateCellValue();
            val cell1 = r.getCell(1);
            val timeCell = cell1.getDateCellValue();

            if (cell1.getDateCellValue()!=null||cell.getDateCellValue()!=null){
                return LocalDateTime.of(1900+dateCell.getYear(),
                        1+dateCell.getMonth(),
                        dateCell.getDate(),
                        timeCell.getHours(),
                        timeCell.getMinutes(),
                        timeCell.getSeconds());
            }else {
                return null;
            }
        }return null;


    }//получение значения даты из строкиж

    private void findScatter() {
        ArrayList<Column> columnInTable = table.getTable();
        for (int i = 2; i < columnInTable.size(); i++) {
            Column c = columnInTable.get(i);
            c.setScatter();
        }
    }//Поиск разброса по колонкам

    private Column maxScatter() {
        return getColumn(table);
    }//Метод для нахождения колонки с максимальным пазброссом

    static Column getColumn(Table table) {
        ArrayList<Column> columns = table.getTable();
        Column rezult = null;
        for (int i = 2; i < 34; i++) {
            Column c = columns.get(i);
            if (rezult == null) {
                rezult = c;
            } else {
                if (rezult.getScatter() <= c.getScatter()) {
                    rezult = c;
                }
            }
        }
        return rezult;
    }


}
