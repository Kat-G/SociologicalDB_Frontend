package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.CustomTreeNode;
import com.example.sociologicaldb_frontend.frames.requestInfo.HierarchyRequest;
import com.example.sociologicaldb_frontend.frames.requestInfo.NodeRequest;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@FxmlView("MaiStepTwoFrame.fxml")
public class MAIStepTwoFrameController {
    private final ConfigurableApplicationContext applicationContext;
    private final RestTemplate restTemplate;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox contentBox;
    @FXML
    private TreeView<CustomTreeNode> treeView;
    @FXML
    private TextField lowborderTextField;
    private final Set<String> visitedNodes = new HashSet<>();
    private final List<NodeRequest> nodeList = new ArrayList<>();
    private final List<String> inequality = new ArrayList<>();

    public void initData(TreeItem<CustomTreeNode> root) {
        treeView.setRoot(root);
    }

    @Autowired
    public MAIStepTwoFrameController(ConfigurableApplicationContext applicationContext, RestTemplate restTemplate) {
        this.applicationContext = applicationContext;
        this.restTemplate = restTemplate;
    }

    @FXML
    private void onAddRelationButtonClick() {
        addNewRelationSet();
    }

    @FXML
    private void onCalcMAIButtonClick() {
        if (lowborderTextField.getText() == null || lowborderTextField.getText().isEmpty()) {
            showAlert("Пожалуйста, введите значение нижней границы");
            return;
        }
        if(validateRelations()) {
            try {
                List<Map<String, Map<String, Double>>> response = sendHierarchyRequest();

                loadView(response);
            }
            catch (Exception e){
                System.out.println(e);
                return;
            }
            visitedNodes.clear();
            nodeList.clear();
            inequality.clear();
        }
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
            } else if (selectedItem.getValue().getName().equals(customTreeNode.getName())) {
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

    private void loadView(List<Map<String, Map<String, Double>>> response) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MAIResultFrameController.class);
        MAIResultFrameController controller = fxWeaver.getBean(MAIResultFrameController.class);
        controller.initData(response);
        Stage stage = (Stage) treeView.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("МАИ");
        stage.show();
    }

    public List<Map<String, Map<String, Double>>> sendHierarchyRequest() {
        String url = "http://localhost:8080/api/operations/hierarchy";

        createListOfNodes(treeView.getRoot());
        createListOfRelations();
        Double number = Double.parseDouble(lowborderTextField.getText());
        HierarchyRequest hierarchyRequest = new HierarchyRequest(nodeList,inequality,number);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HierarchyRequest> request = new HttpEntity<>(hierarchyRequest, headers);

        ResponseEntity<List> response = restTemplate.postForEntity(url, request, List.class);

        return response.getBody();
    }

    private void createListOfNodes(TreeItem<CustomTreeNode> root) {
        addNodeToList(root);
        for (TreeItem<CustomTreeNode> child : root.getChildren()) {
            createListOfNodes(child);
        }
    }

    private boolean validateRelations() {
        boolean atLeastOneValid = false;

        for (Node node : contentBox.getChildren()) {
            if (node instanceof HBox) {
                HBox relationSet = (HBox) node;
                String text1 = ((TextField) relationSet.getChildren().get(0)).getText();
                String operator = ((ComboBox<String>) relationSet.getChildren().get(2)).getValue();
                String value = ((TextField) relationSet.getChildren().get(3)).getText();
                String text2 = ((TextField) relationSet.getChildren().get(4)).getText();

                if (!text1.isEmpty() && operator != null && !value.isEmpty() && !text2.isEmpty()) {
                    atLeastOneValid = true;
                } else if (!text1.isEmpty() || operator != null || !value.isEmpty() || !text2.isEmpty()) {
                    showAlert("Пожалуйста, заполните все поля для каждого соотношения.");
                    return false;
                }
            }
        }

        if (!atLeastOneValid) {
            showAlert("Пожалуйста, введите хотя бы одно отношение.");
            return false;
        }

        return true;
    }

    private void addNodeToList(TreeItem<CustomTreeNode> node) {
        CustomTreeNode value = node.getValue();
        String nodeName = value.getName();

        if (!visitedNodes.contains(nodeName)) {
            if(node.getValue().isAttribute()){
                nodeList.add(new NodeRequest(nodeName, node.getParent().getValue().getName()));
            } else {
                String parentName = node.getParent() == null ? "" : node.getParent().getValue().getName();
                nodeList.add(new NodeRequest(nodeName, parentName));
            }
            visitedNodes.add(nodeName);
        }
    }

    private void createListOfRelations() {
        for (Node node : contentBox.getChildren()) {
            StringBuilder relations = new StringBuilder();
            if (node instanceof HBox relationSet) {
                String text1 = ((TextField) relationSet.getChildren().get(0)).getText();
                String operator = ((ComboBox<String>) relationSet.getChildren().get(2)).getValue();
                String value = ((TextField) relationSet.getChildren().get(3)).getText();
                String text2 = ((TextField) relationSet.getChildren().get(4)).getText();

                if (!text1.isEmpty() && operator != null && !value.isEmpty() && !text2.isEmpty()) {
                    relations.append(text1).append("|")
                            .append(operator).append("|")
                            .append(value).append("|")
                            .append(text2);
                }
            }
            inequality.add(relations.toString());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
