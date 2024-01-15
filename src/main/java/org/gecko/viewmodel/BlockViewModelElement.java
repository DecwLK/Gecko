package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import org.gecko.model.Element;

public abstract class BlockViewModelElement<T extends Element> extends PositionableViewModelElement<T> implements Renamable {
    private final StringProperty nameProperty;

    BlockViewModelElement(T target) {
        super(target);
        this.nameProperty = new SimpleStringProperty();
    }

    @Override
    public String getName() {
        return this.nameProperty.getValue();
    }

    @Override
    public void setName(String nameProperty) {
        // TODO: further checks before updating?
        this.nameProperty.setValue(nameProperty);
    }

    private void resize(Point2D delta) {
        super.sizeProperty.getValue().add(delta);
    }

    public void move(Point2D delta) {
        // TODO: Check movement availability.
        super.positionProperty.getValue().add(delta);
    }

    public void scale(Point2D startPoint, Point2D delta) {
        Point2D topLeftCorner = super.positionProperty.getValue();
        Point2D bottomRightCorner = topLeftCorner.add(super.sizeProperty.getValue());
        Point2D topRightCorner = new Point2D(bottomRightCorner.getX(), topLeftCorner.getY());
        Point2D bottomLeftCorner = new Point2D(topLeftCorner.getX(), bottomRightCorner.getY());

        if (startPoint.equals(topLeftCorner) || startPoint.equals(bottomRightCorner)) {
            if (startPoint.equals(topLeftCorner)) {
                this.move(delta);
            }
            this.resize(delta);
        } else {
            Point2D newTopLeftCorner = super.positionProperty.getValue();;
            Point2D newBottomRightCorner = newTopLeftCorner.add(super.sizeProperty.getValue());

            double coordinateX;
            double coordinateY;

            if (startPoint.equals(topRightCorner)) {
                coordinateX = bottomRightCorner.getX() + delta.getX();
                coordinateY = topLeftCorner.getY() + delta.getY();

                newTopLeftCorner = new Point2D(topLeftCorner.getX(), coordinateY);
                newBottomRightCorner = new Point2D(coordinateX, bottomRightCorner.getY());
            } else if (startPoint.equals(bottomLeftCorner)) {
                coordinateX = topLeftCorner.getX() + delta.getX();
                coordinateY = bottomRightCorner.getY() + delta.getY();

                newTopLeftCorner = new Point2D(coordinateX, topLeftCorner.getY());
                newBottomRightCorner = new Point2D(bottomRightCorner.getX(), coordinateY);
            }

            setPosition(newTopLeftCorner);
            setSize(newBottomRightCorner.subtract(newTopLeftCorner));
        }
    }
}
