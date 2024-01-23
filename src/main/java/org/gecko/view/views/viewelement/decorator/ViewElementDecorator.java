package org.gecko.view.views.viewelement.decorator;

import java.util.List;
import javafx.beans.property.Property;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.PositionableViewModelElement;

public abstract class ViewElementDecorator implements ViewElement<PositionableViewModelElement<?>> {

    @Getter
    private final ViewElement<?> decoratorTarget;

    @Getter
    @Setter
    private boolean selected;

    public ViewElementDecorator(ViewElement<?> decoratorTarget) {
        this.decoratorTarget = decoratorTarget;
    }

    @Override
    public PositionableViewModelElement<?> getTarget() {
        return decoratorTarget.getTarget();
    }

    @Override
    public List<Property<Point2D>> getEdgePoints() {
        return getDecoratorTarget().getEdgePoints();
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
        getDecoratorTarget().setEdgePoint(index, point);
    }
}
