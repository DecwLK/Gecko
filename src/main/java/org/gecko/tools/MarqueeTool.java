package org.gecko.tools;

import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MarqueeTool extends Tool {
    private Point2D startPosition;
    private final EditorViewModel editorViewModel;

    public MarqueeTool(ActionManager actionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.MARQUEE_TOOL);
        this.editorViewModel = editorViewModel;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);
        startPosition = null;
        view.setOnMousePressed(event -> {
            startPosition = new Point2D(event.getX(), event.getY());
        });

        view.setOnMouseReleased(event -> {
            if (startPosition == null) {
                return;
            }
            Point2D topLeft =
                new Point2D(Math.min(startPosition.getX(), event.getX()), Math.min(startPosition.getY(), event.getY()));
            Point2D bottomRight =
                new Point2D(Math.max(startPosition.getX(), event.getX()), Math.max(startPosition.getY(), event.getY()));
            Point2D size = bottomRight.subtract(topLeft);
            //prevent negative size and too small regions TODO properly
            if (size.getX() < 0 || size.getY() < 0) {
                startPosition = null;
                return;
            }
            Point2D worldTopLeft = editorViewModel.transformScreenToWorldCoordinates(topLeft);
            Point2D worldBottomRight = editorViewModel.transformScreenToWorldCoordinates(bottomRight);
            Set<PositionableViewModelElement<?>> elements =
                editorViewModel.getElementsInArea(worldTopLeft, worldBottomRight);
            if (elements.isEmpty()) {
                return;
            }
            actionManager.run(actionManager.getActionFactory().createSelectAction(elements, !event.isShiftDown()));

            startPosition = null;
        });
    }
}
