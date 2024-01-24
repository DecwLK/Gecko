package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class ElementScalerBlock extends Rectangle {
    private final ElementScalerViewElementDecorator decoratorTarget;
    private final int index;

    public ElementScalerBlock(int index, ElementScalerViewElementDecorator decoratorTarget, int width, int height) {
        this.index = index;
        this.decoratorTarget = decoratorTarget;

        setWidth(width);
        setHeight(height);

        setLayoutX(decoratorTarget.getEdgePoints().get(index).getValue().getX() - (getWidth() / 2));
        setLayoutY(decoratorTarget.getEdgePoints().get(index).getValue().getY() - (getHeight() / 2));
    }

    public void movePoint(Point2D point) {
        setLayoutX(point.getX());
        setLayoutY(point.getY());

        decoratorTarget.setEdgePoint(index, point);
    }
}
