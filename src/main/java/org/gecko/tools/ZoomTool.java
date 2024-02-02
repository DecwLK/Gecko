package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.ActionManager;

public class ZoomTool extends Tool {
    private static final double ZOOM_SCALE = 1.1;

    public ZoomTool(ActionManager actionManager) {
        super(actionManager, ToolType.ZOOM_TOOL);
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);

        view.setOnMouseClicked(event -> {
            Point2D position = new Point2D(event.getX(), event.getY());

            if (event.isShiftDown()) {
                actionManager.run(actionManager.getActionFactory().createZoomAction(position, 1 / ZOOM_SCALE));
            } else {
                actionManager.run(actionManager.getActionFactory().createZoomAction(position, ZOOM_SCALE));
            }
        });
    }
}
