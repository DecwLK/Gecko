package org.gecko.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.geometry.Point2D;
import org.gecko.model.Element;

public abstract class PositionableViewModelElement<T extends Element> extends AbstractViewModelElement<T> {
    private Property<Point2D> position;
    private Property<Point2D> size;
    private BooleanProperty isSelected;


    PositionableViewModelElement(T target) {
        super(target);
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    public void accept() {
        // TODO: Add parameter PositionableViewModelVisitor.
    }
}
