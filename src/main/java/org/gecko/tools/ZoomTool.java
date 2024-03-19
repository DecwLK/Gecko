package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;
import org.gecko.viewmodel.EditorViewModel;

/**
 * A concrete representation of a zoom-{@link Tool}, utilized for zooming in and out in the view.
 */
public class ZoomTool extends Tool {

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
                actionManager.run(actionManager.getActionFactory()
                    .createZoomAction(position, 1 / EditorViewModel.getDefaultZoomStep()));
            } else {
                actionManager.run(
                    actionManager.getActionFactory().createZoomAction(position, EditorViewModel.getDefaultZoomStep()));
            }
        });
    }
}
