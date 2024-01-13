package org.gecko.viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import org.gecko.model.Element;

public abstract class BlockViewModelElement<T extends Element> extends PositionableViewModelElement<T> implements Renamable {
    private StringProperty name;

    BlockViewModelElement(T target) {
        super(target);
        this.name = new SimpleStringProperty();
    }

    @Override
    public String getName() {
        return this.name.getValue();
    }

    @Override
    public void setName(String name) {
        // TODO: further checks before updating?
        this.name.setValue(name);
    }

    private void resize(Point2D delta) {
        // Moves bottom-right corner with delta.
        Point2D currentSizeValue = super.getSize().getValue();

        double newXCoordinate = currentSizeValue.getX() + delta.getX();
        double newYCoordinate = currentSizeValue.getY() + delta.getY();
        Point2D newSize = new Point2D(newXCoordinate, newYCoordinate);

        super.setSize(new SimpleObjectProperty<>(newSize));
    }

    public void move(Point2D delta) {
        // TODO: Check movement availability.
        Point2D currentPositionValue = super.getPosition().getValue();
        Point2D currentSizeValue = super.getSize().getValue();

        // Update top-left corner's position:
        double newXCoordinate = currentPositionValue.getX() + delta.getX();
        double newYCoordinate = currentPositionValue.getY() + delta.getY();
        Point2D newPosition = new Point2D(newXCoordinate, newYCoordinate);

        super.setPosition(new SimpleObjectProperty<>(newPosition));

        // Update bottom-right corner's position:
        newXCoordinate = currentSizeValue.getX() + delta.getX();
        newYCoordinate = currentSizeValue.getY() + delta.getY();
        newPosition = new Point2D(newXCoordinate, newYCoordinate);

        super.setSize(new SimpleObjectProperty<>(newPosition));
    }

    public void scale(Point2D startPoint, Point2D delta) {
        Point2D topLeftCorner = super.getPosition().getValue();
        Point2D bottomRightCorner = super.getSize().getValue();
        Point2D topRightCorner = new Point2D(bottomRightCorner.getX(), topLeftCorner.getY());
        Point2D bottomLeftCorner = new Point2D(topLeftCorner.getX(), bottomRightCorner.getY());

        if (startPoint.equals(topLeftCorner) || startPoint.equals(bottomRightCorner)) {
            if (startPoint.equals(topLeftCorner)) {
                this.move(delta);
            }
            this.resize(delta);
        } else {
            Point2D currentPositionValue = super.getPosition().getValue();
            Point2D currentSizeValue = super.getSize().getValue();

            Point2D newPosition = currentPositionValue;
            Point2D newSize = currentSizeValue;

            double newXCoordinate;
            double newYCoordinate;

            if (startPoint.equals(topRightCorner)) {
                newYCoordinate = currentPositionValue.getY() + delta.getY();
                newPosition = new Point2D(currentPositionValue.getX(), newYCoordinate);

                newXCoordinate = currentSizeValue.getX() + delta.getX();
                newSize = new Point2D(newXCoordinate, currentSizeValue.getY());
            } else if (startPoint.equals(bottomLeftCorner)){
                newXCoordinate = currentPositionValue.getX() + delta.getX();
                newPosition = new Point2D(newXCoordinate, currentPositionValue.getY());

                newYCoordinate = currentSizeValue.getY() + delta.getY();
                newSize = new Point2D(currentSizeValue.getX(), newYCoordinate);
            }

            super.setPosition(new SimpleObjectProperty<>(newPosition));
            super.setSize(new SimpleObjectProperty<>(newSize));
        }
    }
}
