package org.gecko.actions;

import java.util.Set;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

public class RestoreContractViewModelElementAction extends Action {

    private final StateViewModel parent;
    private final ContractViewModel contractViewModel;
    private final Set<EdgeViewModel> edgesWithContract;

    RestoreContractViewModelElementAction(
        StateViewModel parent, ContractViewModel contractViewModel, Set<EdgeViewModel> edgesWithContract) {
        this.parent = parent;
        this.contractViewModel = contractViewModel;
        this.edgesWithContract = edgesWithContract;
    }

    @Override
    boolean run() throws GeckoException {
        for (EdgeViewModel edge : edgesWithContract) {
            edge.setContract(contractViewModel);
            edge.updateTarget();
        }
        parent.addContract(contractViewModel);
        parent.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createDeleteContractViewModelAction(parent, contractViewModel);
    }
}
