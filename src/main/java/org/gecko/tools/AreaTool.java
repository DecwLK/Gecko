package org.gecko.tools;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;

public abstract class AreaTool extends Tool {

    private Point2D startPosition;
    private Rectangle area;

    public AreaTool(ActionManager actionManager, ToolType toolType) {
        super(actionManager, toolType);
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setPannable(false);
        view.setCursor(Cursor.CROSSHAIR);
        worldGroup.setMouseTransparent(true);
        startPosition = null;

        vbox.setOnMousePressed(event -> {
            startPosition = containerGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
            area = createNewArea();
            area.setX(startPosition.getX());
            area.setY(startPosition.getY());
            containerGroup.getChildren().add(area);
        });

        vbox.setOnMouseDragged(event -> {
            if (startPosition == null) {
                return;
            }
            Bounds areaBounds = calculateAreaBounds(startPosition,
                containerGroup.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
            area.setX(areaBounds.getMinX());
            area.setY(areaBounds.getMinY());
            area.setWidth(areaBounds.getWidth());
            area.setHeight(areaBounds.getHeight());
        });

        vbox.setOnMouseReleased(event -> {
            if (startPosition == null) {
                return;
            }
            containerGroup.getChildren().remove(area);
            Bounds areaBounds = calculateAreaBounds(getStartPosition(),
                containerGroup.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
            Bounds worldAreaBounds = worldGroup.sceneToLocal(containerGroup.localToScene(areaBounds));
            onAreaCreated(event, worldAreaBounds);
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
