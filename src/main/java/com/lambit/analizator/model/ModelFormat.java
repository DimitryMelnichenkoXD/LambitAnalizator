package com.lambit.analizator.model;

import com.lambit.analizator.table.Column;

import java.time.LocalDateTime;

public interface ModelFormat {

    void createColumnsInTable();
    void addCellInColumns();
    void createCell();
    LocalDateTime getLocalDateTame();
    void findScatter();
    Column maxScatter();
    void startView();
}
