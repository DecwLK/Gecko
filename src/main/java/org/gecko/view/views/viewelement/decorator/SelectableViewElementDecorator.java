package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public class SelectableViewElementDecorator extends ViewElementDecorator {

    @Getter
    @Setter
    private ViewElementDecorator decorator;

    public SelectableViewElementDecorator(ViewElement<?> decoratorTarget) {
        super(decoratorTarget);
    }

    @Override
    public Node drawElement() {
        Node node = getDecoratorTarget().drawElement();

        // draw border around decoratedNode
        Pane decoratedNode = new Pane();
        decoratedNode.setStyle("-fx-border-color: blue;");
        decoratedNode.setPrefSize(node.getLayoutBounds().getWidth(), node.getLayoutBounds().getHeight());
        decoratedNode.setLayoutX(node.getLayoutX());
        decoratedNode.setLayoutY(node.getLayoutY());

        decoratedNode.getChildren().add(node);

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
