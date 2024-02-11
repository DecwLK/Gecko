package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

public interface ConnectionViewModel {
    /**
     * Sets the position of the edge newPosition at the given index.
     *
     * @param index       the index of the edge newPosition
     * @param newPosition the new position of the edge newPosition
     */
    void setEdgePoint(int index, Point2D newPosition);

    ObservableList<Property<Point2D>> getEdgePoints();
}
