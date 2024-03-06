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
    private Rectangle area;

    public AreaTool(ActionManager actionManager, ToolType toolType) {
        super(actionManager, toolType);
    }

    @Override
    public void visitView(ViewElementPane pane) {
        super.visitView(pane);
        ScrollPane view = pane.draw();
        view.setPannable(false);
        view.setCursor(Cursor.CROSSHAIR);
        startPosition = null;
        Pane world = (Pane) view.getContent();

        view.setOnMousePressed(event -> {
            System.out.println("pressed");
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            startPosition = world.sceneToLocal(event.getSceneX(), event.getSceneY());
            area = createNewArea();
            area.setX(startPosition.getX());
            area.setY(startPosition.getY());
            world.getChildren().add(area);
        });

        view.setOnMouseDragged(event -> {
            System.out.println("dragged");
            if (startPosition == null) {
                return;
            }
            Point2D dragPosition = world.sceneToLocal(event.getSceneX(), event.getSceneY());
            Bounds areaBounds = calculateAreaBounds(startPosition, dragPosition);
            area.setX(areaBounds.getMinX());
            area.setY(areaBounds.getMinY());
            area.setWidth(areaBounds.getWidth());
            area.setHeight(areaBounds.getHeight());
        });

        view.setOnMouseReleased(event -> {
            System.out.println("released");
            if (startPosition == null) {
                return;
            }
            world.getChildren().remove(area); //TODO
            Bounds areaBounds =
                calculateAreaBounds(startPosition, world.sceneToLocal(event.getSceneX(), event.getSceneY()));
            onAreaCreated(event, areaBounds);
            startPosition = null;
        });
    }

    abstract Rectangle createNewArea();

    protected Bounds calculateAreaBounds(Point2D startPosition, Point2D eventPosition) {
        Point2D topLeft = new Point2D(Math.min(startPosition.getX(), eventPosition.getX()),
            Math.min(startPosition.getY(), eventPosition.getY()));
        Point2D bottomRight = new Point2D(Math.max(startPosition.getX(), eventPosition.getX()),
            Math.max(startPosition.getY(), eventPosition.getY()));
        return new BoundingBox(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(),
            bottomRight.getY() - topLeft.getY());
    }

    abstract void onAreaCreated(MouseEvent event, Bounds worldAreaBounds);

    protected Point2D getStartPosition() {
        return startPosition;
    }
}
