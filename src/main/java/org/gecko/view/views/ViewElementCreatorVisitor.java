package org.gecko.view.views;

import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewElementCreatorVisitor implements PositionableViewModelElementVisitor {

    private final ViewFactory viewFactory;

    public ViewElementCreatorVisitor(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    @Override
    public ViewElement<SystemViewModel> visit(SystemViewModel systemViewModel) {
        return viewFactory.createSystemViewElementFrom(systemViewModel);
    }

    @Override
    public ViewElement<RegionViewModel> visit(RegionViewModel regionViewModel) {
        return viewFactory.createRegionViewElementFrom(regionViewModel);
    }

    @Override
    public ViewElement<SystemConnectionViewModel> visit(SystemConnectionViewModel systemConnectionViewModel) {
        return viewFactory.createSystemConnectionViewElementFrom(systemConnectionViewModel);
    }

    @Override
    public ViewElement<EdgeViewModel> visit(EdgeViewModel edgeViewModel) {
        return viewFactory.createEdgeViewElementFrom(edgeViewModel);
    }

    @Override
    public ViewElement<StateViewModel> visit(StateViewModel stateViewModel) {
        return viewFactory.createStateViewElementFrom(stateViewModel);
    }

    @Override
    public ViewElement<PortViewModel> visit(PortViewModel portViewModel) {
        return viewFactory.createVariableBlockViewElementFrom(portViewModel);
    }
}
