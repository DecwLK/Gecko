package org.gecko.tools;

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
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SelectionManager;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";
    private static final String ICON_STYLE_NAME = "cursor-icon";

    private boolean isDragging = false;

    private final SelectionManager selectionManager;
    private final EditorViewModel editorViewModel;
    private Point2D startDragPosition;
    private Point2D previousDragPosition;
    private ViewElement<?> draggedElement;

    private ScrollPane viewPane;

    public CursorTool(ActionManager actionManager, SelectionManager selectionManager, EditorViewModel editorViewModel) {
        super(actionManager);
        this.selectionManager = selectionManager;
        this.editorViewModel = editorViewModel;
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
            startDraggingElementHandler(event, element);
            selectElement(element, !event.isShiftDown());
        });
        element.drawElement().setOnMouseDragged(this::dragElementsHandler);
        element.drawElement().setOnMouseReleased(this::stopDraggingElementHandler);
    }

    private void startDraggingElementHandler(MouseEvent event, ViewElement<?> element) {
        draggedElement = element;
        isDragging = true;
        startDragPosition = getCoordinatesInPane(element).add(new Point2D(event.getX(), event.getY()));
        previousDragPosition = startDragPosition;
    }

    private void dragElementsHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Point2D eventPosition = getCoordinatesInPane(draggedElement).add(new Point2D(event.getX(), event.getY()));
        Point2D delta = eventPosition.subtract(previousDragPosition);
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setPosition(element.getPosition().add(delta));
        });
        previousDragPosition = eventPosition;
    }

    private void stopDraggingElementHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        isDragging = false;
        Point2D endWorldPos = getCoordinatesInPane(draggedElement).add(new Point2D(event.getX(), event.getY()));
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setPosition(element.getPosition().add(startDragPosition.subtract(endWorldPos)));
        });
        Action moveAction = actionManager.getActionFactory()
            .createMoveBlockViewModelElementAction(endWorldPos.subtract(startDragPosition));
        actionManager.run(moveAction);
        startDragPosition = null;
        draggedElement = null;
    }

    private void selectElement(ViewElement<?> viewElement, boolean newSelection) {
        Action select = actionManager.getActionFactory().createSelectAction(viewElement.getTarget(), newSelection);
        actionManager.run(select);
    }

    private Point2D getCoordinatesInPane(ViewElement<?> viewElement) {
        return editorViewModel.transformScreenToWorldCoordinates(
            new Point2D(viewElement.drawElement().getBoundsInParent().getMinX(),
                viewElement.drawElement().getBoundsInParent().getMinY()).multiply(editorViewModel.getZoomScale()));
    }
}
