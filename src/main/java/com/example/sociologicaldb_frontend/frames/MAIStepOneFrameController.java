package com.example.sociologicaldb_frontend.frames;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("MaiStepOneFrame.fxml")
public class MAIStepOneFrameController {
    private ConfigurableApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;

    private double initialY = 0.0;
    private int count = 1;

    @FXML
    private void onAddTableButtonClick(ActionEvent event) {
        Label label = new Label("Таблица:");
        label.setLayoutX(15.0);
        label.setLayoutY(initialY + 50);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setLayoutX(110.0);
        comboBox.setLayoutY(initialY + 47);
        comboBox.setPrefWidth(150);
        comboBox.setId("tableComboBox"+count);

        CheckBox checkBox = new CheckBox("Без атрибутов");
        checkBox.setLayoutX(265.0);
        checkBox.setLayoutY(initialY + 51);
        checkBox.setId("attributeCheckBox"+count);

        CheckComboBox<String> checkComboBox = new CheckComboBox<>();
        checkComboBox.setLayoutX(380.0);
        checkComboBox.setLayoutY(initialY + 47);
        checkComboBox.setPrefWidth(150);
        checkComboBox.setId("attributeComboBox"+count);

        anchorPane.getChildren().addAll(label, comboBox, checkBox, checkComboBox);

        Button addButton = (Button)event.getSource();
        if (count++ < 4){
            addButton.setLayoutY(addButton.getLayoutY() + 50);
        }
        else
            addButton.setVisible(false);
        initialY += 50;
    }

    @FXML
    private void onFurtherButtonClick(ActionEvent event) {
        // открытие окна MaiStepTwoFrame, передача выбранных данных
    }

    @Autowired
    public MAIStepOneFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
