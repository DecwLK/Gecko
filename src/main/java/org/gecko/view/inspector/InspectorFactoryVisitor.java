package org.gecko.view.inspector;

import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorFactoryVisitor implements PositionableViewModelElementVisitor {
    @Override
    public Object visit(SystemViewModel systemViewModel) {
        return null;
    }

    @Override
    public Object visit(RegionViewModel regionViewModel) {
        return null;
    }

    @Override
    public Object visit(SystemConnectionViewModel systemConnectionViewModel) {
        return null;
    }

    @Override
    public Object visit(EdgeViewModel edgeViewModel) {
        return null;
    }

    @Override
    public Object visit(StateViewModel stateViewModel) {
        return null;
    }

    @Override
    public Object visit(PortViewModel portViewModel) {
        return null;
    }
}
