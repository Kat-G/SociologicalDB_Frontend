package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.CustomTreeNode;
import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
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
    @FXML
    private TreeView<CustomTreeNode> treeView;
    @FXML
    private Button addButton;
    @FXML
    private Button nextButton;
    @FXML
    private RadioButton addNodeRadioButton;
    @FXML
    private RadioButton addAttributeRadioButton;
    @FXML
    private ComboBox<String> attributeTableComboBox;
    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private TextField nodeTextField;

    @FXML
    public void initialize() {
        TreeItem<CustomTreeNode> rootItem = new TreeItem<>(new CustomTreeNode("Исследование", false));
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);

        ToggleGroup toggleGroup = new ToggleGroup();
        addNodeRadioButton.setToggleGroup(toggleGroup);
        addAttributeRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == addNodeRadioButton) {
                nodeTextField.setVisible(true);
                attributeTableComboBox.setVisible(false);
                attributeComboBox.setVisible(false);
            } else if (newValue == addAttributeRadioButton) {
                nodeTextField.setVisible(false);
                attributeTableComboBox.setVisible(true);
                attributeComboBox.setVisible(true);

                ObservableList<String> researchNames = FXCollections.observableArrayList(TablesInfo.getAllResearchNames());
                attributeTableComboBox.setItems(researchNames);
                attributeTableComboBox.getSelectionModel().selectedItemProperty().addListener((observableTwo, oldTableNameValue, newTableNameValue) -> {
                    if (newTableNameValue != null) {
                        ObservableList<String> attributeNames = FXCollections.observableArrayList(TablesInfo.getAttributesForResearch(newTableNameValue.toString()));

                        attributeComboBox.setItems(attributeNames);
                    }
                });
            }
        });
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        TreeItem<CustomTreeNode> selectedItem = (TreeItem<CustomTreeNode>)treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Пожалуйста, выберите элемент дерева.");
            return;
        }

        if (addNodeRadioButton.isSelected()) {
            if (selectedItem.getValue().isAttribute()) {
                showAlert("Нельзя добавлять узлы к атрибутам.");
                return;
            }

            String nodeName = nodeTextField.getText();
            if (nodeName.isEmpty()) {
                showAlert("Пожалуйста, введите имя узла.");
                return;
            }
            TreeItem<CustomTreeNode> newNode = new TreeItem<>(new CustomTreeNode(nodeName, false));
            selectedItem.getChildren().add(newNode);
            selectedItem.setExpanded(true);
            nodeTextField.clear();
        } else if (addAttributeRadioButton.isSelected()) {
            if (selectedItem.getValue().isAttribute()) {
                showAlert("Нельзя добавлять атрибуты к атрибутам.");
                return;
            }
            String attribute = attributeComboBox.getSelectionModel().getSelectedItem();
            String attributeTableName = attributeTableComboBox.getSelectionModel().getSelectedItem();
            if (attribute == null) {
                showAlert("Пожалуйста, выберите атрибут.");
                return;
            }
            TreeItem<CustomTreeNode> tableNode = null;
            for (TreeItem<CustomTreeNode> child : selectedItem.getChildren()) {
                if (child.getValue().equals(attributeTableName)) {
                    tableNode = child;
                    break;
                }
            }

            if (tableNode == null) {
                tableNode = new TreeItem<>(new CustomTreeNode(attributeTableName, true));
                selectedItem.getChildren().add(tableNode);
                selectedItem.setExpanded(true);
            }

            TreeItem<CustomTreeNode> newAttribute = new TreeItem<>(new CustomTreeNode(attribute, true));
            tableNode.getChildren().add(newAttribute);
            tableNode.setExpanded(true);

            attributeTableComboBox.getSelectionModel().clearSelection();
            attributeComboBox.getSelectionModel().clearSelection();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onNextButtonClick(ActionEvent event) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);

        // Загрузка нового окна и его контроллера
        Parent root = fxWeaver.loadView(MAIStepTwoFrameController.class);
        MAIStepTwoFrameController controller = fxWeaver.getBean(MAIStepTwoFrameController.class);

        // Передача данных в контроллер второго окна
        controller.initData(treeView.getRoot());

        // Закрытие текущего окна
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();

        // Открытие нового окна
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("МАИ. Задание соотношений");
        stage.show();
    }

    @Autowired
    public MAIStepOneFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
