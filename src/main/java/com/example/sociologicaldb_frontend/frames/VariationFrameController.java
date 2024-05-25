package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
@FxmlView("VariationFrame.fxml")
public class VariationFrameController {
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

    private final RestTemplate restTemplate;

    @Autowired
    public VariationFrameController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        TablesInfo tablesInfo = new TablesInfo();
        tablesInfo.initializeTableView(tableName,attributeName);
    }

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
                .queryParam("table", tableName.getValue())
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

            for (Map<String, Object> pair : responseBody) {
                System.out.println(pair);
            }
            fillTableView(responseBody);
            fillPaiChart(responseBody);
            tabPane.setVisible(true);
        }
    }

    private void fillTableView(List<Map<String, Object>> responseData) {
        tableView.getColumns().clear();

        double columnWidth = tableView.getPrefWidth() / 2;

        Map<String, Object> firstRow = responseData.get(0);
        for (String key : firstRow.keySet()) {
            TableColumn<Map<String, Object>, String> column = new TableColumn<>(key);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(key).toString()));
            column.setPrefWidth(columnWidth);
            tableView.getColumns().add(column);
        }
        
        ObservableList<Map<String, Object>> tableData = FXCollections.observableArrayList(responseData);
        tableView.setItems(tableData);
    }
    
    private void fillPaiChart(List<Map<String, Object>> responseData) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map<String, Object> entry : responseData) {
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
