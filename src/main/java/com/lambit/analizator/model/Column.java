package com.lambit.analizator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Column {
    private ArrayList<CellTable> colum = new ArrayList<>();
    private String name;

    public Column(String name) {
        this.name = name;
    }

    public CellTable getValue(int index){
        return colum.get(index);
    }

    public void addCells(CellTable o){
        colum.add(o);
    }

}
