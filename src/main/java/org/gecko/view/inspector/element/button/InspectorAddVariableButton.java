package org.gecko.view.inspector.element.button;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a type of {@link AbstractInspectorButton} used for adding a {@link PortViewModel} to a given
 * {@link SystemViewModel} with a given {@link Visibility}.
 */
public class InspectorAddVariableButton extends AbstractInspectorButton {

    private static final int WIDTH = 70;

    public InspectorAddVariableButton(
        ActionManager actionManager, SystemViewModel systemViewModel, Visibility visibility) {
        setText(ResourceHandler.getString("Buttons", "inspector_add_variable"));
        setPrefWidth(WIDTH);
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createCreatePortViewModelElementAction(systemViewModel));
            //TODO this (hopyfully) gets the added port. This should be replaced by something not as hacky.
            PortViewModel addedPort = systemViewModel.getPortsProperty().getLast();
            //This is not an action because it should not be undoable.
            addedPort.setVisibility(visibility);
            try {
                addedPort.updateTarget();
            } catch (Exception e) {
                throw new RuntimeException("Failed while changeing a port's visibility. This should never happen.");
            }
        });
    }
}
