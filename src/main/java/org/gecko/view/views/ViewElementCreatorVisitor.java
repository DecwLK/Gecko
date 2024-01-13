package org.gecko.view.views;

import org.gecko.viewmodel.*;

public class ViewElementCreatorVisitor implements PositionableViewModelElementVisitor {

    @Override
    public Void visit(SystemViewModel systemViewModel) {
        return null;
    }

    @Override
    public Void visit(RegionViewModel regionViewModel) {
        return null;
    }

    @Override
    public Void visit(SystemConnectionViewModel systemConnectionViewModel) {
        return null;
    }

    @Override
    public Void visit(EdgeViewModel edgeViewModel) {
        return null;
    }

    @Override
    public Void visit(StateViewModel stateViewModel) {
        return null;
    }


    @Override
    public Void visit(PortViewModel portViewModel) {
        return null;
    }
}
