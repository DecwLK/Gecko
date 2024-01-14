package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Element;

@Getter
@Setter
public abstract class PositionableViewModelElement<T extends Element> extends AbstractViewModelElement<T> {
    protected Property<Point2D> position;
    protected Property<Point2D> size;
    protected BooleanProperty isSelected;

    PositionableViewModelElement(T target) {
        super(target);
        this.position = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.size = new SimpleObjectProperty<>(new Point2D(-1, 1));
        this.isSelected = new SimpleBooleanProperty(false);
    }

    // TODO: Is there any relevant update operation that should take place at this level?
    public abstract void updateTarget();

    public abstract Object accept(PositionableViewModelElementVisitor visitor);
}
