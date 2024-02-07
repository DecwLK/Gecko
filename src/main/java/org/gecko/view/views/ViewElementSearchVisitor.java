package org.gecko.view.views;

import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewElementSearchVisitor implements PositionableViewModelElementVisitor {
    private final String search;

    public ViewElementSearchVisitor(String search) {
        this.search = search;
    }

    @Override
    public SystemViewModel visit(SystemViewModel systemViewModel) {
        if (systemViewModel.getName().toLowerCase().contains(search.toLowerCase())) {
            return systemViewModel;
        }
        return null;
    }

    @Override
    public RegionViewModel visit(RegionViewModel regionViewModel) {
        if (regionViewModel.getName().toLowerCase().contains(search.toLowerCase())) {
            return regionViewModel;
        }
        return null;
    }

    @Override
    public SystemConnectionViewModel visit(SystemConnectionViewModel systemConnectionViewModel) {
        return null;
    }

    @Override
    public EdgeViewModel visit(EdgeViewModel edgeViewModel) {
        if (visit(edgeViewModel.getContract())) {
            return edgeViewModel;
        }
        return null;
    }

    @Override
    public StateViewModel visit(StateViewModel stateViewModel) {
        if (stateViewModel.getName().toLowerCase().contains(search.toLowerCase())) {
            return stateViewModel;
        }
        for (ContractViewModel contractViewModel : stateViewModel.getContracts()) {
            if (visit(contractViewModel)) {
                return stateViewModel;
            }
        }
        return null;
    }

    @Override
    public PortViewModel visit(PortViewModel portViewModel) {
        if (portViewModel.getName().toLowerCase().contains(search.toLowerCase())) {
            return portViewModel;
        }
        return null;
    }

    private boolean visit(ContractViewModel contractViewModel) {
        return contractViewModel.getName().toLowerCase().contains(search.toLowerCase());
    }
}
