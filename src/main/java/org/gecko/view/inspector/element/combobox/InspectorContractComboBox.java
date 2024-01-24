package org.gecko.view.inspector.element.combobox;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorContractComboBox extends AbstractInspectorComboBox<InspectorElement<?>> {

    private static final int PREF_WIDTH = 300;

    public InspectorContractComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        setPrefWidth(PREF_WIDTH);
        viewModel.getSource().getContractsProperty().forEach(contract -> {
            getItems().add(new InspectorContractItem(actionManager, viewModel.getSource(), contract));
        });
    }
}
