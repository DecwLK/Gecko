package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.viewmodel.BlockViewModelElement;

public class ElementScalerViewElementDecorator extends ViewElementDecorator {

    @Getter
    @Setter
    private ViewElementDecorator decorator;

    public ElementScalerViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public Node drawElement() {
        Node decoratedNode = getDecoratorTarget().drawElement();

        return decoratedNode;
    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
