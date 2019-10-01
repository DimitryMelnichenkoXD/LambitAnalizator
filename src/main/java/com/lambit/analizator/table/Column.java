package com.lambit.analizator.table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@Data
@NoArgsConstructor
public class Column {
    private ArrayList<Cell> colum = new ArrayList<Cell>();
    private String name;
    private double scatter;

    public Column(String name) {
        this.name = name;
    }

    public Cell getValue(int index){
        return colum.get(index);
    }

    public void addCells(Cell o){
        colum.add(o);
    }

    public void setScatter() {
        ArrayList<Double> num = new ArrayList<Double>();
        for (Cell c: colum){
           num.add(c.getValue());
        }
        Collections.sort(num);
        scatter = num.get(num.size()-1)-num.get(0);
    }//метод для нахождения разбросса



}
