package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.ConverterToCSV;
import com.example.sociologicaldb_frontend.configuration.TableInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("VariationFrame.fxml")
public class VariationFrameController {
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> tableName;
    @FXML
    private ComboBox<String> attributeName;
    @FXML
    private CheckBox varCheckBox;
    @FXML
    TabPane tabPane;
    @FXML
    private TableView tableView;
    @FXML
    private PieChart pieChart;

    private List<Map<String, Object>> responseData = new ArrayList<>();
    private final RestTemplate restTemplate;

    @Autowired
    public VariationFrameController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        TableInfo.initializeTableView(tableName,attributeName);
    }

    @FXML
    public void onCalcVariationButtonClick(ActionEvent actionEvent) {
        if (tableName.getValue() == null || tableName.getValue().isEmpty()) {
            showAlert("Пожалуйста, выберите название таблицы");
            return;
        }
        if (attributeName.getValue() == null || attributeName.getValue().isEmpty()) {
            showAlert("Пожалуйста, выберите название атрибута");
            return;
        }

        String baseUrl = "http://localhost:8080/api/operations/variation";

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("table",  tableName.getValue())
                .queryParam("attribute", attributeName.getValue())
                .queryParam("variationType", varCheckBox.isSelected())
                .encode()
                .build()
                .toUri();
        List<Map<String, Object>> responseBody = null;
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(uri, List.class);
            responseBody = response.getBody();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        if (responseBody != null) {
            responseData = responseBody;
            fillTableView();
            fillPaiChart();

            saveButton.setVisible(true);
            tabPane.setVisible(true);
        }
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"))) {
                String text = ConverterToCSV.convertVarInfo(responseData);
                writer.write(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTableView() {
        tableView.getColumns().clear();

        double columnWidth = tableView.getPrefWidth() / 2;

        Map<String, String> columnTitles = Map.of(
                "first", "Интервал",
                "second", "Количество"
        );

        Map<String, Object> firstRow = responseData.get(0);
        for (String key : firstRow.keySet()) {
            String columnTitle = columnTitles.getOrDefault(key, key);
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(columnTitle);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(key).toString()));
            column.setPrefWidth(columnWidth);
            column.setStyle("-fx-alignment: CENTER;");
            tableView.getColumns().add(column);
        }
        
        ObservableList<Map<String, Object>> tableData = FXCollections.observableArrayList(responseData);
        tableView.setItems(tableData);
    }
    
    private void fillPaiChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<Map<String, Object>> data;
        data = responseData;
        data.remove(data.size() - 1);
        for (Map<String, Object> entry : data) {
            String label = entry.get("first").toString();
            Double value = Double.valueOf(entry.get("second").toString());
            pieChartData.add(new PieChart.Data(label, value));
        }

        pieChart.setData(pieChartData);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
