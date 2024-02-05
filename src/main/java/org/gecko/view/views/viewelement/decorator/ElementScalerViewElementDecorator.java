package org.gecko.view.views.viewelement.decorator;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

@Getter
public class ElementScalerViewElementDecorator extends ViewElementDecorator {

    private static final int SCALER_SIZE = 10;
    private static final int IGNORE_Z_PRIORITY = 10000;

    private final Group decoratedNode;
    private final List<ElementScalerBlock> scalers;

    public ElementScalerViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
        scalers = new ArrayList<>();
        decoratedNode = new Group(decoratorTarget.drawElement());

        for (int i = 0; i < decoratorTarget.getEdgePoints().size(); i++) {
            ElementScalerBlock scalerBlock = new ElementScalerBlock(i, this, SCALER_SIZE, SCALER_SIZE);
            scalerBlock.setFill(Color.RED);

            scalers.add(scalerBlock);
            decoratedNode.getChildren().add(scalerBlock);
        }
    }

    @Override
    public Node drawElement() {
        return decoratedNode;
    }

    @Override
    public void setSelected(boolean selected) {
        if (scalers != null) {
            for (ElementScalerBlock scaler : scalers) {
                scaler.setVisible(selected);
            }
        }
        super.setSelected(selected);
    }

    @Override
    public int getZPriority() {
        if (isSelected()) {
            return getDecoratorTarget().getZPriority() + IGNORE_Z_PRIORITY;
        }
        return getDecoratorTarget().getZPriority();
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
