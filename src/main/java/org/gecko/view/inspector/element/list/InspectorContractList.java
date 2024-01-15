package org.gecko.view.inspector.element.list;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractList extends AbstractInspectorList<InspectorContractItem> {
    public InspectorContractList(ActionManager actionManager, StateViewModel stateViewModel) {
        ObservableList<InspectorContractItem> items = getItems();
        ObservableList<ContractViewModel> contractViewModels = stateViewModel.getContractsProperty();

        // Create a listener for contractViewModels changes and update inspector items accordingly
        ListChangeListener<ContractViewModel> contractViewModelListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (ContractViewModel item : change.getAddedSubList()) {
                        items.add(new InspectorContractItem(actionManager, stateViewModel, item));
                    }
                } else if (change.wasRemoved()) {
                    for (ContractViewModel item : change.getRemoved()) {
                        contractViewModels.remove(item);
                    }
                }
            }
        };

        contractViewModels.addListener(contractViewModelListener);
    }
}
