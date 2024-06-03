package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.TableInfo;
import com.example.sociologicaldb_frontend.configuration.TableInfoService;
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
import java.util.List;

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
        rootItem.setExpanded(true);

        List<String> researchNames = TableInfo.getAllResearchNames();

        for (String researchName : researchNames) {
            TreeItem<String> researchItem = new TreeItem<>(researchName);
            rootItem.getChildren().add(researchItem);

            List<String> attributes = TableInfo.getAttributes(researchName);
            for (String attribute : attributes) {
                TreeItem<String> attributeItem = new TreeItem<>(attribute);
                researchItem.getChildren().add(attributeItem);
            }
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
