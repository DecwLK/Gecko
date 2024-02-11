package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.StateViewModel;

/** Represents a type of {@link AbstractInspectorButton} used for adding a {@link org.gecko.viewmodel.ContractViewModel ContractViewModel} to a given {@link StateViewModel}.  */
public class InspectorAddContractButton extends AbstractInspectorButton {
    private static final int WIDTH = 70;

    public InspectorAddContractButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setText(ResourceHandler.getString("Buttons", "inspector_add_contract"));
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createCreateContractViewModelElementAction(stateViewModel));
        });
    }
}
