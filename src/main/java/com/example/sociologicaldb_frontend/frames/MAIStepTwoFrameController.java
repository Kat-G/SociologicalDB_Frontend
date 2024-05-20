package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.CustomTreeNode;
import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("MaiStepTwoFrame.fxml")
public class MAIStepTwoFrameController {
    private ConfigurableApplicationContext applicationContext;

    @FXML
    private VBox contentBox;
    @FXML
    private TreeView<CustomTreeNode> treeView;

    public void initData(TreeItem<CustomTreeNode> root) {
        treeView.setRoot(root);
    }

    @FXML
    private void onAddRelationButtonClick() {
        addNewRelationSet();
    }

    @FXML
    private void onCalcMAIButtonClick() {
        // проверка ввода данных
        // отправка на сервер и получение результата
        // передача результата следующему контроллеру и открытие нового окна
    }

    private void addNewRelationSet() {
        TextField textField1 = new TextField();
        textField1.setEditable(false);
        textField1.setPrefWidth(115.0);

        CustomTreeNode customTreeNode = new CustomTreeNode();

        Button addButton1 = new Button("+");
        addButton1.setMinWidth(30);
        addButton1.setOnAction(event -> {
            TreeItem<CustomTreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();

            customTreeNode.copy(selectedItem.getValue());

            if (selectedItem != null) {
                textField1.setText(selectedItem.getValue().getName());
            } else {
                showAlert("Пожалуйста, выберите элемент из дерева.");
            }
        });

        ComboBox<String> operatorComboBox = new ComboBox<>();
        operatorComboBox.setMinWidth(40);
        operatorComboBox.getItems().addAll("=", ">", "<", ">=", "<=");

        TextField editableTextField = new TextField();
        editableTextField.setText("1");
        editableTextField.setPrefWidth(40.0);

        TextField textField2 = new TextField();
        textField2.setEditable(false);
        textField2.setPrefWidth(115.0);

        Button addButton2 = new Button("+");
        addButton2.setMinWidth(30);
        addButton2.setOnAction(event -> {
            TreeItem<CustomTreeNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert("Пожалуйста, выберите элемент из дерева.");
            } else if (selectedItem.getValue().getName() == customTreeNode.getName()) {
                showAlert("Пожалуйста, выберите разные элементы из дерева.");
            } else if (!selectedItem.getValue().isSameParent(customTreeNode)) {
                showAlert("Пожалуйста, выберите элементы одного уровня иерархии.");
            } else {
                textField2.setText(selectedItem.getValue().getName());
            }
        });

        HBox relationSet = new HBox(0, textField1, addButton1, operatorComboBox, editableTextField, textField2, addButton2);

        contentBox.getChildren().add(relationSet);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Autowired
    public MAIStepTwoFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
