package com.example.sociologicaldb_frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("VariationFrame.fxml")
public class VariationFrameController {
    @FXML
    private ComboBox tableName;
    @FXML
    private ComboBox attributeName;
    @FXML
    private CheckBox varCheckBox;
    @FXML
    TabPane tabPane;
    @FXML
    private TableView tableView;
    @FXML
    private AnchorPane graphPane;

    private ConfigurableApplicationContext applicationContext;

    @Autowired
    public VariationFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onCalcVariationButtonClick(ActionEvent actionEvent) {
        // отправка данных - tableName.getValue(), attributeName.getValue(), varCheckBox.isSelected()

        // при получении ответа - заполнение tableView
        // вкладка graphPane - отрисовка круговой диаграммы (?)
        tabPane.setVisible(true);
    }
}
