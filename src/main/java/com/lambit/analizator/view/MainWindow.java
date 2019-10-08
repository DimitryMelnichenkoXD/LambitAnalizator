package com.lambit.analizator.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Data
@NoArgsConstructor
public class MainWindow {
    Parent root = null;
    @FXML private TabPane mainWindowChooseChart;
    @FXML
    private ChoiceBox firstSerialChartChoiceBox;
    @FXML
    private TextArea firstSerialChartTextArea;
    @FXML
    private Label firstSerialChartLable;
    @FXML
    private LineChart firstSerialChartChart;

    @FXML
    private ChoiceBox secondSerialChartChoiceBox;
    @FXML
    private Label secondSerialChartLable;
    @FXML
    private TextArea secondSerialChartTextArea;
    @FXML
    private LineChart secondSerialChartChart;

    @FXML
    private ChoiceBox thirdSerialChartChoiceBox;
    @FXML
    private Label thirdSerialChartLable;
    @FXML
    private TextArea thirdSerialChartTextArea;
    @FXML
    private LineChart thirdSerialChartChart;
    @FXML
    private MenuItem menuOpenFile;

    private final String NAME_AXIS_X = "Время";

    public MainWindow(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);

        mainWindowChooseChart = (TabPane) scene.lookup("#mainWindowChooseChart");
        mainWindowChooseChart.setVisible(false);

       Thread first = new Thread(() -> {
            firstSerialChartChart = (LineChart) scene.lookup("#firstSerialChartChart");
            firstSerialChartChart.getXAxis().setLabel(NAME_AXIS_X);
            firstSerialChartChart.getYAxis().setLabel("Относительное изменение, %");
        });

        Thread second = new Thread(() -> {
            secondSerialChartChart = (LineChart) scene.lookup("#secondSerialChartChart");
            secondSerialChartChart.getXAxis().setLabel(NAME_AXIS_X);
            secondSerialChartChart.getYAxis().setLabel("Абсолютное значение, N/cm3");
        });

        Thread third = new Thread(() -> {
            thirdSerialChartChart = (LineChart) scene.lookup("#thirdSerialChartChart");
            thirdSerialChartChart.getXAxis().setLabel(NAME_AXIS_X);
            thirdSerialChartChart.getYAxis().setLabel("");
        });
        first.start();
        second.start();
        third.start();

        try {
            first.join();
            second.join();
            third.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Главное окно");
        primaryStage.setScene(scene);
    }
}
