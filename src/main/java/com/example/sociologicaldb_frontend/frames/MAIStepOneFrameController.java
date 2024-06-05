package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.CustomTreeNode;
import com.example.sociologicaldb_frontend.configuration.TableInfo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("MaiStepOneFrame.fxml")
public class MAIStepOneFrameController {
    private final ConfigurableApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TreeView<CustomTreeNode> treeView;
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
        TreeItem<CustomTreeNode> rootItem = new TreeItem<>(new CustomTreeNode("Исследование", false, null));
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

                TableInfo.initializeTableView(attributeTableComboBox,attributeComboBox);
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

            TreeItem<CustomTreeNode> newNode = new TreeItem<>(new CustomTreeNode(nodeName, false,selectedItem.getValue()));
            selectedItem.getChildren().add(newNode);
            selectedItem.setExpanded(true);
            nodeTextField.clear();
        } else if (addAttributeRadioButton.isSelected()) {
            if (selectedItem.getValue().isAttribute()) {
                showAlert("Нельзя добавлять атрибуты к атрибутам.");
                return;
            }
            if (attributeComboBox.getValue() == null) {
                showAlert("Пожалуйста, заполните все значения.");
                return;
            }
            String attributeTableName = attributeTableComboBox.getSelectionModel().getSelectedItem();
            String attribute = attributeTableName + "." + attributeComboBox.getSelectionModel().getSelectedItem();

            for (TreeItem<CustomTreeNode> child : treeView.getRoot().getChildren()) {
                if (child.getValue().getName().equals(attribute)) {
                    showAlert("Нельзя добавлять одинаковые элементы.");
                    return;
                }
            }

            TreeItem<CustomTreeNode> newAttribute = new TreeItem<>(new CustomTreeNode(attribute, true, selectedItem.getValue()));
            selectedItem.getChildren().add(newAttribute);
            selectedItem.setExpanded(true);

            attributeTableComboBox.getSelectionModel().clearSelection();
            attributeComboBox.getSelectionModel().clearSelection();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean allPathsEndWithAttributes(TreeItem<CustomTreeNode> root) {
        return checkNode(root);
    }

    private boolean checkNode(TreeItem<CustomTreeNode> node) {
        if (node == null) {
            return true;
        }

        if (node.getChildren().isEmpty()) {
            return node.getValue().isAttribute();
        }

        for (TreeItem<CustomTreeNode> child : node.getChildren()) {
            if (!checkNode(child)) {
                return false;
            }
        }

        return true;
    }

    @FXML
    private void onNextButtonClick(ActionEvent event) {
        if (!allPathsEndWithAttributes(treeView.getRoot())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Все узлы должны заканчиваться атрибутами.");
            alert.showAndWait();
        } else {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            Parent root = fxWeaver.loadView(MAIStepTwoFrameController.class);
            MAIStepTwoFrameController controller = fxWeaver.getBean(MAIStepTwoFrameController.class);
            controller.initData(treeView.getRoot());
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("МАИ. Задание соотношений");
            stage.show();
        }
    }

    @Autowired
    public MAIStepOneFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
