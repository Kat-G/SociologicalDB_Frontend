package com.example.sociologicaldb_frontend.configuration;

public class CustomTreeNode {
    private String name;
    private boolean isAttribute;

    public CustomTreeNode(String name, boolean isAttribute) {
        this.name = name;
        this.isAttribute = isAttribute;
    }

    public String getName() {
        return name;
    }

    public boolean isAttribute() {
        return isAttribute;
    }

    @Override
    public String toString() {
        return name;
    }
}
