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
        // Moves bottom-right corner with delta.
        Point2D currentSizeValue = super.sizeProperty.getValue();

        double xCoordinate = currentSizeValue.getX() + delta.getX();
        double yCoordinate = currentSizeValue.getY() + delta.getY();
        Point2D newSize = new Point2D(xCoordinate, yCoordinate);

        setSize(newSize);
    }

    public void move(Point2D delta) {
        // TODO: Check movement availability.
        Point2D currentPositionValue = super.positionProperty.getValue();
        Point2D currentSizeValue = super.sizeProperty.getValue();

        // Update top-left corner's position:
        double xCoordinate = currentPositionValue.getX() + delta.getX();
        double yCoordinate = currentPositionValue.getY() + delta.getY();
        Point2D newPosition = new Point2D(xCoordinate, yCoordinate);

        setPosition(newPosition);

        // Update bottom-right corner's position:
        xCoordinate = currentSizeValue.getX() + delta.getX();
        yCoordinate = currentSizeValue.getY() + delta.getY();
        newPosition = new Point2D(xCoordinate, yCoordinate);

        setSize(newPosition);
    }

    public void scale(Point2D startPoint, Point2D delta) {
        Point2D topLeftCorner = super.positionProperty.getValue();
        Point2D bottomRightCorner = super.sizeProperty.getValue();
        Point2D topRightCorner = new Point2D(bottomRightCorner.getX(), topLeftCorner.getY());
        Point2D bottomLeftCorner = new Point2D(topLeftCorner.getX(), bottomRightCorner.getY());

        if (startPoint.equals(topLeftCorner) || startPoint.equals(bottomRightCorner)) {
            if (startPoint.equals(topLeftCorner)) {
                this.move(delta);
            }
            this.resize(delta);
        } else {
            Point2D currentPositionValue = super.positionProperty.getValue();
            Point2D currentSizeValue = super.sizeProperty.getValue();

            Point2D newPosition = currentPositionValue;
            Point2D newSize = currentSizeValue;

            double xCoordinate;
            double yCoordinate;

            if (startPoint.equals(topRightCorner)) {
                yCoordinate = currentPositionValue.getY() + delta.getY();
                newPosition = new Point2D(currentPositionValue.getX(), yCoordinate);

                xCoordinate = currentSizeValue.getX() + delta.getX();
                newSize = new Point2D(xCoordinate, currentSizeValue.getY());
            } else if (startPoint.equals(bottomLeftCorner)) {
                xCoordinate = currentPositionValue.getX() + delta.getX();
                newPosition = new Point2D(xCoordinate, currentPositionValue.getY());

                yCoordinate = currentSizeValue.getY() + delta.getY();
                newSize = new Point2D(currentSizeValue.getX(), yCoordinate);
            }

            setPosition(newPosition);
            setSize(newSize);
        }
    }
}
