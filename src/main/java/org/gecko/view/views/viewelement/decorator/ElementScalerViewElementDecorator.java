package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public class ElementScalerViewElementDecorator extends ViewElementDecorator {

    private static final int SCALER_SIZE = 10;

    @Getter
    private ElementScalerBlock[] scalers;

    public ElementScalerViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public Node drawElement() {
        Node node = getDecoratorTarget().drawElement();

        if (isSelected()) {
            StackPane decoratedNode = new StackPane();
            Pane scalarPane = new Pane();
            scalarPane.setPickOnBounds(false);

            // Create scalars
            scalers = new ElementScalerBlock[getEdgePoints().size()];
            for (int i = 0; i < scalers.length; i++) {
                scalers[i] = new ElementScalerBlock(i, this, SCALER_SIZE, SCALER_SIZE);
                scalers[i].setFill(Color.RED);

                Point2D edgePoint = getEdgePoints().get(i).getValue();
                scalers[i].setLayoutX(node.getLayoutX() + edgePoint.getX());
                scalers[i].setLayoutY(node.getLayoutY() + edgePoint.getY());

                scalarPane.getChildren().add(scalers[i]);
            }

            decoratedNode.getChildren().addAll(node, scalarPane);

            return decoratedNode;
        } else {
            return node;
        }
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
