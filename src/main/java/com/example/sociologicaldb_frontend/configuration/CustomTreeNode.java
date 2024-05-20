package com.example.sociologicaldb_frontend.configuration;

public class CustomTreeNode {
    private String name;
    private boolean isAttribute;
    private CustomTreeNode parent;

    public CustomTreeNode(){ }

    public CustomTreeNode(String name, boolean isAttribute, CustomTreeNode parent) {
        this.name = name;
        this.isAttribute = isAttribute;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public boolean isAttribute() {
        return isAttribute;
    }

    public CustomTreeNode getParent() {
        return parent;
    }

    public void setParent(CustomTreeNode parent) {
        this.parent = parent;
    }

    public boolean isSameParent(CustomTreeNode node){
        return this.getParent() == node.getParent();
    }

    public void copy(CustomTreeNode node){
        this.name = node.name;
        this.isAttribute = node.isAttribute;
        this.parent = node.parent;
    }

    @Override
    public String toString() {
        return name;
    }
}
