package org.gecko.tools;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";
    private static final String ICON_STYLE_NAME = "cursor-icon";

    private ScrollPane viewPane;
    private boolean isDragging = false;

    public CursorTool(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconStyleName() {
        return ICON_STYLE_NAME;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.DEFAULT);
        view.setPannable(false);
        viewPane = view;
        view.setOnMouseReleased(moveElement());
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        stateViewElement.setOnMousePressed(event -> {
            isDragging = true;
        });
        stateViewElement.setOnMouseClicked(selectElement(stateViewElement));
        stateViewElement.setOnMouseReleased(event -> {
            // Forward event to the main view, so that the mouse position is relative to the view and not the element
            viewPane.fireEvent(event);
        });
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        systemViewElement.setOnMousePressed(event -> {
            isDragging = true;
        });
        systemViewElement.setOnMouseClicked(selectElement(systemViewElement));
        systemViewElement.setOnMouseReleased(event -> {
            // Forward event to the main view, so that the mouse position is relative to the view and not the element
            viewPane.fireEvent(event);
        });
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        super.visit(edgeViewElement);
        edgeViewElement.setOnMouseClicked(selectElement(edgeViewElement));
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        super.visit(variableBlockViewElement);
        variableBlockViewElement.setOnMousePressed(event -> {
            isDragging = true;
        });
        variableBlockViewElement.setOnMouseClicked(selectElement(variableBlockViewElement));
        variableBlockViewElement.setOnMouseReleased(event -> {
            // Forward event to the main view, so that the mouse position is relative to the view and not the element
            viewPane.fireEvent(event);
        });
    }

    @Override
    public void visit(RegionViewElement regionViewElement) {
        super.visit(regionViewElement);
        regionViewElement.setOnMouseClicked(selectElement(regionViewElement));
    }

    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        super.visit(systemConnectionViewElement);
        systemConnectionViewElement.setOnMouseClicked(selectElement(systemConnectionViewElement));
    }

    private EventHandler<MouseEvent> selectElement(ViewElement<?> viewElement) {
        return event -> {
            Action selectAction =
                actionManager.getActionFactory().createSelectAction(viewElement.getTarget(), !event.isShiftDown());
            actionManager.run(selectAction);
        };
    }

    private EventHandler<MouseEvent> moveElement() {
        return event -> {
            if (!isDragging) {
                return;
            }
            Point2D position = new Point2D(event.getX(), event.getY());
            Action selectAction = actionManager.getActionFactory().createMoveBlockViewModelElementAction(position);
            actionManager.run(selectAction);
            isDragging = false;
        };
    }
}
