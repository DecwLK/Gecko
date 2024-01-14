package org.gecko.actions;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.ViewModelFactory;

public class CreateContractViewModelElementAction extends Action {

    private final ViewModelFactory viewModelFactory;
    private final StateViewModel stateViewModel;
    private ContractViewModel createdContractViewModel;

    CreateContractViewModelElementAction(ViewModelFactory viewModelFactory, StateViewModel stateViewModel) {
        this.viewModelFactory = viewModelFactory;
        this.stateViewModel = stateViewModel;
    }

    @Override
    void run() {
        createdContractViewModel = viewModelFactory.createContractViewModelIn(stateViewModel);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeleteContractViewModelAction(stateViewModel, createdContractViewModel);
    }
}
