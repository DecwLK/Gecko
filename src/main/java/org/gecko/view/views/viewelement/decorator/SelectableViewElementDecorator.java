package org.gecko.view.views.viewelement.decorator;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public class SelectableViewElementDecorator extends ViewElementDecorator {

    @Setter
    private boolean selected;

    public SelectableViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public Node drawElement() {
        Node node = getDecoratorTarget().drawElement();

        if (selected) {
            // draw border around decoratedNode
            Pane decoratedNode = new Pane();
            decoratedNode.setStyle("-fx-border-color: blue;");
            decoratedNode.setPrefSize(node.getLayoutBounds().getWidth(), node.getLayoutBounds().getHeight());
            decoratedNode.setLayoutX(node.getLayoutX());
            decoratedNode.setLayoutY(node.getLayoutY());

            decoratedNode.getChildren().add(node);

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
    }
}
