package org.gecko.view.inspector.element.container;

import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorAreaField;
import org.gecko.view.inspector.element.textfield.InspectorCodeSystemField;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorCodeSystemContainer extends VBox implements InspectorElement<VBox> {

    public InspectorCodeSystemContainer(ActionManager actionManager, SystemViewModel viewModel) {
        if (!viewModel.getTarget().getChildren().isEmpty()) {
            return;
        }

        getChildren().add(new InspectorLabel(ResourceHandler.getString("Inspector", "code")));
        InspectorAreaField codeField = new InspectorCodeSystemField(actionManager, viewModel);
        codeField.prefWidthProperty().bind(widthProperty().subtract(50));
        getChildren().add(codeField);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
