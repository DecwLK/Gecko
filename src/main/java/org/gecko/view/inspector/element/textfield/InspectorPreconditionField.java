package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextArea;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.ContractViewModel;

public class InspectorPreconditionField extends InspectorContractField {

    public InspectorPreconditionField(ActionManager actionManager, ContractViewModel contractViewModel) {
        setText(contractViewModel.getPrecondition());

        textProperty().addListener(event -> {
            actionManager.run(actionManager.getActionFactory()
                .createChangePreconditionViewModelElementAction(contractViewModel, getText()));
        });
    }

    @Override
    public TextArea getControl() {
        return this;
    }
}
