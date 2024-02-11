package org.gecko.actions;

import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.StateViewModel;

public class DeleteContractViewModelAction extends Action {
    private final StateViewModel parent;
    private final ContractViewModel contractViewModel;
    private Set<EdgeViewModel> edgesWithContract;

    DeleteContractViewModelAction(StateViewModel parent, ContractViewModel contractViewModel) {
        this.parent = parent;
        this.contractViewModel = contractViewModel;
    }

    @Override
    boolean run() throws GeckoException {
        edgesWithContract = parent.getOutgoingEdges()
            .stream()
            .filter(edge -> edge.getContract().equals(contractViewModel))
            .collect(Collectors.toSet());
        for (EdgeViewModel edge : edgesWithContract) {
            edge.setContract(null);
            edge.updateTarget();
        }
        parent.removeContract(contractViewModel);
        parent.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRestoreContractViewModelElementAction(parent, contractViewModel, edgesWithContract);
    }
}
