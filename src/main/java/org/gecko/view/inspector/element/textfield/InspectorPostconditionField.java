package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextArea;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;

public class InspectorPostconditionField extends InspectorContractField {

    public InspectorPostconditionField(ActionManager actionManager, ContractViewModel contractViewModel) {
        setText(contractViewModel.getPostcondition());

        textProperty().addListener(event -> {
            actionManager.run(actionManager.getActionFactory().createChangePostconditionViewModelElementAction(contractViewModel, getText()));
        });
    }

    @Override
    public TextArea getControl() {
        return this;
    }
}
