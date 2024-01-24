package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextArea;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorInvariantField extends InspectorContractField {

    public InspectorInvariantField(ActionManager actionManager, RegionViewModel regionViewModel) {
        setText(regionViewModel.getInvariant());

        textProperty().addListener(event -> {
            actionManager.run(actionManager.getActionFactory().createChangeInvariantViewModelElementAction(regionViewModel, getText()));
        });
    }

    @Override
    public TextArea getControl() {
        return this;
    }
}
