package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.RegionViewModel;

public class InspectorInvariantField extends InspectorContractField {

    public InspectorInvariantField(ActionManager actionManager, RegionViewModel regionViewModel) {
        setText(regionViewModel.getInvariant());

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createChangeInvariantViewModelElementAction(regionViewModel, getText()));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
