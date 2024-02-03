package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class RegionCreatorTool extends Tool {
    private Point2D startPosition;

    public RegionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.REGION_CREATOR);
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
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
            if (size.getX() < 0 || size.getY() < 0 || size.getX() * size.getY() < 100) {
                startPosition = null;
                return;
            }
            actionManager.run(actionManager.getActionFactory().createCreateRegionViewModelElementAction(topLeft, size));
            startPosition = null;
        });
    }
}
