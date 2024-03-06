package org.gecko.view.inspector.element.combobox;

import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.model.Variable;
import org.gecko.viewmodel.PortViewModel;

/**
 * A concrete representation of an {@link InspectorComboBox} for a {@link PortViewModel}, through which the type of the
 * port can be changed.
 */
public class InspectorTypeComboBox extends InspectorComboBox<String> {
    private final PortViewModel viewModel;
    private final ActionManager actionManager;

    public InspectorTypeComboBox(ActionManager actionManager, PortViewModel viewModel) {
        super(actionManager, Variable.BUILTIN_TYPES, viewModel.getTypeProperty());
        this.viewModel = viewModel;
        this.actionManager = actionManager;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeTypePortViewModelElementAction(viewModel, getValue());
    }
}
