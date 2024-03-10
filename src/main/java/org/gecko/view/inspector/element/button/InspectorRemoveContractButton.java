package org.gecko.view.inspector.element.button;

import javafx.scene.control.Tooltip;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for removing a {@link ContractViewModel} from a
 * {@link StateViewModel}.
 */
public class InspectorRemoveContractButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-remove-button";

    public InspectorRemoveContractButton(
        ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        getStyleClass().add(ICON_STYLE_NAME);
        setTooltip(new Tooltip(ResourceHandler.getString("Tooltips", "inspector_remove_contract")));
        setOnAction(event -> {
            getParent().requestFocus();
            actionManager.run(actionManager.getActionFactory()
                .createDeleteContractViewModelAction(stateViewModel, contractViewModel));
        });
    }
}
