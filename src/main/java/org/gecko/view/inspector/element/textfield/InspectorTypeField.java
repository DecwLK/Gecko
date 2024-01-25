package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.PortViewModel;

public class InspectorTypeField extends TextField implements InspectorElement<TextField> {

    public InspectorTypeField(ActionManager actionManager, PortViewModel viewModel) {
        setText(viewModel.getType());

        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createChangeTypePortViewModelElementAction(viewModel, getText()));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
