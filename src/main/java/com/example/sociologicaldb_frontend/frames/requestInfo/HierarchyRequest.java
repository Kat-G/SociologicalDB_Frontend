package com.example.sociologicaldb_frontend.frames.requestInfo;

import java.util.List;

public class HierarchyRequest {
    public List<NodeRequest> listOfNodes;
    public List<String> inequality;
    public Double number;

    public HierarchyRequest(List<NodeRequest> listOfNodes, List<String> inequality, Double number) {
        this.listOfNodes = listOfNodes;
        this.inequality = inequality;
        this.number = number;
    }
}
