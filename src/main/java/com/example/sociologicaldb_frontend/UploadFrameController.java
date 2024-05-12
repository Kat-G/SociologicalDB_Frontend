package com.example.sociologicaldb_frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
@FxmlView("UploadFrame.fxml")
public class UploadFrameController {

    private ConfigurableApplicationContext applicationContext;
    final FileChooser fileChooser = new FileChooser();
    @FXML
    private ComboBox nameField;
    @FXML
    private TextField organizationName;
    @FXML
    private Button fileButton;
    private File selectedFile;

    @Autowired
    public UploadFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onFileButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage)fileButton.getScene().getWindow();
        fileChooser.setTitle("Выберите файл");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(stage);
    }

    public void onRequestButtonClick(ActionEvent actionEvent) {
        // отправка данных - nameField.getValue(), organizationName.getText(), selectedFile
        // проверка на null перед

        // при получении ответа - вывод сообщения в statusLabel
    }

}
