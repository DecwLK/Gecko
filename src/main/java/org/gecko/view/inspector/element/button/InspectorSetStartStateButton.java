package org.gecko.view.inspector.element.button;

import javafx.scene.control.ToggleButton;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.StateViewModel;

public class InspectorSetStartStateButton extends ToggleButton implements InspectorElement<ToggleButton> {
    public InspectorSetStartStateButton(ActionManager actionManager, StateViewModel stateViewModel) {
        setMaxWidth(Double.MAX_VALUE);
        setText("L:Set as start state");
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
