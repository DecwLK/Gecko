package org.gecko.view.inspector.element.container;

import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.button.InspectorAddVariableButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a type of {@link LabeledInspectorElement}. Contains an {@link InspectorLabel} and an
 * {@link InspectorAddVariableButton}.
 */
public class InspectorVariableLabel extends LabeledInspectorElement {
    private static final String INPUT_KEY = "input";
    private static final String OUTPUT_KEY = "output";

    public InspectorVariableLabel(ActionManager actionManager, SystemViewModel viewModel, Visibility visibility) {
        super(new InspectorLabel(getLabel(visibility)),
            new InspectorAddVariableButton(actionManager, viewModel, visibility));
    }

    //Cant be public because it is called in the super constructor
    private static String getLabel(Visibility visibility) {
        return switch (visibility) {
            case INPUT -> ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, INPUT_KEY);
            case OUTPUT -> ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, OUTPUT_KEY);
            default -> "";
        };
    }
}
