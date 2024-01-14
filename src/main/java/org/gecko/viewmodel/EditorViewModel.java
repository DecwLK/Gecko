package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Data;
import org.gecko.tools.Tool;

import java.util.List;

@Data
public class EditorViewModel {
    private final SystemViewModel currentSystem;
    private boolean isAutomatonEditor;
    private Property<Point2D> pivotProperty;
    private Property<Double> zoomScaleProperty;
    private Property<Tool> currentToolProperty;
    private Property<PositionableViewModelElement<?>> focusedElementProperty;
    private final ObservableList<PositionableViewModelElement<?>> containedPositionableViewModelElementsProperty;
    private final List<List<Tool>> tools;
    private final SelectionManager selectionManager;

    public EditorViewModel(SystemViewModel systemViewModel, boolean isAutomatonEditor) {
        currentSystem = systemViewModel;
        containedPositionableViewModelElementsProperty = FXCollections.observableArrayList();
        this.isAutomatonEditor = isAutomatonEditor;
        tools = FXCollections.observableArrayList();
        selectionManager = new SelectionManager();
    }

    public List<RegionViewModel> getRegionViewModels(StateViewModel stateViewModel) {
        //TODO stub
        return null;
    }

    public void moveToFocusedElement() {
        //TODO stub
    }

    public Point2D getPivot() {
        return pivotProperty.getValue();
    }

    public void setPivot(Point2D pivot) {
        pivotProperty.setValue(pivot);
    }

    public double getZoomScale() {
        return zoomScaleProperty.getValue();
    }

    public void setZoomScale(double zoomScale) {
        zoomScaleProperty.setValue(zoomScale);
    }

    public Tool getCurrentTool() {
        return currentToolProperty.getValue();
    }

    public void setCurrentTool(Tool currentTool) {
        currentToolProperty.setValue(currentTool);
    }

    public PositionableViewModelElement<?> getFocusedElement() {
        return focusedElementProperty.getValue();
    }

    public void setFocusedElement(PositionableViewModelElement<?> focusedElement) {
        focusedElementProperty.setValue(focusedElement);
    }

}
