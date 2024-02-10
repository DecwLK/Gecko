package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ElementScalerBlock extends Rectangle {
    private final ElementScalerViewElementDecorator decoratorTarget;
    private final int index;
    @Setter
    private boolean isDragging = false;

    public ElementScalerBlock(
        int index, ElementScalerViewElementDecorator decoratorTarget, double width, double height) {
        this.index = index;
        this.decoratorTarget = decoratorTarget;

        setWidth(width);
        setHeight(height);

        refreshListeners();
    }

    protected void refreshListeners() {
        decoratorTarget.getEdgePoints().get(index).removeListener((observable, oldValue, newValue) -> {
            if (!isDragging) {
                setLayoutX(newValue.getX() - (getWidth() / 2));
                setLayoutY(newValue.getY() - (getHeight() / 2));
            }
        });

        updatePosition();

        decoratorTarget.getEdgePoints().get(index).addListener((observable, oldValue, newValue) -> {
            if (!isDragging) {
                setLayoutX(newValue.getX() - (getWidth() / 2));
                setLayoutY(newValue.getY() - (getHeight() / 2));
            }
        });
    }

    public void updatePosition() {
        setLayoutX(decoratorTarget.getEdgePoints().get(index).getValue().getX() - (getWidth() / 2));
        setLayoutY(decoratorTarget.getEdgePoints().get(index).getValue().getY() - (getHeight() / 2));
    }

    public void setPosition(Point2D point) {
        setLayoutX(point.getX());
        setLayoutY(point.getY());

        decoratorTarget.setEdgePoint(index, point);
    }

    public void setCenter(Point2D point) {
        Point2D center = new Point2D(point.getX() - (getWidth() / 2), point.getY() - (getHeight() / 2));
        setLayoutX(center.getX());
        setLayoutY(center.getY());

        decoratorTarget.setEdgePoint(index, point);
    }

    public Point2D getPosition() {
        return new Point2D(getLayoutX(), getLayoutY());
    }

    public Point2D getCenter() {
        return new Point2D(getLayoutX() + (getWidth() / 2), getLayoutY() + (getHeight() / 2));
    }
}
