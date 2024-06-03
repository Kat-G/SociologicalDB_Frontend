package com.example.sociologicaldb_frontend.configuration;

import jakarta.annotation.PostConstruct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TableInfo {
    private final TableInfoService tableInfoService;
    private List<Map<String, List<String>>> tableInfo;
    private static Map<String, List<String>> researchAttributes;

    @Autowired
    public TableInfo(TableInfoService tableInfoService) {
        this.tableInfoService = tableInfoService;
    }

    @PostConstruct
    public void init() {
        this.researchAttributes = tableInfoService.fetchTableInfo();
    }

    public static List<String> getAttributes(String researchName) {
        return researchAttributes.getOrDefault(researchName, List.of());
    }

    public static List<String> getAllResearchNames() {
        return new ArrayList<>(researchAttributes.keySet());
    }

    public static void initializeTableView(ComboBox<String> tableName, ComboBox<String> attributeName) {
        ObservableList<String> researchNames = FXCollections.observableArrayList(TableInfo.getAllResearchNames());

        tableName.setItems(researchNames);

        tableName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<String> attributeNames = FXCollections.observableArrayList(TableInfo.getAttributes(newValue.toString()));
                attributeName.setItems(attributeNames);
            }
        });
    }

}
