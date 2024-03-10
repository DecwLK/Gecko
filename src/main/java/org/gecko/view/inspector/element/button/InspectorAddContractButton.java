package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for adding a
 * {@link org.gecko.viewmodel.ContractViewModel ContractViewModel} to a given {@link StateViewModel}.
 */
public class InspectorAddContractButton extends AbstractInspectorButton {
    private static final String STYLE = "inspector-add-button";
    private static final int WIDTH = 70;
    private static final String ADD_CONTRACT_BUTTON_KEY = "inspector_add_contract";

    public InspectorAddContractButton(ActionManager actionManager, StateViewModel stateViewModel) {
        getStyleClass().add(STYLE);
        setText(ResourceHandler.getString(BUTTONS, ADD_CONTRACT_BUTTON_KEY));
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createCreateContractViewModelElementAction(stateViewModel));
        });
    }
}
