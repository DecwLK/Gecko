package org.gecko.tools;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.view.views.viewelement.decorator.ElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.SelectableViewElementDecorator;

public abstract class Tool implements ViewElementVisitor {

    protected final ActionManager actionManager;

    public Tool(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public abstract String getName();

    public abstract String getIconStyleName();

    public void visitView(ScrollPane view) {
        view.setPannable(false);
        removeAllListeners(view);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        removeAllListeners(stateViewElement);
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        removeAllListeners(edgeViewElement);
    }

    @Override
    public void visit(RegionViewElement regionViewElement) {
        removeAllListeners(regionViewElement);
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        removeAllListeners(systemViewElement);
    }

    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        removeAllListeners(systemConnectionViewElement);
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        removeAllListeners(variableBlockViewElement);
    }

    @Override
    public void visit(ElementScalerViewElementDecorator elementScalerViewElementDecorator) {
        removeAllListeners(elementScalerViewElementDecorator.drawElement());
    }

    @Override
    public void visit(SelectableViewElementDecorator selectableViewElementDecorator) {
        removeAllListeners(selectableViewElementDecorator.drawElement());
    }

    private void removeAllListeners(Node view) {
        view.setOnMousePressed(null);
        view.setOnMouseDragged(null);
        view.setOnMouseReleased(null);
        view.setOnMouseClicked(null);
        view.setOnMouseMoved(null);
    }
}
