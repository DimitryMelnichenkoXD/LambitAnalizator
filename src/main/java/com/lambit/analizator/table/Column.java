package com.lambit.analizator.table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@Data
@NoArgsConstructor
public class Column {
    private ArrayList<CellTable> colum = new ArrayList<CellTable>();
    private String name;
    private double scatter;

    public Column(String name) {
        this.name = name;
    }

    public CellTable getValue(int index){
        return colum.get(index);
    }

    public void addCells(CellTable o){
        colum.add(o);
    }

    public void setScatter() {
        ArrayList<Double> num = new ArrayList<Double>();
        for (CellTable c: colum){
           num.add(c.getValue());
        }
        Collections.sort(num);
        scatter = num.get(num.size()-1)-num.get(0);
    }//метод для нахождения разбросса



}
