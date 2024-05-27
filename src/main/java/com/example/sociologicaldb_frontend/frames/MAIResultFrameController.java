package com.example.sociologicaldb_frontend.frames;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("MaiResultFrame.fxml")
public class MAIResultFrameController {
    private List<Map<String, Map<Double, Double>>> results = new ArrayList<>();
    @FXML
    private TableView<Map.Entry<String, Map<Double, Double>>> tableView;

    public MAIResultFrameController() {}

    public void initData(List<Map<String, Map<Double, Double>>> response) {
        this.results = response;

        fillTableView();
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
