package org.gecko.tools;

import java.util.Set;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MarqueeTool extends Tool {
    private Point2D startPosition;
    private final EditorViewModel editorViewModel;
    private Rectangle marquee;

    public MarqueeTool(ActionManager actionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.MARQUEE_TOOL);
        this.editorViewModel = editorViewModel;
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setCursor(Cursor.CROSSHAIR);
        startPosition = null;

        vbox.setOnMousePressed(event -> {
            startPosition = vbox.sceneToLocal(event.getSceneX(), event.getSceneY());
            createNewMarquee();
            marquee.setX(startPosition.getX());
            marquee.setY(startPosition.getY());
            containerGroup.getChildren().add(marquee);
        });

        vbox.setOnMouseDragged(event -> {
            if (startPosition == null) {
                return;
            }
            Bounds marqueeBounds = calculateMarqueeBounds(startPosition,
                vbox.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
            marquee.setX(marqueeBounds.getMinX());
            marquee.setY(marqueeBounds.getMinY());
            marquee.setWidth(marqueeBounds.getWidth());
            marquee.setHeight(marqueeBounds.getHeight());
        });

        vbox.setOnMouseReleased(event -> {
            if (startPosition == null) {
                return;
            }
            containerGroup.getChildren().remove(marquee);

            Bounds marqueeBounds = calculateMarqueeBounds(startPosition,
                vbox.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
            marqueeBounds = worldGroup.sceneToLocal(vbox.localToScene(marqueeBounds));
            Set<PositionableViewModelElement<?>> elements = editorViewModel.getElementsInArea(marqueeBounds);
            if (elements.isEmpty()) {
                return;
            }
            actionManager.run(actionManager.getActionFactory().createSelectAction(elements, !event.isShiftDown()));
            startPosition = null;
        });
    }

    private void createNewMarquee() {
        marquee = new Rectangle();
        marquee.setFill(null);
        marquee.setStroke(Color.BLUE);
        marquee.setStrokeWidth(1);
        marquee.getStrokeDashArray().addAll(5d, 5d);
    }

    private Bounds calculateMarqueeBounds(Point2D startPosition, Point2D eventPosition) {
        Point2D topLeft = new Point2D(Math.min(startPosition.getX(), eventPosition.getX()),
            Math.min(startPosition.getY(), eventPosition.getY()));
        Point2D bottomRight = new Point2D(Math.max(startPosition.getX(), eventPosition.getX()),
            Math.max(startPosition.getY(), eventPosition.getY()));
        return new BoundingBox(topLeft.getX(), topLeft.getY(), bottomRight.getX() - topLeft.getX(),
            bottomRight.getY() - topLeft.getY());
    }
}
