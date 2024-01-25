package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorPriorityField extends TextField implements InspectorElement<TextField> {

    public InspectorPriorityField(ActionManager actionManager, EdgeViewModel edgeViewModel) {
        setText(String.valueOf(edgeViewModel.getPriority()));

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory()
                .createModifyEdgeViewModelPriorityAction(edgeViewModel, Integer.parseInt(getText())));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
