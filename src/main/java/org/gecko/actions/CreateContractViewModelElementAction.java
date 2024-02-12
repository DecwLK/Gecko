package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.ViewModelFactory;

/**
 * A concrete representation of an {@link Action} that creates a {@link ContractViewModel} in a given
 * {@link StateViewModel} through the {@link ViewModelFactory} of the
 * {@link org.gecko.viewmodel.GeckoViewModel GeckoViewModel}.
 */
public class CreateContractViewModelElementAction extends Action {

    private final ViewModelFactory viewModelFactory;
    private final StateViewModel stateViewModel;
    private ContractViewModel createdContractViewModel;

    CreateContractViewModelElementAction(ViewModelFactory viewModelFactory, StateViewModel stateViewModel) {
        this.viewModelFactory = viewModelFactory;
        this.stateViewModel = stateViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        createdContractViewModel = viewModelFactory.createContractViewModelIn(stateViewModel);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeleteContractViewModelAction(stateViewModel, createdContractViewModel);
    }
}
