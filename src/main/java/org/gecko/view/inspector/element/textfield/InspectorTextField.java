package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.Renamable;

public class InspectorTextField extends TextField implements InspectorElement<TextField> {

    public InspectorTextField(ActionManager actionManager, Renamable renamable) {
        setText(renamable.getName());

        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createRenameViewModelElementAction(renamable, getText()));
        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
