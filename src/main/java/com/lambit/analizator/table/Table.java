package com.lambit.analizator.table;

import java.util.ArrayList;

public class Table {
    private ArrayList<Column> table = new ArrayList<Column>();

    public ArrayList<Column> getTable() {
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

    public String showTable(){
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < table.size(); i++) {
            Column buffer = table.get(i);
            builder.append(buffer.getName()).append("\n");
            builder.append("---------------------------------------------------\n");
            for (int j = 0; j < buffer.getColum().size(); j++) {
                if(!buffer.getValue(j).isDiviation()) {
                    builder.append(buffer.getValue(j).toString());
                    builder.append("\n");
                }
            }
            builder.append("---------------------------------------------------\n");
        }
        return builder.toString();
    }//Метод для вывода таблицы
}
