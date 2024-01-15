package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;

public class InspectorPreconditionField extends InspectorContractField {

    public InspectorPreconditionField(ActionManager actionManager, ContractViewModel contractViewModel) {
        setText(contractViewModel.getPrecondition());

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createChangePreconditionViewModelElementAction(contractViewModel, getText()));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
