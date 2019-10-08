package com.lambit.analizator.view;

import com.lambit.analizator.model.Column;
import javafx.scene.chart.XYChart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class LineChartDrawer {
//    private final String AXIS_NAME_X = "Время";
    private XYChart.Series dateTimeNumberLineChart ;
    private String nameOfChart;
    private Column columnOfChart;
//    private String axisNameY;

    public LineChartDrawer(Column columnOfChart) {
        this.columnOfChart = columnOfChart;
        this.nameOfChart = columnOfChart.getName();
        dateTimeNumberLineChart = new XYChart.Series();
        drow();


    }

    private void drow() {
        dateTimeNumberLineChart.setName(nameOfChart);
        columnOfChart.getColum().forEach(cellTable -> {
            dateTimeNumberLineChart.getData()
                    .add(new XYChart.Data(cellTable.getDateTime().format (DateTimeFormatter.ofPattern ("MM/dd/yy HH:mm:ss")), cellTable.getValue()));
        });
    }
}

