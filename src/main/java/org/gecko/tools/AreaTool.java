package org.gecko.tools;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.ViewElementPane;

/**
 * A concrete representation of an area-{@link Tool}, utilized for marking a rectangle-formed area in the view. Holds
 * the starting position and the afferent {@link Rectangle}.
 */
public abstract class AreaTool extends Tool {

    private Point2D startPosition;
    private Point2D startScreenPosition;
    private Rectangle area;
    private ScrollPane view;

    public AreaTool(ActionManager actionManager, ToolType toolType, boolean transparentElements) {
        super(actionManager, toolType, transparentElements);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        view = pane.draw();
        Pane world = pane.getWorld();
        view.setPannable(false);
        view.setCursor(Cursor.CROSSHAIR);
        startPosition = null;

        world.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            startPosition = pane.screenToLocalCoordinates(event.getScreenX(), event.getScreenY());
            startScreenPosition = new Point2D(event.getScreenX(), event.getScreenY());
            area = createNewArea();
            area.setX(startPosition.getX());
            area.setY(startPosition.getY());
            world.getChildren().add(area);
        });

        world.setOnMouseDragged(event -> {
            if (startPosition == null) {
                return;
            }
            Point2D dragPosition = pane.screenToLocalCoordinates(event.getScreenX(), event.getScreenY());
            Bounds areaBounds = calculateAreaBounds(startPosition, dragPosition);
            area.setX(areaBounds.getMinX());
            area.setY(areaBounds.getMinY());
            area.setWidth(areaBounds.getWidth());
            area.setHeight(areaBounds.getHeight());
        });

        world.setOnMouseReleased(event -> {
            if (startPosition == null) {
                return;
            }
            world.getChildren().remove(area);
            Point2D endPos = pane.screenToWorldCoordinates(event.getScreenX(), event.getScreenY());
            onAreaCreated(event, calculateAreaBounds(pane.screenToWorldCoordinates(startScreenPosition), endPos));
            startPosition = null;
            startScreenPosition = null;
        });
    }

    protected Bounds calculateAreaBounds(Point2D startPosition, Point2D endPosition) {
        Point2D topLeft = new Point2D(Math.min(startPosition.getX(), endPosition.getX()),
            Math.min(startPosition.getY(), endPosition.getY()));
        Point2D bottomRight = new Point2D(Math.max(startPosition.getX(), endPosition.getX()),
            Math.max(startPosition.getY(), endPosition.getY()));
        return new BoundingBox(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(),
            bottomRight.getY() - topLeft.getY());
    }

    abstract Rectangle createNewArea();

    abstract void onAreaCreated(MouseEvent event, Bounds worldBounds);
}
