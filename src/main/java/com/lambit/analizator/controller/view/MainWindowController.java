package com.lambit.analizator.controller.view;

import com.lambit.analizator.controller.data.Controller;
import com.lambit.analizator.controller.data.DataCharts;
import com.lambit.analizator.view.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MainWindowController extends Application {
    public static final int INFO_IN_TABLE_FOR_FIRST_SERIAL_CHART = 0;
    public static final int INFO_IN_TABLE_FOR_SECOND_SERIAL_CHART = 16;
    public static final int INFO_IN_TABLE_FOR_THIRD_SERIAL_CHART = 30;

    private final FileChooser fileChooser = new FileChooser();
    @FXML
    private TabPane mainWindowChooseChart;
    @FXML
    private ChoiceBox firstSerialChartChoiceBox;
    @FXML
    private Label firstSerialChartLable;
    @FXML
    private TextArea firstSerialChartTextArea;
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
    private MainWindow mainWindow;
    private Controller controller;
    private Stage primaryStage;
    private DataCharts dataCharts;

    @Override
    public void start(Stage primaryStage){
        Platform.runLater(() -> {
            primaryStage.setMaximized (true);
            this.primaryStage = primaryStage;
            mainWindow = new MainWindow(primaryStage);
            primaryStage.show();
        });
    }

    public void startMainWindow(String[] args) {
        launch(args);
    }

    private void serialWrite() {
        ObservableList particleSize = getObservableListByParticleSize();

        Platform.runLater(() -> firstSerialWrite(particleSize));
        Platform.runLater(() -> secondSerialWrite(particleSize));
        Platform.runLater(this::thirdSerialWrite);

    }

    private void thirdSerialWrite() {
        ObservableList<String> observableList = javafx.collections.FXCollections.observableArrayList("D_avr", "N_cm3");
        thirdSerialChartChoiceBox.setItems(observableList);

        thirdSerialChartChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                refreshScene(thirdSerialChartChart, INFO_IN_TABLE_FOR_THIRD_SERIAL_CHART, thirdSerialChartChoiceBox, thirdSerialChartLable);
            }
        });
        thirdSerialChartChoiceBox.getSelectionModel().selectFirst();

    }

    private void secondSerialWrite(ObservableList list) {
        secondSerialChartChoiceBox.setItems(list);

        secondSerialChartChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                refreshScene(secondSerialChartChart, INFO_IN_TABLE_FOR_SECOND_SERIAL_CHART, secondSerialChartChoiceBox, secondSerialChartLable);
            }
        });
        secondSerialChartChoiceBox.getSelectionModel().selectFirst();
    }

    private void firstSerialWrite(ObservableList list) {
        firstSerialChartChoiceBox.setItems(list);

        firstSerialChartChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                refreshScene(firstSerialChartChart, INFO_IN_TABLE_FOR_FIRST_SERIAL_CHART, firstSerialChartChoiceBox, firstSerialChartLable);
            }
        });
        firstSerialChartChoiceBox.getSelectionModel().selectFirst();
    }

    private void refreshScene(LineChart chart, int skip, ChoiceBox box, Label label){
        XYChart.Series set = dataCharts.getLineCharts()
                .stream()
                .skip(skip)
                .filter(lineChartDrawer -> box.getValue().equals(lineChartDrawer.getNameOfChart()))
                .findFirst()
                .get()
                .getDateTimeNumberLineChart();

        if(!chart.getData().contains(set)){
            chart.getData().add(set);
        }
        label.setText(box.getValue().toString());
    }

    public void addNewFile(ActionEvent actionEvent) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xls, xlsx", "*.xls", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("xls", "*.xls"),
                new FileChooser.ExtensionFilter("xlsx", "*.xlsx"));

        File file = fileChooser.showOpenDialog(primaryStage);

        Thread saveDir = new Thread(() -> {
            if (file != null) {
                fileChooser.setInitialDirectory(file.getParentFile());
            }
        });
        saveDir.start();

        clearAllCharts();

        Thread makeView = new Thread(() -> {
            if (file != null) {
                controller = new Controller(file);
                Thread thread = new Thread(controller::startModel);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dataCharts = new DataCharts(controller.getModel().getTable());
                dataCharts.drowCharts();
                serialWrite();
                mainWindowChooseChart.setVisible(true);
            }
        });
        makeView.start();
    }

    private void clearAllCharts() {
        firstSerialChartChart.getData().clear();
        secondSerialChartChart.getData().clear();
        thirdSerialChartChart.getData().clear();

    }

    private ObservableList getObservableListByParticleSize() {
        return javafx.collections.FXCollections.observableArrayList(controller.getModel()
                .getTable()
                .getTableColumn()
                .stream().skip(2).limit(9)
                .map(column -> column.getName())
                .collect(Collectors.toList()));
    }

    public void clearChart(ActionEvent event) {
        String buttoneID = ((Node)event.getSource()).getId();
        if (buttoneID.equals("firstSerialChartClearButton")){
            firstSerialChartChart.getData().clear();
        }else if(buttoneID.equals("secondSerialChartClearButton")){
            secondSerialChartChart.getData().clear();
        }else{
            thirdSerialChartChart.getData().clear();
        }
    }

}
