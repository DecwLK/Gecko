package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Element;

@Getter
@Setter
public abstract class PositionableViewModelElement<T extends Element> extends AbstractViewModelElement<T> {
    protected final Property<Point2D> positionProperty;
    protected final Property<Point2D> sizeProperty;

    PositionableViewModelElement(T target) {
        super(target);
        this.positionProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.sizeProperty = new SimpleObjectProperty<>(new Point2D(-1, 1));
    }

    public Point2D getPosition() {
        return this.positionProperty.getValue();
    }

    public void setPosition(Point2D position) {
        this.positionProperty.setValue(position);
    }

    public Point2D getSize() {
        return this.sizeProperty.getValue();
    }

    public void setSize(Point2D size) {
        this.sizeProperty.setValue(size);
    }

    // TODO: Is there any relevant update operation that should take place at this level?
    public abstract void updateTarget();

    public abstract Object accept(PositionableViewModelElementVisitor visitor);
}
