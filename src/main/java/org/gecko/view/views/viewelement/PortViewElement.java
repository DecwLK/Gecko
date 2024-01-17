package org.gecko.view.views.viewelement;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class PortViewElement extends Pane {

    private final StringProperty nameProperty;

    public PortViewElement(StringProperty variableBlockNameProperty) {
        this.nameProperty = variableBlockNameProperty;
        nameProperty.bind(variableBlockNameProperty);
    }

    public void setName(String name) {
        nameProperty.setValue(name);
    }

    public String getName() {
        return nameProperty.getValue();
    }
}
