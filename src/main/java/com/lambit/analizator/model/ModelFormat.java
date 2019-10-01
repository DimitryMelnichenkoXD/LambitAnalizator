package com.lambit.analizator.model;

import com.lambit.analizator.table.Column;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;

public interface ModelFormat {

    void createColumnsInTable();
    void addCellInColumns();
    void createCell(Row row);
    LocalDateTime getLocalDateTame(Row row);
    void findScatter();
    Column maxScatter();
    void startView();


}
