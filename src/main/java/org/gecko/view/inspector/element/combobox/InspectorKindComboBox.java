package org.gecko.view.inspector.element.combobox;

import java.util.Arrays;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.viewmodel.EdgeViewModel;

/** Represents a type of {@link InspectorComboBox} encapsulating a {@link Kind}. Holds a reference to an {@link EdgeViewModel} and the current {@link ActionManager}. */
public class InspectorKindComboBox extends InspectorComboBox<Kind> {
    private final EdgeViewModel viewModel;
    private final ActionManager actionManager;

    public InspectorKindComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        super(actionManager, Arrays.stream(Kind.values()).toList(), viewModel.getKindProperty());
        this.viewModel = viewModel;
        this.actionManager = actionManager;
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeKindAction(viewModel, getValue());
    }
}
