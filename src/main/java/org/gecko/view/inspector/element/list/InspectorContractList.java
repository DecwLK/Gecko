package org.gecko.view.inspector.element.list;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.container.InspectorContractItem;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractList extends AbstractInspectorList<InspectorContractItem> {
    public InspectorContractList(ActionManager actionManager, StateViewModel stateViewModel) {
        ObservableList<InspectorContractItem> items = getItems();
        ObservableList<ContractViewModel> contractViewModels = stateViewModel.getContractsProperty();

        // Initialize inspector items
        for (ContractViewModel contractViewModel : contractViewModels) {
            items.add(new InspectorContractItem(actionManager, stateViewModel, contractViewModel));
        }

        // Create a listener for contractViewModels changes and update inspector items accordingly
        ListChangeListener<ContractViewModel> contractViewModelListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (ContractViewModel item : change.getAddedSubList()) {
                        InspectorContractItem newContractItem = new InspectorContractItem(actionManager, stateViewModel, item);
                        newContractItem.prefWidthProperty().bind(widthProperty());
                        items.add(newContractItem);
                    }
                } else if (change.wasRemoved()) {
                    for (ContractViewModel item : change.getRemoved()) {
                        items.removeIf(inspectorContractItem -> inspectorContractItem.getViewModel() == item);
                    }
                }
            }
        };

        contractViewModels.addListener(contractViewModelListener);
    }
}
