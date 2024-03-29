package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.decorator.BlockElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SelectionManager;
import org.gecko.viewmodel.SystemConnectionViewModel;

/**
 * A concrete representation of an area-{@link Tool}, utilized for marking a rectangle-formed area in the view. Holds
 * the {@link Point2D starting position}  and the afferent {@link javafx.scene.shape.Rectangle}.
 */
public class CursorTool extends Tool {
    private boolean isDragging = false;
    private final SelectionManager selectionManager;
    private final EditorViewModel editorViewModel;
    private Point2D startDragPosition;
    private Point2D previousDragPosition;
    private Point2D oldPosition;
    private Point2D oldSize;
    private ViewElementPane viewPane;

    private static final double DRAG_THRESHOLD = 4;

    public CursorTool(ActionManager actionManager, SelectionManager selectionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.CURSOR, false);
        this.selectionManager = selectionManager;
        this.editorViewModel = editorViewModel;
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setCursor(Cursor.DEFAULT);
        pane.draw().setPannable(false);
        viewPane = pane;
        setDeselectHandler(pane.draw());
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
        setOpenSystemHandler(systemViewElement);
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        super.visit(edgeViewElement);
        edgeViewElement.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            selectElement(edgeViewElement, !event.isShiftDown());
            event.consume();
        });
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
        systemConnectionViewElement.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            selectElement(systemConnectionViewElement, !event.isShiftDown());
            event.consume();
        });
    }

    @Override
    public void visit(ConnectionElementScalerViewElementDecorator connectionElementScalerViewElementDecorator) {
        super.visit(connectionElementScalerViewElementDecorator);

        for (ElementScalerBlock scaler : connectionElementScalerViewElementDecorator.getScalers()) {
            setConnectionScalerElementsHandlers(scaler);
        }
    }

    @Override
    public void visit(BlockElementScalerViewElementDecorator blockElementScalerViewElementDecorator) {
        super.visit(blockElementScalerViewElementDecorator);
        for (ElementScalerBlock scaler : blockElementScalerViewElementDecorator.getScalers()) {
            setBlockScalerElementHandlers(scaler);
        }
    }

    private void setDeselectHandler(Node node) {
        node.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            selectionManager.deselectAll();
            event.consume();
        });
    }

    private void setBlockScalerElementHandlers(ElementScalerBlock scaler) {
        scaler.setOnMousePressed(event -> {
            if (isDragging) {
                return;
            }
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            startDraggingElementHandler(event);
            scaler.setDragging(true);
            oldPosition = scaler.getDecoratorTarget().getTarget().getPosition();
            oldSize = scaler.getDecoratorTarget().getTarget().getSize();
            startDragPosition = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            isDragging = true;
            event.consume();
        });

        scaler.setOnMouseDragged(event -> {
            if (!isDragging) {
                return;
            }
            Point2D newPosition = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            if (!scaler.setCenter(newPosition)) {
                runResizeAction(scaler);
                cancelDrag(scaler);
            }
            event.consume();
        });

        scaler.setOnMouseReleased(event -> {
            if (!isDragging) {
                return;
            }
            scaler.setCenter(viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY()));
            runResizeAction(scaler);
            cancelDrag(scaler);
            event.consume();
        });
    }

    private void runResizeAction(ElementScalerBlock scaler) {
        scaler.setDragging(false);
        BlockViewModelElement<?> target = (BlockViewModelElement<?>) scaler.getDecoratorTarget().getTarget();
        Action resizeAction = actionManager.getActionFactory()
            .createScaleBlockViewModelElementAction(target, scaler, oldPosition, oldSize, true);
        actionManager.run(resizeAction);
    }

    private void setConnectionScalerElementsHandlers(ElementScalerBlock scaler) {
        scaler.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            startDraggingElementHandler(event);
            scaler.setDragging(true);
            scaler.getDecoratorTarget().getTarget().setCurrentlyModified(true);
            event.consume();
        });
        scaler.setOnMouseDragged(event -> {
            if (!isDragging) {
                return;
            }
            Point2D eventPosition = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            Point2D delta = eventPosition.subtract(previousDragPosition);
            scaler.setLayoutPosition(scaler.getLayoutPosition().add(delta));
            previousDragPosition = eventPosition;
            event.consume();
        });
        scaler.setOnMouseReleased(event -> {
            if (!isDragging) {
                return;
            }
            Point2D endWorldPos = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            scaler.setLayoutPosition(scaler.getLayoutPosition().add(startDragPosition.subtract(endWorldPos)));
            Action moveAction;

            if (editorViewModel.isAutomatonEditor()) {
                moveAction = actionManager.getActionFactory()
                    .createMoveEdgeViewModelElementAction((EdgeViewModel) scaler.getDecoratorTarget().getTarget(),
                        scaler, endWorldPos.subtract(startDragPosition));
            } else {
                moveAction = actionManager.getActionFactory()
                    .createMoveSystemConnectionViewModelElementAction(
                        (SystemConnectionViewModel) scaler.getDecoratorTarget().getTarget(), scaler,
                        endWorldPos.subtract(startDragPosition));
            }

            actionManager.run(moveAction);
            scaler.getDecoratorTarget().getTarget().setCurrentlyModified(false);
            cancelDrag(scaler);
            event.consume();
        });
    }

    private void setOpenSystemHandler(SystemViewElement systemViewElement) {
        systemViewElement.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            if (event.getClickCount() == 2) {
                Action openSystemAction =
                    actionManager.getActionFactory().createViewSwitchAction(systemViewElement.getTarget(), false);
                actionManager.run(openSystemAction);
            }
            event.consume();
        });
    }

    private void setDragAndSelectHandlers(ViewElement<?> element) {
        element.drawElement().setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            startDraggingElementHandler(event);

            if (!element.isSelected()) {
                selectElement(element, !event.isShiftDown());
            }
            event.consume();
        });
        element.drawElement().setOnMouseDragged(event -> {
            dragElementsHandler(event);
            event.consume();
        });
        element.drawElement().setOnMouseReleased(event -> {
            stopDraggingElementHandler(event);

            if (event.getButton() == MouseButton.PRIMARY && element.isSelected()
                && startDragPosition.distance(previousDragPosition) * editorViewModel.getZoomScale() < DRAG_THRESHOLD) {
                selectElement(element, !event.isShiftDown());
            }
            event.consume();
        });
    }

    private void startDraggingElementHandler(MouseEvent event) {
        isDragging = true;
        startDragPosition = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
        previousDragPosition = startDragPosition;
        event.consume();
    }

    private void dragElementsHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Point2D eventPosition = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
        Point2D delta = eventPosition.subtract(previousDragPosition);
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setCurrentlyModified(true);
            element.setPosition(element.getPosition().add(delta));
        });
        previousDragPosition = eventPosition;
        event.consume();
    }

    private void stopDraggingElementHandler(MouseEvent event) {
        if (!isDragging) {
            return;
        }
        Point2D endWorldPos = viewPane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
        selectionManager.getCurrentSelection().forEach(element -> {
            element.setCurrentlyModified(false);
            element.setPosition(element.getPosition().add(startDragPosition.subtract(endWorldPos)));
        });
        Action moveAction = actionManager.getActionFactory()
            .createMoveBlockViewModelElementAction(endWorldPos.subtract(startDragPosition));
        actionManager.run(moveAction);
        cancelDrag();
        event.consume();
    }

    private void selectElement(ViewElement<?> viewElement, boolean newSelection) {
        Action select = actionManager.getActionFactory().createSelectAction(viewElement.getTarget(), newSelection);
        actionManager.run(select);
    }

    private void cancelDrag(ElementScalerBlock scaler) {
        scaler.setDragging(false);
        cancelDrag();
    }

    private void cancelDrag() {
        isDragging = false;
    }
}
