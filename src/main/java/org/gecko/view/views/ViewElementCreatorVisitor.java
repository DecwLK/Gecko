package org.gecko.view.views;

import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Follows the visitor pattern, implementing the {@link PositionableViewModelElementVisitor} interface. Creates each
 * type of {@link ViewElement} through the {@link ViewFactory} by visiting a type of
 * {@link org.gecko.viewmodel.PositionableViewModelElement PositionableViewModelElement}.
 */
public class ViewElementCreatorVisitor implements PositionableViewModelElementVisitor<ViewElement<?>> {

    private final ViewFactory viewFactory;

    public ViewElementCreatorVisitor(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    @Override
    public ViewElement<?> visit(SystemViewModel systemViewModel) {
        return viewFactory.createViewElementFrom(systemViewModel);
    }

    @Override
    public ViewElement<?> visit(RegionViewModel regionViewModel) {
        return viewFactory.createViewElementFrom(regionViewModel);
    }

    @Override
    public ViewElement<?> visit(SystemConnectionViewModel systemConnectionViewModel) {
        return viewFactory.createViewElementFrom(systemConnectionViewModel);
    }

    @Override
    public ViewElement<?> visit(EdgeViewModel edgeViewModel) {
        return viewFactory.createViewElementFrom(edgeViewModel);
    }

    @Override
    public ViewElement<?> visit(StateViewModel stateViewModel) {
        return viewFactory.createViewElementFrom(stateViewModel);
    }

    @Override
    public ViewElement<?> visit(PortViewModel portViewModel) {
        return viewFactory.createViewElementFrom(portViewModel);
    }
}
