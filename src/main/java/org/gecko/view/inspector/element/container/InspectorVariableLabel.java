package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.button.InspectorAddVariableButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorVariableLabel extends LabeledInspectorElement {

    public InspectorVariableLabel(ActionManager actionManager, SystemViewModel viewModel, Visibility visibility) {
        super(new InspectorLabel(getLabel(visibility)),
            new InspectorAddVariableButton(actionManager, viewModel, visibility));
    }

    //Cant be public because it is called in the super constructor
    private static String getLabel(Visibility visibility) {
        return switch (visibility) {
            case INPUT -> ResourceHandler.getString("Inspector", "input");
            case OUTPUT -> ResourceHandler.getString("Inspector", "output");
            default -> "";
        };
    }
}
