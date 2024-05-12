package com.example.sociologicaldb_frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Autowired
    public MainFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        initializeTreeView();
        //respLabel.setText("1700");
        //resLabel.setText("4");
    }

    public void initializeTreeView() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/sociologicaldb_frontend/info/treeview-info.txt"));
            String line;
            TreeItem<String> rootItem = new TreeItem<>("Исследования");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                TreeItem<String> item = new TreeItem<>(parts[0]);
                rootItem.getChildren().add(item);

                for (int i = 1; i < parts.length; i++) {
                    item.getChildren().add(new TreeItem<>(parts[i]));
                }
            }

            treeView.setRoot(rootItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAploadButtonClick(ActionEvent actionEvent) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(UploadFrameController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Загрузка исследования");
        stage.show();
    }

    public void onCorrelationButtonClick(ActionEvent actionEvent) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(CorrelationFrameController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Корреляционная матрица");
        stage.show();
    }
    public void onVariationButtonClick(ActionEvent actionEvent) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(VariationFrameController.class);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Вариационный ряд");
        stage.show();
    }
}
