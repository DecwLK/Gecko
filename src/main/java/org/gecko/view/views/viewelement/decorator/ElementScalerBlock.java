package org.gecko.view.views.viewelement.decorator;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class ElementScalerBlock extends Rectangle {
    private final ElementScalerViewElementDecorator decoratorTarget;
    private final int index;

    public ElementScalerBlock(int index, ElementScalerViewElementDecorator decoratorTarget, int width, int height) {
        this.index = index;
        this.decoratorTarget = decoratorTarget;
    }

    public void movePoint(Point2D point) {
        setLayoutX(point.getX());
        setLayoutY(point.getY());

        decoratorTarget.setEdgePoint(index, point);
    }
}
