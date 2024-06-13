package com.example.sociologicaldb_frontend.configuration;

import java.util.*;

public class ConverterToCSV {
    public static String convertCorrInfo(List<List<Double>> responseData,List<String> rowHeaders, List<String> columnHeaders) {
        StringBuilder csvBuilder = new StringBuilder();

        csvBuilder.append(";");
        for (String columnHeader : columnHeaders) {
            csvBuilder.append(columnHeader).append(";");
        }
        csvBuilder.append("\n");

        for (int i = 0; i < responseData.size(); i++) {
            String rowHeader = i < rowHeaders.size() ? rowHeaders.get(i) : "";
            csvBuilder.append(rowHeader).append(";");
            List<Double> row = responseData.get(i);
            StringJoiner joiner = new StringJoiner(";");
            for (Double value : row) {
                joiner.add(value.toString());
            }
            csvBuilder.append(joiner.toString()).append("\n");
        }
        return csvBuilder.toString();
    }

    public static String convertVarInfo(List<Map<String, Object>> data) {
        StringBuilder csvBuilder = new StringBuilder();

        if (!data.isEmpty()) {
            Map<String, Object> firstRow = data.get(0);
            StringJoiner headerJoiner = new StringJoiner(";");

            for (Map<String, Object> row : data) {
                StringJoiner rowJoiner = new StringJoiner(";");
                for (Object value : row.values()) {
                    rowJoiner.add(value.toString());
                }
                csvBuilder.append(rowJoiner.toString()).append("\n");
            }
        }

        return csvBuilder.toString();
    }

    public static String convertMaiInfo(List<Map<String, Map<String, Double>>> data) {
        StringBuilder csvBuilder = new StringBuilder();

        if (!data.isEmpty()) {
            Set<String> nestedKeys = new LinkedHashSet<>();
            for (Map<String, Map<String, Double>> row : data) {
                for (Map<String, Double> values : row.values()) {
                    nestedKeys.addAll(values.keySet());
                }
            }

            for (Map<String, Map<String, Double>> row : data) {
                for (Map.Entry<String, Map<String, Double>> entry : row.entrySet()) {
                    String rowKey = entry.getKey();
                    Map<String, Double> values = entry.getValue();
                    csvBuilder.append(rowKey).append(";");
                    StringJoiner rowJoiner = new StringJoiner(";");
                    for (String nestedKey : nestedKeys) {
                        Double value = values.get(nestedKey);
                        rowJoiner.add(value != null ? value.toString() : "");
                    }
                    csvBuilder.append(rowJoiner.toString()).append("\n");
                }
            }
        }

        return csvBuilder.toString();
    }
}
