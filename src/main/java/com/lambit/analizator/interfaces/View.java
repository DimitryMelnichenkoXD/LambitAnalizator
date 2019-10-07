package com.lambit.analizator.interfaces;

import com.lambit.analizator.model.CellTable;
import com.lambit.analizator.model.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class View {

    private RezultFrame rezultFrame;
    private Column maxScatterColumn;
    private String textForJArea;
    private ArrayList<Column> tableColums;
    private Box mainBox = Box.createHorizontalBox();
    private String openFile;

    public View(Column maxScatterColumn, String textForJArea, ArrayList<Column> tableColums, String openFile) {
        this.maxScatterColumn = maxScatterColumn;
        this.textForJArea = textForJArea;
        this.tableColums = tableColums;
        this.openFile = openFile;
    }

    public void makeStandartRezultFrame() {
        RezultFrame frame = new RezultFrame(openFile, 1500, 770);
        mainBox.add(makeLeftBox(0));
        mainBox.add(makeMidleBox());
        mainBox.add(makeRightBox(tableColums.indexOf(maxScatterColumn)));
        frame.add(mainBox);
        this.rezultFrame = frame;

    }

    private Box makeLeftBox(int choose) {
        Box leftBox = Box.createVerticalBox();
        final int NCM2 = 35;
        final int D = 34;
        if (choose == 0) {
            leftBox.add(dworChart(NCM2, "График по N_Cm3"));
        } else {
            leftBox.add(dworChart(D, "График по D_avr"));
        }
        return leftBox;
    }

    private Box makeRightBox(int choose) {
        Box rightBox = Box.createVerticalBox();
        rightBox.add(dworChart(choose, "График для максимального разбросса"));
        return rightBox;
    }

    private Box makeMidleBox() {
        Box midleBox = Box.createVerticalBox();
       // JTextArea textField = new JTextArea("Колонка с максимальным разбросом - " + maxScatterColumn.getName() + " Значение: " + maxScatterColumn.getScatter(), 1, 20);
        JTextArea jTextArea = new JTextArea(textForJArea, 50, 20);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        //midleBox.add(textField);
        midleBox.add(comboBox());
        midleBox.add(jScrollPane);
        midleBox.add(radioGrop());
        return midleBox;
    }

    private Box radioGrop() {
        ButtonGroup group = new ButtonGroup();
        AbstractButton nChose = new JRadioButton("N_Cm3", true);
        AbstractButton dChose = new JRadioButton("D_Avr", false);

        nChose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rezultFrame.setVisible(false);
                replaseLeftBox(0);
                rezultFrame.setVisible(true);
            }
        });
        dChose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rezultFrame.setVisible(false);
                replaseLeftBox(1);
                rezultFrame.setVisible(true);
            }
        });

        group.add(nChose);
        group.add(dChose);
        Box radioGrop = Box.createHorizontalBox();
        radioGrop.add(nChose);
        radioGrop.add(dChose);

        return radioGrop;
    }//Создание группы переключателей

    private Box comboBox() {
        final JComboBox choseColumsForChart = new JComboBox();
        for (int i = 2; i < 34; i++) {
            choseColumsForChart.addItem(tableColums.get(i).getName());
        }
        choseColumsForChart.addActionListener(e -> {
            int index = choseColumsForChart.getSelectedIndex() + 2;
            rezultFrame.setVisible(false);
            replaseRigthBox(index);
            rezultFrame.setVisible(true);
        });
        Box boxForCombo = Box.createHorizontalBox();
        boxForCombo.add(choseColumsForChart);
        return boxForCombo;
    }

    private void replaseLeftBox(int num) {
        mainBox.remove(0);
        mainBox.add(makeLeftBox(num), 0);
    }//Отрисовка левого графика

    private void replaseRigthBox(int num) {
        mainBox.remove(2);
        mainBox.add(makeRightBox(num), 2);
    }//Отрисовка правого графика

    private ChartPanel dworChart(int num, String title) {
        JFreeChart chart = createChart(makeDataset(tableColums.get(num)), title);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 300));
        return panel;
    }

    private XYDataset makeDataset(Column maxScatterColumn) {
        TimeSeries series = new TimeSeries("График по колонке " + maxScatterColumn.getName());
        for (int i = 0; i < maxScatterColumn.getColum().size() - 1; i++) {
            CellTable bufferCellTable = maxScatterColumn.getValue(i);
            LocalDateTime time = bufferCellTable.getDateTime();
            series.add(new Second(time.getSecond(),
                    time.getMinute(),
                    time.getHour(),
                    time.getDayOfMonth(),
                    time.getMonthValue(),
                    time.getYear()), bufferCellTable.getValue());
        }
        TimeSeriesCollection collection = new TimeSeriesCollection();
        collection.addSeries(series);
        return collection;
    }//Создание датасетовж

    private JFreeChart createChart(XYDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,  // title//Название файла
                "Время;М",                            // x-axis label
                "Относительное изменение в %",                      // y-axis label
                dataset,                       // data
                true,                          // create legend
                true,                          // generate tooltips
                false                          // generate URLs
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));

        return chart;
    }//Создание непосредственно графика


}
