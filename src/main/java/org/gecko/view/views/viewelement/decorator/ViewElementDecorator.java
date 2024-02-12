package org.gecko.view.views.viewelement.decorator;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * An abstract representation of a {@link ViewElement} implementation following the decorator pattern and targeting a
 * decorated {@link ViewElement}, which can be selected and encapsulates a {@link PositionableViewModelElement}.
 */
@Getter
public abstract class ViewElementDecorator implements ViewElement<PositionableViewModelElement<?>> {

    private final ViewElement<?> decoratorTarget;

    @Setter
    private boolean selected;

    public ViewElementDecorator(ViewElement<?> decoratorTarget) {
        this.decoratorTarget = decoratorTarget;
    }

    @Override
    public int getZPriority() {
        if (selected) {
            return decoratorTarget.getZPriority() + 1;
        }
        return decoratorTarget.getZPriority();
    }

    @Override
    public PositionableViewModelElement<?> getTarget() {
        return decoratorTarget.getTarget();
    }

    @Override
    public ObservableList<Property<Point2D>> getEdgePoints() {
        return getDecoratorTarget().getEdgePoints();
    }

    @Override
    public boolean setEdgePoint(int index, Point2D point) {
        return getDecoratorTarget().setEdgePoint(index, point);
    }
}
