package org.gecko.view.inspector.element.combobox;

import java.util.Arrays;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

public class InspectorVisibilityComboBox extends InspectorComboBox<Visibility> {
    private final PortViewModel viewModel;
    private final ActionManager actionManager;

    public InspectorVisibilityComboBox(ActionManager actionManager, PortViewModel viewModel) {
        super(actionManager, Arrays.stream(Visibility.values()).toList(), viewModel.getVisibilityProperty());
        this.viewModel = viewModel;
        this.actionManager = actionManager;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeVisibilityPortViewModelAction(viewModel, getValue());
    }
}
