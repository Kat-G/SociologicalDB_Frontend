package com.example.sociologicaldb_frontend.frames;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
@FxmlView("MainFrame.fxml")
public class MainFrameController {

    private final ConfigurableApplicationContext applicationContext;

    @FXML
    private TreeView<String> treeView;
    @FXML
    private Label respLabel;
    @FXML
    private Label resLabel;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @Autowired
    public MainFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        initializeTreeView();
        // If you need to initialize the chart, you can uncomment and modify this block
        /*
        xAxis.setLabel("Категория");
        yAxis.setLabel("Значение");
        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.getData().add(new XYChart.Data<>("", 46));
        barChart.getData().add(data);
        barChart.setLegendVisible(false);
         */
    }

    private void initializeTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Исследования");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/sociologicaldb_frontend/info/treeview-info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                TreeItem<String> item = new TreeItem<>(parts[0]);
                rootItem.getChildren().add(item);

                for (int i = 1; i < parts.length; i++) {
                    item.getChildren().add(new TreeItem<>(parts[i]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeView.setRoot(rootItem);
    }

    @FXML
    private void onAploadButtonClick(ActionEvent actionEvent) {
        loadView(UploadFrameController.class, "Загрузка исследования");
    }

    @FXML
    private void onCorrelationButtonClick(ActionEvent actionEvent) {
        loadView(CorrelationFrameController.class, "Корреляционная матрица");
    }

    @FXML
    private void onVariationButtonClick(ActionEvent actionEvent) {
        loadView(VariationFrameController.class, "Вариационный ряд");
    }

    @FXML
    private void onMAIButtonClick(ActionEvent actionEvent) {
        loadView(MAIStepOneFrameController.class, "МАИ. Задание иерархии");
    }

    private void loadView(Class<?> controllerClass, String title) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
