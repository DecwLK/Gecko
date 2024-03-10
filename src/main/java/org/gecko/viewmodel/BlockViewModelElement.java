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
    public static final double MIN_WIDTH = 100;
    public static final double MIN_HEIGHT = 100;
    protected static final double HALF = 0.5;

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
        nameProperty.setValue(name);
    }

    /**
     * Manipulates the position and size of the element, so that the two points are two diagonally opposite corners of
     * the {@link BlockViewModelElement}.
     *
     * @param firstCornerPoint  the first corner point
     * @param secondCornerPoint the second corner point that is diagonally opposite to the first corner point
     */
    public boolean manipulate(@NonNull Point2D firstCornerPoint, @NonNull Point2D secondCornerPoint) {
        Point2D newStartPosition = new Point2D(Math.min(firstCornerPoint.getX(), secondCornerPoint.getX()),
            Math.min(firstCornerPoint.getY(), secondCornerPoint.getY()));
        Point2D newEndPosition = new Point2D(Math.max(firstCornerPoint.getX(), secondCornerPoint.getX()),
            Math.max(firstCornerPoint.getY(), secondCornerPoint.getY()));

        if (Math.abs(newEndPosition.getX() - newStartPosition.getX()) * Math.abs(
            newEndPosition.getY() - newStartPosition.getY()) <= MIN_HEIGHT * MIN_WIDTH) {
            return false;
        }

        setPosition(newStartPosition);
        setSize(newEndPosition.subtract(newStartPosition));
        return true;
    }

    @Override
    public Point2D getCenter() {
        return positionProperty.getValue().add(sizeProperty.getValue().multiply(HALF));
    }

    @Override
    public void updateTarget() throws ModelException {
        target.setName(getName());
    }
}
