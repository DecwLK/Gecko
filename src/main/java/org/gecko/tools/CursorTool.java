package org.gecko.tools;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SelectionManager;

public class CursorTool extends Tool {
    private boolean isDragging = false;
    private final SelectionManager selectionManager;
    private final EditorViewModel editorViewModel;
    private Point2D startDragPosition;
    private Point2D previousDragPosition;
    private Node draggedElement;

    private ScrollPane viewPane;

    public CursorTool(ActionManager actionManager, SelectionManager selectionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.CURSOR_TOOL);
        this.selectionManager = selectionManager;
        this.editorViewModel = editorViewModel;
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

    @Override
    public void visit(ConnectionElementScalerViewElementDecorator connectionElementScalerViewElementDecorator) {
        super.visit(connectionElementScalerViewElementDecorator);

        connectionElementScalerViewElementDecorator.drawElement().setOnMouseClicked(event -> {
            if (!connectionElementScalerViewElementDecorator.isSelected()) {
                return;
            }
            if (isDragging) {
                isDragging = false;
                return;
            }

            // Create new scaler block
            Point2D newScalerBlockPosition =
                getWorldCoordinates(connectionElementScalerViewElementDecorator.drawElement(),
                    new Point2D(event.getX(), event.getY())).multiply(1 / editorViewModel.getZoomScale());
            Action createScalerBlockAction = actionManager.getActionFactory()
                .createCreateEdgeScalerBlockViewElementAction(connectionElementScalerViewElementDecorator,
                    newScalerBlockPosition);
            actionManager.run(createScalerBlockAction);

            // Add handlers to the new scaler block
            connectionElementScalerViewElementDecorator.accept(this);
        });

        for (ElementScalerBlock scaler : connectionElementScalerViewElementDecorator.getScalers()) {
            setScalerBlockHandlers(scaler);
        }
    }

    private void setScalerBlockHandlers(ElementScalerBlock scaler) {
        scaler.setOnMousePressed(event -> {
            startDraggingElementHandler(event, scaler);
            scaler.setDragging(true);
        });
        scaler.setOnMouseDragged(event -> {
            if (!isDragging) {
                return;
            }
            Point2D eventPosition = getWorldCoordinates(draggedElement).add(new Point2D(event.getX(), event.getY()));
            Point2D delta = eventPosition.subtract(previousDragPosition);
            scaler.setPoint(scaler.getPoint().add(delta));
            previousDragPosition = eventPosition;
        });
        scaler.setOnMouseReleased(event -> {
            if (!isDragging) {
                return;
            }
            Point2D endWorldPos = getWorldCoordinates(draggedElement).add(new Point2D(event.getX(), event.getY()));
            scaler.setPoint(scaler.getPoint().add(startDragPosition.subtract(endWorldPos)));
            Action moveAction = actionManager.getActionFactory()
                .createMoveEdgeScalerBlockViewElementAction((EdgeViewModel) scaler.getDecoratorTarget().getTarget(),
                    scaler, endWorldPos.subtract(startDragPosition));
            actionManager.run(moveAction);
            startDragPosition = null;
            draggedElement = null;
            scaler.setDragging(false);
        });
    }

    private void setDragAndSelectHandlers(ViewElement<?> element) {
        element.drawElement().setOnMousePressed(event -> {
            startDraggingElementHandler(event, element.drawElement());
            selectElement(element, !event.isShiftDown());
        });
        element.drawElement().setOnMouseDragged(this::dragElementsHandler);
        element.drawElement().setOnMouseReleased(this::stopDraggingElementHandler);
    }

    private void startDraggingElementHandler(MouseEvent event, Node element) {
        draggedElement = element;
        isDragging = true;
        startDragPosition = getWorldCoordinates(element).add(new Point2D(event.getX(), event.getY()));
        previousDragPosition = startDragPosition;
    }

    private void dragElementsHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Point2D eventPosition = getWorldCoordinates(draggedElement).add(new Point2D(event.getX(), event.getY()));
        Point2D delta = eventPosition.subtract(previousDragPosition);
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setCurrentlyModified(true);
            element.setPosition(element.getPosition().add(delta));
        });
        previousDragPosition = eventPosition;
    }

    private void stopDraggingElementHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        isDragging = false;
        Point2D endWorldPos = getWorldCoordinates(draggedElement).add(new Point2D(event.getX(), event.getY()));
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setCurrentlyModified(false);
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

    private Point2D getWorldCoordinates(Node node) {
        Bounds nodeBounds = node.getBoundsInLocal();
        return getWorldCoordinates(node, new Point2D(nodeBounds.getMinX(), nodeBounds.getMinY()));
    }

    private Point2D getWorldCoordinates(Node node, Point2D point) {
        double parentX = 0;
        double parentY = 0;
        Node current = node;

        while (current != null && current != viewPane) {
            parentX += current.getLocalToParentTransform().getTx();
            parentY += current.getLocalToParentTransform().getTy();
            current = current.getParent();
        }

        if (current == null) {
            return null;
        }

        return editorViewModel.transformScreenToWorldCoordinates(
            new Point2D(parentX + point.getX(), parentY + point.getY()).multiply(editorViewModel.getZoomScale()));
    }
}
