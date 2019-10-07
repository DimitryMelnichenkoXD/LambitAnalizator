package com.lambit.analizator.controller.data;

import com.lambit.analizator.model.Table;
import com.lambit.analizator.view.LineChartDrawer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DataCharts {
    private List<LineChartDrawer> lineCharts = new ArrayList<>();
    private Table table;

    public DataCharts(Table table) {
        this.table = table;
    }

    public void drowCharts(){
        table.getTableColumn().stream().skip(2).forEach(column -> lineCharts.add(new LineChartDrawer(column)));
    }
}
