package org.gecko.view.inspector.element.container;

import javafx.scene.layout.VBox;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorVariableField extends VBox implements InspectorElement<VBox> {
    public InspectorVariableField(ActionManager actionManager, SystemViewModel systemViewModel) {
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
