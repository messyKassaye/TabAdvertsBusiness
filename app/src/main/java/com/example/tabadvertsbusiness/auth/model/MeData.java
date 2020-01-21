package com.example.tabadvertsbusiness.auth.model;

public class MeData {
    private Attribute attribute;
    private Relations relations;

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Relations getRelations() {
        return relations;
    }

    public void setRelations(Relations relations) {
        this.relations = relations;
    }
}
