package org.gecko.view.inspector.element.combobox;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorContractComboBox extends AbstractInspectorComboBox<InspectorElement<?>> {
    public InspectorContractComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        viewModel.getSource().getContractsProperty().forEach(contract -> {
            getItems().add(new InspectorContractItem(actionManager, viewModel.getSource(), contract));
        });
    }
}
