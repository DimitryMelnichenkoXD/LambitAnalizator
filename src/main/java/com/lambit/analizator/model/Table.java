package com.lambit.analizator.model;

import java.util.ArrayList;

public class Table {
    private ArrayList<Column> table = new ArrayList<>();

    public ArrayList<Column> getTableColumn() {
        return table;
    }

    public void setTable(ArrayList<Column> table) {
        this.table = table;
    }

    public void addColumn(Column c){
        table.add(c);
    }

    public Column getColumn(int index){
        return table.get(index);
    }

    public int getLength(){
        return table.size();
    }
}
