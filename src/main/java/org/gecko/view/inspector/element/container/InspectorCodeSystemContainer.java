package org.gecko.view.inspector.element.container;

import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorAreaField;
import org.gecko.view.inspector.element.textfield.InspectorCodeSystemField;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorCodeSystemContainer extends VBox implements InspectorElement<VBox> {

    public InspectorCodeSystemContainer(ActionManager actionManager, SystemViewModel viewModel) {
        getChildren().add(new InspectorLabel("Code"));
        InspectorAreaField codeField = new InspectorCodeSystemField(actionManager, viewModel);
        codeField.prefWidthProperty().bind(widthProperty().subtract(50));
        codeField.setPrefHeight(100);
        getChildren().add(codeField);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
