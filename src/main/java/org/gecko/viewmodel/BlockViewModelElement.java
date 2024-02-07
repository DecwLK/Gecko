package org.gecko.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Element;

/**
 * Represents an abstraction of a view model element that has a rectangular shape in a Gecko project. A
 * {@link BlockViewModelElement} has a name. Contains methods for moving and scaling the element.
 */
@Getter
public abstract class BlockViewModelElement<T extends Element & org.gecko.model.Renamable>
    extends PositionableViewModelElement<T> implements Renamable {
    private final StringProperty nameProperty;

    BlockViewModelElement(int id, @NonNull T target) {
        super(id, target);
        this.nameProperty = new SimpleStringProperty();
        setName(target.getName());
    }

    @Override
    public String getName() {
        return nameProperty.getValue();
    }

    @Override
    public void setName(@NonNull String name) {
        // TODO: further checks before updating?
        nameProperty.setValue(name);
    }

    private void resize(@NonNull Point2D delta) {
        sizeProperty.getValue().add(delta);
    }

    public void move(@NonNull Point2D delta) {
        // TODO: Check movement availability.
        positionProperty.getValue().add(delta);
    }

    public void scale(@NonNull Point2D startPoint, @NonNull Point2D delta) {
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
            Point2D newTopLeftCorner = super.positionProperty.getValue();
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

    @Override
    public Point2D getCenter() {
        return positionProperty.getValue().add(sizeProperty.getValue().multiply(0.5));
    }

    @Override
    public void updateTarget() throws ModelException {
        target.setName(getName());
    }
}
