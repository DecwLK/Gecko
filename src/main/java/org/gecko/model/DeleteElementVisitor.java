package org.gecko.model;

import lombok.Getter;

@Getter
public class DeleteElementVisitor implements ElementVisitor {
    private System parentSystem;
    @Override
    public void visit(State state) {
        //TODO stub
    }
}
