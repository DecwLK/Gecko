package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Element;

@Getter
@Setter
public abstract class PositionableViewModelElement<T extends Element> extends AbstractViewModelElement<T> {
    protected final Property<Point2D> positionProperty;
    protected final Property<Point2D> sizeProperty;

    PositionableViewModelElement(@NonNull T target) {
        super(target);
        this.positionProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.sizeProperty = new SimpleObjectProperty<>(new Point2D(-1, 1));
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

    // TODO: Is there any relevant update operation that should take place at this level?

    public abstract Object accept(@NonNull PositionableViewModelElementVisitor visitor);
}
