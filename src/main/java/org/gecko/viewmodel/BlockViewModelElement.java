package org.gecko.viewmodel;

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

    public void move(Point2D delta) {
        // TODO
    }

    public void scale(Point2D startPoint, Point2D delta) {
        // TODO
    }
}
