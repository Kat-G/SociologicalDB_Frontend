package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.ConverterToCSV;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("MaiResultFrame.fxml")
public class MAIResultFrameController {
    @FXML
    private Button saveButton;
    private List<Map<String, Map<Double, Double>>> results = new ArrayList<>();
    @FXML
    private TableView<Map.Entry<String, Map<Double, Double>>> tableView;

    public MAIResultFrameController() {}

    public void initData(List<Map<String, Map<Double, Double>>> response) {
        this.results = response;

        fillTableView();
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
                //String text = ConverterToCSV.convertMaiInfo(results);
                //writer.write(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTableView() {
        tableView.getColumns().clear();

        double columnWidth = tableView.getPrefWidth() / 3;

        TableColumn<Map.Entry<String, Map<Double, Double>>, String> keyColumn = new TableColumn<>("Индекс");
        keyColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getKey()));
        keyColumn.setPrefWidth(columnWidth);
        keyColumn.setStyle("-fx-alignment: CENTER;");
        tableView.getColumns().add(keyColumn);

        TableColumn<Map.Entry<String, Map<Double, Double>>, Double> firstValueColumn = new TableColumn<>("Нижняя оценка");
        firstValueColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getValue().get("first"));
        });
        firstValueColumn.setPrefWidth(columnWidth);
        firstValueColumn.setStyle("-fx-alignment: CENTER;");
        tableView.getColumns().add(firstValueColumn);

        TableColumn<Map.Entry<String, Map<Double, Double>>, Double> secondValueColumn = new TableColumn<>("Верхняя оценка");
        secondValueColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getValue().get("second"));
        });
        secondValueColumn.setPrefWidth(columnWidth);
        secondValueColumn.setStyle("-fx-alignment: CENTER;");
        tableView.getColumns().add(secondValueColumn);

        ObservableList<Map.Entry<String, Map<Double, Double>>> tableData = FXCollections.observableArrayList();

        for (Map<String, Map<Double, Double>> map : results) {
            tableData.addAll(map.entrySet());
        }

        tableView.setItems(tableData);
    }

}
