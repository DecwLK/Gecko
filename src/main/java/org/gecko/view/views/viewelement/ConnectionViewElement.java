package org.gecko.view.views.viewelement;


import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import lombok.Getter;

@Getter
public abstract class ConnectionViewElement extends Path {

    private final MoveTo startElement;
    private final LineTo endElement;
    private final List<Property<Point2D>> pathPoints;

    protected ConnectionViewElement() {
        pathPoints = new ArrayList<>();

        startElement = new MoveTo();
        Property<Point2D> startPoint = new SimpleObjectProperty<>(new Point2D(0, 0));

        // TODO: NEEDS TO BE TESTED
        startElement.xProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldStartPoint = new Point2D(startPoint.getValue().getX(), startPoint.getValue().getY());
            startPoint.getValue().subtract(oldStartPoint).add(newValue.doubleValue(), oldStartPoint.getY());
        });
        startElement.yProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldStartPoint = new Point2D(startPoint.getValue().getX(), startPoint.getValue().getY());
            startPoint.getValue().subtract(oldStartPoint).add(oldStartPoint.getX(), newValue.doubleValue());
        });

        startPoint.addListener((observable, oldValue, newValue) -> {
            startElement.setX(newValue.getX());
            startElement.setY(newValue.getY());
        });

        pathPoints.add(startPoint);

        endElement = new LineTo();
        Property<Point2D> endPoint = new SimpleObjectProperty<>(new Point2D(0, 0));

        endElement.xProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldEndPoint = new Point2D(endPoint.getValue().getX(), endPoint.getValue().getY());
            endPoint.getValue().subtract(oldEndPoint).add(newValue.doubleValue(), oldEndPoint.getY());
        });
        endElement.yProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldEndPoint = new Point2D(endPoint.getValue().getX(), endPoint.getValue().getY());
            endPoint.getValue().subtract(oldEndPoint).add(oldEndPoint.getX(), newValue.doubleValue());
        });
        pathPoints.add(endPoint);

        getElements().addAll(startElement, endElement);
    }

    protected void setPathPoint(int index, Point2D point) {
        Point2D oldPoint = pathPoints.get(index).getValue();
        pathPoints.get(index).getValue().subtract(oldPoint).add(point);
    }

    protected void createPathPoint(Point2D point) {
        LineTo newPoint = new LineTo();
        Property<Point2D> newPointCoord = new SimpleObjectProperty<>(new Point2D(point.getX(), point.getY()));

        newPoint.xProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldPointCoord = new Point2D(newPointCoord.getValue().getX(), newPointCoord.getValue().getY());
            newPointCoord.getValue().subtract(oldPointCoord).add(newValue.doubleValue(), oldPointCoord.getY());
        });
        newPoint.yProperty().addListener((observable, oldValue, newValue) -> {
            Point2D oldPointCoord = new Point2D(newPointCoord.getValue().getX(), newPointCoord.getValue().getY());
            newPointCoord.getValue().subtract(oldPointCoord).add(oldPointCoord.getX(), newValue.doubleValue());
        });

        newPointCoord.addListener((observable, oldValue, newValue) -> {
            newPoint.setX(newValue.getX());
            newPoint.setY(newValue.getY());
        });

        // TODO: calculate index (calculate between which points the new point should be added)
        //        pathPoints.add(index, newPointCoord);
    }
}
