package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.ConverterToCSV;
import com.example.sociologicaldb_frontend.configuration.TableInfo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

@Component
@FxmlView("CorrelationFrame.fxml")
public class CorrelationFrameController {
    private final RestTemplate restTemplate;
    @FXML
    private Button saveButton;
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
    private List<List<Double>> responseData = new ArrayList<>();

    @Autowired
    public CorrelationFrameController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        ObservableList<String> researchNames = FXCollections.observableArrayList(TableInfo.getAllResearchNames());
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
                    .queryParam("table2", tableNameTwo.getValue())
                    .encode()
                    .build()
                    .toUri();
        }
        else {
            uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("table1", tableNameOne.getValue())
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
            responseData = responseBody;
            if(corrCheckBox.isSelected()) {
                fillTableView(TableInfo.getAttributes(tableNameOne.getValue()), TableInfo.getAttributes(tableNameOne.getValue()));
            }
            else {
                fillTableView(TableInfo.getAttributes(tableNameOne.getValue()), TableInfo.getAttributes(tableNameTwo.getValue()));
            }

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
                String text;
                if(corrCheckBox.isSelected()) {
                    text = ConverterToCSV.convertCorrInfo(responseData, TableInfo.getAttributes(tableNameOne.getValue()), TableInfo.getAttributes(tableNameOne.getValue()));
                }
                else {
                    text = ConverterToCSV.convertCorrInfo(responseData, TableInfo.getAttributes(tableNameOne.getValue()), TableInfo.getAttributes(tableNameTwo.getValue()));
                }
                writer.write(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTableView(List<String> rowAttributes, List<String> columnAttributes) {
        tableView.getColumns().clear();

        TableColumn<ObservableList<Double>, String> rowHeaderColumn = new TableColumn<>("");
        rowHeaderColumn.setCellValueFactory(cellData -> {
            int rowIndex = tableView.getItems().indexOf(cellData.getValue());
            if (rowIndex >= 0 && rowIndex < rowAttributes.size()) {
                return new SimpleStringProperty(rowAttributes.get(rowIndex));
            } else {
                return new SimpleStringProperty("");
            }
        });
        rowHeaderColumn.setMinWidth(tableView.getWidth() / (columnAttributes.size() + 1));
        rowHeaderColumn.setStyle("-fx-alignment: CENTER;");
        tableView.getColumns().add(rowHeaderColumn);

        for (int i = 0; i < columnAttributes.size(); i++) {
            TableColumn<ObservableList<Double>, Double> column = new TableColumn<>(columnAttributes.get(i));
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> {
                if (cellData.getValue().size() > columnIndex) {
                    return new SimpleObjectProperty<>(cellData.getValue().get(columnIndex));
                } else {
                    return new SimpleObjectProperty<>(null);
                }
            });
            column.setMinWidth(tableView.getWidth() / (columnAttributes.size() + 1));
            column.setStyle("-fx-alignment: CENTER;");
            tableView.getColumns().add(column);
        }

        ObservableList<ObservableList<Double>> tableData = FXCollections.observableArrayList();

        for (List<Double> rowData : responseData) {
            ObservableList<Double> row = FXCollections.observableArrayList(rowData);
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
