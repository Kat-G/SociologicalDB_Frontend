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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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

    private ConfigurableApplicationContext applicationContext;
    final FileChooser fileChooser = new FileChooser();
    @FXML
    private ComboBox nameField;
    @FXML
    private TextField organizationName;
    @FXML
    private Button fileButton;
    @FXML
    private Label statusLabel;
    private File selectedFile;

    @Autowired
    public UploadFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        ObservableList<String> researchNames = FXCollections.observableArrayList(TablesInfo.getAllResearchNames());
        nameField.setItems(researchNames);
    }

    public void onFileButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage)fileButton.getScene().getWindow();
        fileChooser.setTitle("Выберите файл");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(stage);
    }

    public void onRequestButtonClick(ActionEvent actionEvent) {
        String url = "http://localhost:8080/api/upload/csv";
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(selectedFile));
        body.add("research", nameField.getValue());
        body.add("organizations", organizationName.getText());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        statusLabel.setText(response.getBody());
        // отправка данных - nameField.getValue(), organizationName.getText(), selectedFile
        // проверка на null перед

        // при получении ответа - вывод сообщения в statusLabel
    }

}
