package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

public interface ConnectionViewModel {
    void setEdgePoint(int index, Point2D point);

    ObservableList<Property<Point2D>> getEdgePoints();
}
