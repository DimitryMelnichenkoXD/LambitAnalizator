package com.lambit.analizator.model;

import com.lambit.analizator.interfaces.View;
import com.lambit.analizator.table.CellTable;
import com.lambit.analizator.table.Column;
import com.lambit.analizator.table.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

@Data
@NoArgsConstructor
public class ModelFormatClass implements ModelFormat {
    public static final int NECESSARY_INFO_IN_COLUMS = 36;

    private Table table;//FIXME Должно создаваться в конструкторе

    private Workbook workbook;
    private Sheet page;
    private double maxDiv;
    private double minDiv;
    private double expectedValue;
    private String fileName;

    public ModelFormatClass(Workbook workbook, double maxDiv, double minDiv, double expectedValue, String fileName) {
            this.table = new Table();
            this.workbook = workbook;
            this.maxDiv = maxDiv;
            this.minDiv = minDiv;
            this.expectedValue = expectedValue;
            this.page = workbook.getSheetAt(0);
            this.fileName = fileName;
    }

    @Override
    public void createColumnsInTable() {
            Row row = page.getRow(0);
            Iterator iter = row.cellIterator();

            while(iter.hasNext()){
                Cell cellTable = (Cell) iter.next();
                if(cellTable.getColumnIndex()< NECESSARY_INFO_IN_COLUMS){
                    String nameColumn = cellTable.getRichStringCellValue().getString();
                    table.addColumn(new Column(nameColumn));
                }
            }
    }

    public void addCellInColumns(){
        Iterator rowIter = page.rowIterator();
        while (rowIter.hasNext()){
            Row row = (Row) rowIter.next();
            if(row.getRowNum()!=0){
                createCell(row);
            }
        }
    }//Добавить ячейки в колонки(2)

    public void createCell(Row row) {
        LocalDateTime date = getLocalDateTame(row);
        Iterator cellIter = row.cellIterator();
        int index = 0;
        if (date!=null){
            while (cellIter.hasNext()){
                val cells = (Cell)cellIter.next();
                if(index >1&&index< NECESSARY_INFO_IN_COLUMS){
                    double value = cells.getNumericCellValue();
                    CellTable cellTable = new CellTable(value, date);
                    if(value<= expectedValue + maxDiv &&value>= expectedValue - minDiv){
                        cellTable.setDiviation(true);
                    }else{
                        cellTable.setDiviation(false);
                    }
                    table.getColumn(index).addCells(cellTable);
                }
                index++;
            }
        }
    }//Создаем ячейки для таблтцы

    public LocalDateTime getLocalDateTame(Row r){
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

    public void findScatter(){
        ArrayList<Column> columnInTable= table.getTable();
        for (int i = 2; i <columnInTable.size() ; i++) {
            Column c = columnInTable.get(i);
            c.setScatter();
        }
    }//Поиск разброса по колонкам

    public Column maxScatter(){
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
