package com.lambit.analizator.model;

import com.lambit.analizator.interfaces.View;
import com.lambit.analizator.table.Cell;
import com.lambit.analizator.table.Column;
import com.lambit.analizator.table.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import static com.lambit.analizator.model.ModelXLSX.getColumn;

@Data
@NoArgsConstructor
public class ModelXLS {
    //Логика обработки
    private Table table = new Table();
    private HSSFWorkbook workbook;
    private HSSFSheet page;
    private double maxDiv;
    private double minDiv;
    private double expectedValue;
    public static final int NECESSARY_INFO_IN_COLUMS = 36;
    private String fileName;

    public ModelXLS(HSSFWorkbook workbook, double maxDiv, double minDiv, double expectedValue, String fileName) {
        this.workbook = workbook;
        this.maxDiv = maxDiv;
        this.minDiv = minDiv;
        this.expectedValue = expectedValue;
        this.page = workbook.getSheetAt(0);
        this.fileName = fileName;
    }

    private void createColumnsInTable(){
        HSSFRow row = page.getRow(0);
        Iterator iter = row.cellIterator();

        while(iter.hasNext()){
            HSSFCell cell = (HSSFCell) iter.next();
            if(cell.getColumnIndex()< NECESSARY_INFO_IN_COLUMS){
            String nameColumn = cell.getRichStringCellValue().getString();
            table.addColumn(new Column(nameColumn));
            }
        }
    }//Создание столбцов для хранения информации и соответствий(1)

    private void addCellInColumns(){
        Iterator rowIter = page.rowIterator();
        while (rowIter.hasNext()){
            HSSFRow row = (HSSFRow) rowIter.next();
            if(row.getRowNum()!=0){
                createCell(row);
            }
        }
    }//Добавить ячейки в колонки(2)

    private void createCell(HSSFRow row) {
        LocalDateTime date = getLocalDateTame(row);
        Iterator cellIter = row.cellIterator();
        int index = 0;
        if (date!=null){
            while (cellIter.hasNext()){
                val cells = (HSSFCell) cellIter.next();
                if(index >1&&index< NECESSARY_INFO_IN_COLUMS){
                    double value = cells.getNumericCellValue();
                    Cell cell = new Cell(value, date);
                    if(value<= expectedValue + maxDiv &&value>= expectedValue - minDiv){
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

    private LocalDateTime getLocalDateTame(HSSFRow r){
        val cellDate = r.getCell(0);
        val cellTime = r.getCell(1);

        if (cellDate==null||cellTime==null||cellDate.equals("")||cellTime.equals("")){
            return null;
        }else {
            val dateCellValue = cellDate.getDateCellValue();
            val timeCellValue = cellTime.getDateCellValue();
            return LocalDateTime.of(1900+dateCellValue.getYear(),
                    1+dateCellValue.getMonth(),
                    dateCellValue.getDate(),
                    timeCellValue.getHours(),
                    timeCellValue.getMinutes(),
                    timeCellValue.getSeconds());
        }
    }//получение значения даты из строкиж

    private void findScatter(){
        ArrayList<Column> columnInTable= table.getTable();
        for (int i = 2; i <columnInTable.size() ; i++) {
            Column c = columnInTable.get(i);
            c.setScatter();
        }
    }//Поиск разброса по колонкам

    private Column maxScatter(){
        return getColumn(table);
    }//Метод для нахождения колонки с максимальным пазброссом



    public void startView(){
        createColumnsInTable();
        addCellInColumns();
        findScatter();
        Column columnWithMaxScatter= maxScatter();
        String outputText = table.showTable();
        View view = new View(columnWithMaxScatter,outputText,table.getTable(),fileName);
        view.makeStandartRezultFrame();
        //Метод для вывода из com.lambit.analizator.interfaces.View.
    }

}
