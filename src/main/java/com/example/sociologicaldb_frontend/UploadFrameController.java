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

import static jdk.jfr.consumer.EventStream.openFile;

@Component
@FxmlView("UploadFrame.fxml")
public class UploadFrameController {

    private ConfigurableApplicationContext applicationContext;
    final FileChooser fileChooser = new FileChooser();
    @FXML
    private Button fileButton;

    @Autowired
    public UploadFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onFileButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage)fileButton.getScene().getWindow();
        fileChooser.setTitle("Выберите файл");
        File selectedFile = fileChooser.showOpenDialog(stage);
    }

}
