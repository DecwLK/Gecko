package org.gecko.viewmodel;

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Element;

/**
 * Represents an abstraction of a view model element that is graphically represented in a Gecko project. A
 * {@link PositionableViewModelElement} is described by a position- and a size-{@link Point2D}. Contains methods for
 * managing the afferent data.
 */
@Getter
@Setter
public abstract class PositionableViewModelElement<T extends Element> extends AbstractViewModelElement<T> {
    protected final Property<Point2D> positionProperty;
    protected final Property<Point2D> sizeProperty;
    private final BooleanProperty isCurrentlyModified;

    private final Set<PositionableViewModelElement<?>> observers;

    private static final Point2D DEFAULT_SIZE = new Point2D(200, 300);

    PositionableViewModelElement(int id, @NonNull T target) {
        super(id, target);
        this.positionProperty = new SimpleObjectProperty<>(Point2D.ZERO);
        this.sizeProperty = new SimpleObjectProperty<>(DEFAULT_SIZE);
        this.observers = new HashSet<>();
        this.isCurrentlyModified = new SimpleBooleanProperty(false);
    }

    public Point2D getPosition() {
        return positionProperty.getValue();
    }

    public void setPosition(@NonNull Point2D position) {
        positionProperty.setValue(position);
    }

    public Point2D getSize() {
        return sizeProperty.getValue();
    }

    public void setSize(@NonNull Point2D size) {
        sizeProperty.setValue(size);
    }

    public Point2D getCenter() {
        return new Point2D(positionProperty.getValue().getX() + sizeProperty.getValue().getX() / 2,
            positionProperty.getValue().getY() + sizeProperty.getValue().getY() / 2);
    }

    public void setCenter(@NonNull Point2D point) {
        setPosition(new Point2D(point.getX() - sizeProperty.getValue().getX() / 2,
            point.getY() - sizeProperty.getValue().getY() / 2));
    }

    public void setCurrentlyModified(boolean isCurrentlyModified) {
        this.isCurrentlyModified.setValue(isCurrentlyModified);
    }

    public boolean isCurrentlyModified() {
        return isCurrentlyModified.getValue();
    }

    public abstract <S> S accept(@NonNull PositionableViewModelElementVisitor<S> visitor);
}
