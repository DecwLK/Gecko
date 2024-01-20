package org.gecko.view.views.viewelement.decorator;

import lombok.Getter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElement;

public abstract class ViewElementDecorator implements ViewElement<PositionableViewModelElement<?>> {

    @Getter
    private final ViewElement<?> decoratorTarget;

    public ViewElementDecorator(ViewElement<?> decoratorTarget) {
        this.decoratorTarget = decoratorTarget;
    }

    @Override
    public PositionableViewModelElement<?> getTarget() {
        return decoratorTarget.getTarget();
    }
}
