package org.gecko.view.views.viewelement.decorator;

import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

/**
 * A concrete representation of a {@link ViewElementDecorator} following the decorator pattern for scaling purposes. It
 * holds a reference to a Group containing the drawn target and a list of {@link ElementScalerBlock}s, one for each edge
 * point of the decorator.
 */
public class BlockElementScalerViewElementDecorator extends ElementScalerViewElementDecorator {
    public BlockElementScalerViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        getDecoratorTarget().accept(visitor);
    }
}
