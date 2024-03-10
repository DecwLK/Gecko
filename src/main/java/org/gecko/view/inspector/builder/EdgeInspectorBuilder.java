package org.gecko.view.inspector.builder;

import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.combobox.InspectorContractComboBox;
import org.gecko.view.inspector.element.container.InspectorEdgeStateLabel;
import org.gecko.view.inspector.element.container.InspectorKindPicker;
import org.gecko.view.inspector.element.container.InspectorPriorityLabel;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.EdgeViewModel;

/**
 * Represents a type of {@link AbstractInspectorBuilder} of an {@link org.gecko.view.inspector.Inspector Inspector} for
 * an {@link EdgeViewModel}. Adds to the list of
 * {@link org.gecko.view.inspector.element.InspectorElement InspectorElement}s, which are added to a built
 * {@link org.gecko.view.inspector.Inspector Inspector}, the following: an {@link InspectorKindPicker}, two
 * {@link InspectorEdgeStateLabel}s for the source- and target-states, an {@link InspectorPriorityLabel} and an
 * {@link InspectorContractComboBox}.
 */
public class EdgeInspectorBuilder extends AbstractInspectorBuilder<EdgeViewModel> {
    private static final String SOURCE_KEY = "source";
    private static final String TARGET_KEY = "source";
    private static final String CONTRACTS_KEY = "contract_plural";

    public EdgeInspectorBuilder(ActionManager actionManager, EdgeViewModel viewModel) {
        super(actionManager, viewModel);

        // Kind
        addInspectorElement(new InspectorKindPicker(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Connected states
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getSource(),
            ResourceHandler.getString(INSPECTOR, SOURCE_KEY)));
        addInspectorElement(new InspectorEdgeStateLabel(actionManager, viewModel.getDestination(),
            ResourceHandler.getString(INSPECTOR, TARGET_KEY)));
        addInspectorElement(new InspectorSeparator());

        // Priority
        addInspectorElement(new InspectorPriorityLabel(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel(ResourceHandler.getString(INSPECTOR, CONTRACTS_KEY)));
        addInspectorElement(new InspectorContractComboBox(actionManager, viewModel));
    }
}
