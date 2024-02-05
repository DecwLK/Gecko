package org.gecko.view.views.viewelement;

import org.gecko.view.views.viewelement.decorator.BlockElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.SelectableViewElementDecorator;

public interface ViewElementVisitor {
    void visit(StateViewElement stateViewElement);

    void visit(EdgeViewElement edgeViewElement);

    void visit(RegionViewElement regionViewElement);

    void visit(SystemViewElement systemViewElement);

    void visit(PortViewElement portViewElement);

    void visit(SystemConnectionViewElement systemConnectionViewElement);

    void visit(VariableBlockViewElement variableBlockViewElement);

    void visit(ElementScalerViewElementDecorator elementScalarViewElementDecorator);

    void visit(SelectableViewElementDecorator selectableViewElementDecorator);

    void visit(ConnectionElementScalerViewElementDecorator connectionElementScalerViewElementDecorator);

    void visit(BlockElementScalerViewElementDecorator blockElementScalerViewElementDecorator);
}
