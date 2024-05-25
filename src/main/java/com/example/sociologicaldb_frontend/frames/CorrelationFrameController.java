package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@FxmlView("CorrelationFrame.fxml")
public class CorrelationFrameController {

    private final RestTemplate restTemplate;
    @FXML
    private ComboBox<String> tableNameOne;
    @FXML
    private CheckBox corrCheckBox;
    @FXML
    private Label tableNameTwoLabel;
    @FXML
    private ComboBox<String> tableNameTwo;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView tableView;

    @Autowired
    public CorrelationFrameController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        ObservableList<String> researchNames = FXCollections.observableArrayList(TablesInfo.getAllResearchNames());
        tableNameOne.setItems(researchNames);

        tableNameOne.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSecondComboBox(newValue, researchNames);
            }
        });
    }

    private void updateSecondComboBox(String selectedResearch, ObservableList<String> researchNames) {
        ObservableList<String> updatedResearchNames = FXCollections.observableArrayList(researchNames);
        updatedResearchNames.remove(selectedResearch);
        tableNameTwo.setItems(updatedResearchNames);
    }

    @FXML
    public void onCorrCheckBoxClick(ActionEvent actionEvent) {
        boolean isSelected = corrCheckBox.isSelected();
        tableNameTwoLabel.setVisible(!isSelected);
        tableNameTwo.setVisible(!isSelected);
    }

    @FXML
    public void onCalcCorrelationButtonClick(ActionEvent actionEvent) {
        if (tableNameOne.getValue() == null || tableNameOne.getValue().isEmpty()) {
            showAlert();
            return;
        }
        if (!corrCheckBox.isSelected() && (tableNameTwo.getValue() == null || tableNameTwo.getValue().isEmpty())) {
            showAlert();
            return;
        }

        String baseUrl = "http://localhost:8080/api/operations/correlation";
        URI uri;
        if (corrCheckBox.isSelected()){
            uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("table1", tableNameOne.getValue())
                    .encode()
                    .build()
                    .toUri();
        }
        else {
            uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("table1", tableNameOne.getValue())
                    .queryParam("table2", tableNameTwo.getValue())
                    .encode()
                    .build()
                    .toUri();
        }
        List<List<Double>> responseBody = null;
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(uri, List.class);
            responseBody = response.getBody();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        if (responseBody != null) {
            for (List<Double> pair : responseBody) {
                System.out.println(pair);
            }

            fillTableView(responseBody);

            tabPane.setVisible(true);
        }

    }

    private void fillTableView(List<List<Double>> responseData) {
        int columnCount = responseData.get(0).size();

        tableView.getColumns().clear();

        double columnWidth = tableView.getWidth() / columnCount;

        for (int i = 0; i < columnCount; i++) {
            TableColumn<ObservableList<Double>, Double> column = new TableColumn<>("Column " + (i + 1));
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
            column.setMinWidth(columnWidth);
            tableView.getColumns().add(column);
        }

        ObservableList<ObservableList<Double>> tableData = FXCollections.observableArrayList();
        for (List<Double> rowData : responseData) {
            ObservableList<Double> row = FXCollections.observableArrayList();
            row.addAll(rowData);
            tableData.add(row);
        }

        tableView.setItems(tableData);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText("Пожалуйста, выберите название таблицы");
        alert.showAndWait();
    }
}
