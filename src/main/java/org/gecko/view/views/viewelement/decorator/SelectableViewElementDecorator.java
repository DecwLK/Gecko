package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

/**
 * A concrete representation of a {@link ViewElementDecorator} following the decorator pattern for selection purposes.
 * It holds a reference to a Group containing the drawn target and borderline Path
 */
public class SelectableViewElementDecorator extends ViewElementDecorator {
    private static final String SELECTED_BORDER_STYLE_CLASS = "selected-border";

    public SelectableViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public Node drawElement() {
        return getDecoratorTarget().drawElement();
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            getDecoratorTarget().drawElement().getStyleClass().add(SELECTED_BORDER_STYLE_CLASS);
        } else {
            getDecoratorTarget().drawElement().getStyleClass().remove(SELECTED_BORDER_STYLE_CLASS);
        }
        super.setSelected(selected);
        getDecoratorTarget().setSelected(selected);
    }

    @Override
    public Point2D getPosition() {
        return getDecoratorTarget().getPosition();
    }


    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }
}
