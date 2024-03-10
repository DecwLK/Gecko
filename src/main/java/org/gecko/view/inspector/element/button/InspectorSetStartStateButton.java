package org.gecko.view.inspector.element.button;

import javafx.scene.control.ToggleButton;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for setting a {@link StateViewModel} as start-state.
 */
public class InspectorSetStartStateButton extends ToggleButton implements InspectorElement<ToggleButton> {
    private static final String START_STATE_STYLE = "inspector-start-state-button";
    private static final String SET_START_STATE_BUTTON_KEY = "inspector_set_start_state";

    public InspectorSetStartStateButton(ActionManager actionManager, StateViewModel stateViewModel) {
        getStyleClass().add(START_STATE_STYLE);
        setMaxWidth(Double.MAX_VALUE);
        setText(ResourceHandler.getString(AbstractInspectorButton.BUTTONS, SET_START_STATE_BUTTON_KEY));
        update(stateViewModel.getIsStartState());
        stateViewModel.getIsStartStateProperty().addListener((observable, oldValue, newValue) -> {
            update(newValue);
        });

        setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createSetStartStateViewModelElementAction(stateViewModel));
        });
    }

    private void update(boolean newValue) {
        setSelected(newValue);
        setDisable(newValue);
    }

    @Override
    public ToggleButton getControl() {
        return this;
    }
}
