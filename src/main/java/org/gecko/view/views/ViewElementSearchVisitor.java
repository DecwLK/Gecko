package org.gecko.view.views;

import java.util.Locale;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/** Follows the visitor pattern, implementing the {@link PositionableViewModelElementVisitor} interface. Searches for absolute or partial matches between a given {@link String} and the names of {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElement}s, if present. */
public class ViewElementSearchVisitor implements PositionableViewModelElementVisitor {
    private final String search;

    public ViewElementSearchVisitor(String search) {
        this.search = search;
    }

    @Override
    public SystemViewModel visit(SystemViewModel systemViewModel) {
        if (systemViewModel.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
            return systemViewModel;
        }
        return null;
    }

    @Override
    public RegionViewModel visit(RegionViewModel regionViewModel) {
        if (regionViewModel.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
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
        return null;
    }

    @Override
    public StateViewModel visit(StateViewModel stateViewModel) {
        if (stateViewModel.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
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
        if (portViewModel.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
            return portViewModel;
        }
        return null;
    }

    private boolean visit(ContractViewModel contractViewModel) {
        return contractViewModel.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT));
    }
}
