package com.lambit.analizator.controller.data;

import com.lambit.analizator.model.CellTable;
import com.lambit.analizator.model.Column;
import com.lambit.analizator.model.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDateTime;
import java.util.Iterator;

@Data
@NoArgsConstructor
public class ModelFormatImpl implements ModelFormat {
    public static final int NECESSARY_INFO_IN_COLUMS = 38;

    private Table table;
    private Workbook workbook;
    private Sheet page;
    private String fileName;

    public ModelFormatImpl(Workbook workbook, String fileName) {
            this.table = new Table();
            this.workbook = workbook;
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
        while (rowIter.hasNext()) {
            Row row = (Row) rowIter.next();
//            Thread threadForCell = new Thread(() -> {
                if (row.getRowNum() != 0) {
                    createCell(row);
                }
//            });
//            threadForCell.start();
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




    public void writeTable(){
        createColumnsInTable();
        Thread addCellThread = new Thread(this::addCellInColumns);
        addCellThread.start();
        try {
            addCellThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Метод для вывода из com.lambit.analizator.interfaces.View.
    }


}
