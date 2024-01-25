package org.gecko.tools;

import javafx.geometry.Bounds;
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
import org.gecko.viewmodel.SelectionManager;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";
    private static final String ICON_STYLE_NAME = "cursor-icon";

    private boolean isDragging = false;

    private final SelectionManager selectionManager;
    private Point2D startDragPosition;
    private ViewElement<?> draggedElement;

    private ScrollPane viewPane;

    public CursorTool(ActionManager actionManager, SelectionManager selectionManager) {
        super(actionManager);
        this.selectionManager = selectionManager;
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
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        setDragAndSelectHandlers(stateViewElement);
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        setDragAndSelectHandlers(systemViewElement);
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        super.visit(edgeViewElement);
        edgeViewElement.setOnMouseClicked(event -> selectElement(edgeViewElement, !event.isShiftDown()));
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        super.visit(variableBlockViewElement);
        setDragAndSelectHandlers(variableBlockViewElement);
    }

    @Override
    public void visit(RegionViewElement regionViewElement) {
        super.visit(regionViewElement);
        setDragAndSelectHandlers(regionViewElement);
    }

    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        super.visit(systemConnectionViewElement);
        systemConnectionViewElement.setOnMouseClicked(
            event -> selectElement(systemConnectionViewElement, !event.isShiftDown()));
    }

    private void setDragAndSelectHandlers(ViewElement<?> element) {
        element.drawElement().setOnMousePressed(event -> {
            selectElement(element, !event.isShiftDown());
            startDraggingElementHandler(event, element);
        });
        element.drawElement().setOnMouseDragged(this::dragElementsHandler);
        element.drawElement().setOnMouseReleased(this::stopDraggingElementHandler);
    }

    private void startDraggingElementHandler(MouseEvent event, ViewElement<?> element) {
        Point2D eventPosition = new Point2D(event.getX(), event.getY());
        draggedElement = element;
        isDragging = true;
        startDragPosition = eventPosition;
    }

    private void dragElementsHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Point2D eventPosition = new Point2D(event.getX(), event.getY());
        Point2D worldspaceDelta = eventPosition.subtract(startDragPosition); //TODO
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setPosition(element.getPosition().add(worldspaceDelta));
        });
    }

    private void stopDraggingElementHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Bounds boundsInScreen =
            draggedElement.drawElement().localToScreen(draggedElement.drawElement().getBoundsInLocal());
        Bounds boundsInPane = viewPane.screenToLocal(boundsInScreen);
        isDragging = false;
        Point2D worldPos = new Point2D(boundsInPane.getMinX(), boundsInPane.getMinY());
        Action moveAction =
            actionManager.getActionFactory().createMoveBlockViewModelElementAction(startDragPosition, worldPos);
        actionManager.run(moveAction);
        startDragPosition = null;
        draggedElement = null;
    }

    private void selectElement(ViewElement<?> viewElement, boolean newSelection) {
        Action select = actionManager.getActionFactory().createSelectAction(viewElement.getTarget(), newSelection);
        actionManager.run(select);
    }
}
