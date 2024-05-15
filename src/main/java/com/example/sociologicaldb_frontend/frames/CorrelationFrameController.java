package com.example.sociologicaldb_frontend.frames;

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
    private ComboBox tableNameOne;
    @FXML
    private CheckBox corrCheckBox;
    @FXML
    private Label tableNameTwoLabel;
    @FXML
    private ComboBox tableNameTwo;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView tableView;

    @Autowired
    public CorrelationFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onCorrCheckBoxClick(ActionEvent actionEvent) {
        if (corrCheckBox.isSelected()){
            tableNameTwoLabel.setVisible(false);
            tableNameTwo.setVisible(false);
        }
        else{
            tableNameTwoLabel.setVisible(true);
            tableNameTwo.setVisible(true);
        }
    }

    public void onCalcCorrelationButtonClick(ActionEvent actionEvent) {
        // отправка данных - если corrCheckBox.isSelected() - отправлка tableNameOne.getValue()
        //    иначе -  tableNameOne.getValue(), tableNameTwo.getValue()

        // при получении ответа - заполнение tableView
        tabPane.setVisible(true);
    }
}
