package org.gecko.tools;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
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
        view.setCursor(Cursor.DEFAULT);
        setAllHandlers(view, null);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        //We need to consume all events so that they don't propagate to the view
        setAllHandlers(stateViewElement, Event::consume);
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        setAllHandlers(edgeViewElement, Event::consume);
    }

    @Override
    public void visit(RegionViewElement regionViewElement) {
        //Events are null here because the events need to propagate to the view.
        // This allows elements to be placed in the region
        setAllHandlers(regionViewElement, null);
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        setAllHandlers(systemViewElement, Event::consume);
    }

    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        setAllHandlers(systemConnectionViewElement, Event::consume);
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        setAllHandlers(variableBlockViewElement, Event::consume);
    }

    @Override
    public void visit(ElementScalerViewElementDecorator elementScalarViewElementDecorator) {
        setAllHandlers(elementScalarViewElementDecorator.drawElement(), null);
    }

    @Override
    public void visit(ConnectionElementScalerViewElementDecorator connectionElementScalerViewElementDecorator) {
        setAllHandlers(connectionElementScalerViewElementDecorator.drawElement(), null);
    }

    @Override
    public void visit(SelectableViewElementDecorator selectableViewElementDecorator) {
        setAllHandlers(selectableViewElementDecorator.drawElement(), null);
    }

    private void setAllHandlers(Node node, EventHandler<MouseEvent> handler) {
        node.setOnMousePressed(handler);
        node.setOnMouseDragged(handler);
        node.setOnMouseReleased(handler);
        node.setOnMouseClicked(handler);
        node.setOnMouseMoved(handler);
    }
}
