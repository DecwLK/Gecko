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
        nameProperty.setValue(name);
    }

    public void scale(@NonNull Point2D startPoint, @NonNull Point2D newPoint) {
        Point2D newPosition = new Point2D(Math.min(Math.min(startPoint.getX(), newPoint.getX()), getSize().getX()),
            Math.min(Math.min(startPoint.getY(), newPoint.getY()), getSize().getY()));
        Point2D newSize = new Point2D(Math.max(Math.max(startPoint.getX(), newPoint.getX()), getSize().getX()),
            Math.max(Math.max(startPoint.getY(), newPoint.getY()), getSize().getY()));

        setPosition(newPosition);
        setSize(newSize);
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
