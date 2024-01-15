package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;

public class InspectorPostconditionField extends InspectorContractField {

    public InspectorPostconditionField(ActionManager actionManager, ContractViewModel contractViewModel) {
        setText(contractViewModel.getPostcondition());

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createChangePostconditionViewModelElementAction(contractViewModel, getText()));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
