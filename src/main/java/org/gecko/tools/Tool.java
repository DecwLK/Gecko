package org.gecko.tools;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.PortViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.view.views.viewelement.decorator.BlockElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.SelectableViewElementDecorator;

/**
 * An abstract representation of a tool used in the Gecko Graphic Editor, characterized by a {@link ToolType}. Follows
 * the visitor pattern, implementing the {@link ViewElementVisitor} interface. Defines the {@link MouseEvent}
 * {@link EventHandler}s for each {@link org.gecko.view.views.viewelement.ViewElement ViewElement}.
 */
public abstract class Tool implements ViewElementVisitor {

    protected final ActionManager actionManager;
    @Getter
    private final ToolType toolType;

    public Tool(ActionManager actionManager, ToolType toolType) {
        this.actionManager = actionManager;
        this.toolType = toolType;
    }

    /**
     * Applies listeners to the given view, which is assumed to be the current view of the application.
     *
     * @param pane the view to visit
     */
    public void visitView(ViewElementPane pane) {
        pane.draw().setCursor(Cursor.DEFAULT);
        setAllHandlers(pane.draw(), null);
        setAllHandlers(pane.getWorld(), null);
    }

    /**
     * Overriden by specific tools to handle interaction with a state view element. By default, consumes all events.
     *
     * @param stateViewElement the state view element to visit
     */
    @Override
    public void visit(StateViewElement stateViewElement) {
        //We need to consume all events so that they don't propagate to the view
        setAllHandlers(stateViewElement, Event::consume);
    }

    /**
     * Overriden by specific tools to handle interaction with an edge view element. By default, consumes all events.
     *
     * @param edgeViewElement the edge view element to visit
     */
    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        setAllHandlers(edgeViewElement, Event::consume);
    }

    /**
     * Overriden by specific tools to handle interaction with a region view element. By default, lets events pass
     * through.
     *
     * @param regionViewElement the region view element to visit
     */
    @Override
    public void visit(RegionViewElement regionViewElement) {
        setAllHandlers(regionViewElement, null);
    }

    /**
     * Overriden by specific tools to handle interaction with a system view element. By default, consumes all events.
     *
     * @param systemViewElement the system view element to visit
     */
    @Override
    public void visit(SystemViewElement systemViewElement) {
        setAllHandlers(systemViewElement, Event::consume);
    }

    /**
     * Overriden by specific tools to handle interaction with a system connection view element. By default, consumes all
     * events.
     *
     * @param systemConnectionViewElement the system connection view element to visit
     */
    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        setAllHandlers(systemConnectionViewElement, Event::consume);
    }

    /**
     * Overriden by specific tools to handle interaction with a variable block view element. By default, consumes all
     * events.
     *
     * @param variableBlockViewElement the variable block view element to visit
     */
    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        setAllHandlers(variableBlockViewElement, Event::consume);
    }

    /**
     * Overriden by specific tools to handle interaction with a block element scaler view element decorator. By default,
     * lets events pass through.
     *
     * @param elementScalarViewElementDecorator the block element scaler view element decorator to visit
     */
    @Override
    public void visit(ElementScalerViewElementDecorator elementScalarViewElementDecorator) {
        setAllHandlers(elementScalarViewElementDecorator.drawElement(), null);
    }

    /**
     * Overriden by specific tools to handle interaction with a connection element scaler view element decorator. By
     * default, lets events pass through.
     *
     * @param connectionElementScalerViewElementDecorator the connection element scaler view element decorator to visit
     */
    @Override
    public void visit(ConnectionElementScalerViewElementDecorator connectionElementScalerViewElementDecorator) {
        setAllHandlers(connectionElementScalerViewElementDecorator.drawElement(), null);
    }

    /**
     * Overriden by specific tools to handle interaction with a block element scaler view element decorator. By default,
     * lets events pass through.
     *
     * @param blockElementScalerViewElementDecorator the block element scaler view element decorator to visit
     */
    @Override
    public void visit(BlockElementScalerViewElementDecorator blockElementScalerViewElementDecorator) {
        setAllHandlers(blockElementScalerViewElementDecorator.drawElement(), null);
    }

    /**
     * Overriden by specific tools to handle interaction with a selectable view element decorator. By default, lets
     * events pass through.
     *
     * @param selectableViewElementDecorator the selectable view element decorator to visit
     */
    @Override
    public void visit(SelectableViewElementDecorator selectableViewElementDecorator) {
        setAllHandlers(selectableViewElementDecorator.drawElement(), null);
    }

    /**
     * Overriden by specific tools to handle interaction with a port view element. By default, consumes all events.
     *
     * @param portViewElement the port view element to visit
     */
    @Override
    public void visit(PortViewElement portViewElement) {
        setAllHandlers(portViewElement, Event::consume);
    }

    private void setAllHandlers(Node node, EventHandler<MouseEvent> handler) {
        node.setOnMousePressed(handler);
        node.setOnMouseDragged(handler);
        node.setOnMouseReleased(handler);
        node.setOnMouseClicked(handler);
        node.setOnMouseMoved(handler);
    }
}
