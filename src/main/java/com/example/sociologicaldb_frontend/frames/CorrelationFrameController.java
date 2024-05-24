package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("CorrelationFrame.fxml")
public class CorrelationFrameController {

    private final ConfigurableApplicationContext applicationContext;
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
    private TableView<?> tableView;

    @Autowired
    public CorrelationFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
        // отправка данных - если corrCheckBox.isSelected() - отправлка tableNameOne.getValue()
        //    иначе -  tableNameOne.getValue(), tableNameTwo.getValue()

        // при получении ответа - заполнение tableView
        tabPane.setVisible(true);
    }
}
