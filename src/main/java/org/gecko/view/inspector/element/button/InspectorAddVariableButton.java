package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorAddVariableButton extends AbstractInspectorButton {

    private static final int WIDTH = 70;

    public InspectorAddVariableButton(
        ActionManager actionManager, SystemViewModel systemViewModel, Visibility visibility) {
        setText("L:Add");
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createCreatePortViewModelElementAction(systemViewModel));
            //TODO this (hopyfully) gets the added port. This should be replaced by something not as hacky.
            PortViewModel addedPort = systemViewModel.getPortsProperty().getLast();
            //This is not an action because it should not be undoable.
            addedPort.setVisibility(visibility);
            addedPort.updateTarget();
        });
    }
}
