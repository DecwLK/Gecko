package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of a zoom-{@link Tool}, utilized for zooming in and out in the view.
 */
public class ZoomTool extends Tool {
    private static final double ZOOM_SCALE = 1.1;

    public ZoomTool(ActionManager actionManager) {
        super(actionManager, ToolType.ZOOM_TOOL, true);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        pane.draw().setCursor(Cursor.CROSSHAIR);

        pane.draw().setOnMouseClicked(event -> {
            Point2D position = pane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());

            if (event.isShiftDown()) {
                actionManager.run(actionManager.getActionFactory().createZoomAction(position, 1 / ZOOM_SCALE));
            } else {
                actionManager.run(actionManager.getActionFactory().createZoomAction(position, ZOOM_SCALE));
            }
        });
    }
}
