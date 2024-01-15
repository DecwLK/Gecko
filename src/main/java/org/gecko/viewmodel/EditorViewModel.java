package org.gecko.viewmodel;

import java.util.List;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import lombok.Data;
import org.gecko.model.Region;
import org.gecko.tools.CursorTool;
import org.gecko.tools.EdgeCreatorTool;
import org.gecko.tools.MarqueeTool;
import org.gecko.tools.PanTool;
import org.gecko.tools.RegionCreatorTool;
import org.gecko.tools.StateCreatorTool;
import org.gecko.tools.SystemConnectionTool;
import org.gecko.tools.SystemCreatorTool;
import org.gecko.tools.Tool;
import org.gecko.tools.VariableBlockCreatorTool;
import org.gecko.tools.ZoomTool;

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
        initializeTools();
    }

    public List<RegionViewModel> getRegionViewModels(StateViewModel stateViewModel) {
        List<Region> regions = currentSystem.getTarget()
                                            .getAutomaton()
                                            .getRegions()
                                            .stream()
                                            .filter(region -> region.getStates().contains(stateViewModel.getTarget()))
                                            .toList();
        return containedPositionableViewModelElementsProperty.stream()
                                                             .filter(element -> regions.contains(element.getTarget()))
                                                             .map(element -> (RegionViewModel) element)
                                                             .toList();
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

    public void addPositionableViewModelElement(PositionableViewModelElement<?> element) {
        containedPositionableViewModelElementsProperty.add(element);
    }

    public void addPositionableViewModelElements(List<PositionableViewModelElement<?>> elements) {
        containedPositionableViewModelElementsProperty.addAll(elements);
    }

    public void removePositionableViewModelElement(PositionableViewModelElement<?> element) {
        containedPositionableViewModelElementsProperty.remove(element);
    }

    public void removePositionableViewModelElements(List<PositionableViewModelElement<?>> elements) {
        containedPositionableViewModelElementsProperty.removeAll(elements);
    }

    private void initializeTools() {
        tools.add(List.of(new CursorTool(), new MarqueeTool(), new PanTool(), new ZoomTool()));
        if (isAutomatonEditor()) {
            tools.add(List.of(new StateCreatorTool(), new EdgeCreatorTool(), new RegionCreatorTool()));
        } else {
            tools.add(List.of(new SystemCreatorTool(), new SystemConnectionTool(), new VariableBlockCreatorTool()));
        }
    }
}
