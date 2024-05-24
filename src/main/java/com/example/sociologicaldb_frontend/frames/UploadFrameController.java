package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@FxmlView("UploadFrame.fxml")
public class UploadFrameController {

    private final FileChooser fileChooser = new FileChooser();
    private final RestTemplate restTemplate;
    private File selectedFile;

    @FXML
    private ComboBox<String> nameField;
    @FXML
    private TextField organizationName;
    @FXML
    private Button fileButton;
    @FXML
    private Label statusLabel;

    @Autowired
    public UploadFrameController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @FXML
    public void initialize() {
        ObservableList<String> researchNames = FXCollections.observableArrayList(TablesInfo.getAllResearchNames());
        nameField.setItems(researchNames);
    }

    @FXML
    public void onFileButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) fileButton.getScene().getWindow();
        fileChooser.setTitle("Выберите файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        selectedFile = fileChooser.showOpenDialog(stage);
    }

    @FXML
    public void onRequestButtonClick(ActionEvent actionEvent) {
        if (selectedFile == null) {
            statusLabel.setText("Пожалуйста, выберите файл.");
            return;
        }
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            statusLabel.setText("Пожалуйста, выберите исследование.");
            return;
        }
        if (organizationName.getText() == null || organizationName.getText().isEmpty()) {
            statusLabel.setText("Пожалуйста, введите название организации.");
            return;
        }

        String url = "http://localhost:8080/api/upload/csv";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(selectedFile));
        body.add("research", nameField.getValue());
        body.add("organizations", organizationName.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            statusLabel.setText(response.getBody());
        } catch (Exception e) {
            statusLabel.setText("Произошла ошибка при загрузке файла: " + e.getMessage());
        }
    }
}
