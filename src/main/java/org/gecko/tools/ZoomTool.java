package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;

/**
 * A concrete representation of a zoom-{@link Tool}, utilized for zooming in and out in the view.
 */
public class ZoomTool extends Tool {
    private static final double ZOOM_SCALE = 1.1;

    public ZoomTool(ActionManager actionManager) {
        super(actionManager, ToolType.ZOOM_TOOL);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setCursor(Cursor.CROSSHAIR);
        worldGroup.setMouseTransparent(true);

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
