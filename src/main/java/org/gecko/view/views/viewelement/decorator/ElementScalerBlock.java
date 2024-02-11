package org.gecko.view.views.viewelement.decorator;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

/** Represents a type of {@link Rectangle} used for displaying the scaling of elements. */
@Getter
public class ElementScalerBlock extends Rectangle {
    private final ElementScalerViewElementDecorator decoratorTarget;
    private final int index;
    @Setter
    private boolean isDragging = false;

    ChangeListener<Point2D> listener;

    public ElementScalerBlock(
        int index, ElementScalerViewElementDecorator decoratorTarget, double width, double height) {
        this.index = index;
        this.decoratorTarget = decoratorTarget;

        setWidth(width);
        setHeight(height);

        refreshListeners();
    }

    /**
     * Refreshes scaler block listeners and updates its position.
     */
    protected void refreshListeners() {
        if (listener != null) {
            decoratorTarget.getEdgePoints().get(index).removeListener(listener);
        }
        updatePosition();

        ChangeListener<Point2D> newListener = (observable, oldValue, newValue) -> {
            if (!isDragging) {
                setLayoutX(newValue.getX() - (getWidth() / 2));
                setLayoutY(newValue.getY() - (getHeight() / 2));
            }
        };
        decoratorTarget.getEdgePoints().get(index).addListener(newListener);
        listener = newListener;
    }

    /**
     * Updates the position of the scaler block to match the edge point.
     */
    public void updatePosition() {
        setLayoutX(decoratorTarget.getEdgePoints().get(index).getValue().getX() - (getWidth() / 2));
        setLayoutY(decoratorTarget.getEdgePoints().get(index).getValue().getY() - (getHeight() / 2));
    }

    /**
     * Sets the position of the scaler block and updates the edge point.
     *
     * @param point The new position of the scaler block.
     */
    public void setPosition(Point2D point) {
        setLayoutX(point.getX());
        setLayoutY(point.getY());

        decoratorTarget.setEdgePoint(index, point);
    }

    /**
     * Sets the center position of the scaler block and updates the edge point.
     *
     * @param point The new center of the scaler block.
     */
    public void setCenter(Point2D point) {
        Point2D center = new Point2D(point.getX() - (getWidth() / 2), point.getY() - (getHeight() / 2));
        setLayoutX(center.getX());
        setLayoutY(center.getY());

        decoratorTarget.setEdgePoint(index, point);
    }

    /**
     * Returns the position of the scaler block.
     *
     * @return The position of the scaler block.
     */
    public Point2D getPosition() {
        return new Point2D(getLayoutX(), getLayoutY());
    }

    /**
     * Returns the center position of the scaler block.
     *
     * @return The center position of the scaler block.
     */
    public Point2D getCenter() {
        return new Point2D(getLayoutX() + (getWidth() / 2), getLayoutY() + (getHeight() / 2));
    }
}
