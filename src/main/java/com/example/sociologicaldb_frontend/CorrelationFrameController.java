package com.example.sociologicaldb_frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;

import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@FxmlView("CorrelationFrame.fxml")
public class CorrelationFrameController {

    private final ConfigurableApplicationContext applicationContext;
    @FXML
    private CheckBox corrCheckBox;
    @FXML
    private Label tableNameTwoLabel;
    @FXML
    private ComboBox tableNameTwo;

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
}
