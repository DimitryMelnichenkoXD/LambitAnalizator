package com.lambit.analizator.controller.data;

import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;

public interface ModelFormat {

    void createColumnsInTable();
    void addCellInColumns();
    void createCell(Row row);
    LocalDateTime getLocalDateTame(Row row);
    void writeTable();


}
