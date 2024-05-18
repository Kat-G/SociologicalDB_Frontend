package com.example.sociologicaldb_frontend.frames;

import com.example.sociologicaldb_frontend.configuration.CustomTreeNode;
import com.example.sociologicaldb_frontend.configuration.TablesInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("MaiStepTwoFrame.fxml")
public class MAIStepTwoFrameController {
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

    }

    public void initData(TreeItem<CustomTreeNode> root) {
        treeView.setRoot(root);
    }


    @Autowired
    public MAIStepTwoFrameController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
