package org.gecko.viewmodel;

import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import lombok.Data;
import org.gecko.actions.ActionManager;
import org.gecko.model.Region;
import org.gecko.tools.CursorTool;
import org.gecko.tools.EdgeCreatorTool;
import org.gecko.tools.MarqueeTool;
import org.gecko.tools.PanTool;
import org.gecko.tools.RegionCreatorTool;
import org.gecko.tools.StateCreatorTool;
import org.gecko.tools.SystemConnectionCreatorTool;
import org.gecko.tools.SystemCreatorTool;
import org.gecko.tools.Tool;
import org.gecko.tools.VariableBlockCreatorTool;
import org.gecko.tools.ZoomTool;

@Data
public class EditorViewModel {
    private static final int ROUND_SCALE = 10;

    private final ActionManager actionManager;
    private final SystemViewModel currentSystem;
    private final SystemViewModel parentSystem;
    private final ObservableSet<PositionableViewModelElement<?>> containedPositionableViewModelElementsProperty;
    private final List<List<Tool>> tools;
    private final SelectionManager selectionManager;

    private final Property<Point2D> pivotProperty;
    private final Property<Double> zoomScaleProperty;
    private final Property<Tool> currentToolProperty;
    private final Property<PositionableViewModelElement<?>> focusedElementProperty;
    private final boolean isAutomatonEditor;

    public EditorViewModel(ActionManager actionManager, SystemViewModel systemViewModel, SystemViewModel parentSystem, boolean isAutomatonEditor) {
        this.actionManager = actionManager;
        this.currentSystem = systemViewModel;
        this.parentSystem = parentSystem;
        this.containedPositionableViewModelElementsProperty = FXCollections.observableSet();
        this.isAutomatonEditor = isAutomatonEditor;
        this.tools = FXCollections.observableArrayList();
        this.selectionManager = new SelectionManager();
        this.pivotProperty = new SimpleObjectProperty<>();
        this.zoomScaleProperty = new SimpleObjectProperty<>(1.0);
        this.currentToolProperty = new SimpleObjectProperty<>();
        this.focusedElementProperty = new SimpleObjectProperty<>();
        initializeTools();

        selectionManager.getCurrentSelection().addListener((ListChangeListener<PositionableViewModelElement<?>>) change -> {
            if (change.getList().size() == 1) {
                setFocusedElement(change.getList().getFirst());
            } else {
                setFocusedElement(null);
            }
        });
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
        zoomScaleProperty.setValue(Math.round(zoomScale * ROUND_SCALE) / (double) ROUND_SCALE);
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

    private void setFocusedElement(PositionableViewModelElement<?> focusedElement) {
        focusedElementProperty.setValue(focusedElement);
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
        elements.forEach(containedPositionableViewModelElementsProperty::remove);
    }

    private void initializeTools() {
        tools.add(List.of(new CursorTool(actionManager), new MarqueeTool(actionManager), new PanTool(actionManager), new ZoomTool(actionManager)));
        if (isAutomatonEditor()) {
            tools.add(List.of(new StateCreatorTool(actionManager), new EdgeCreatorTool(actionManager), new RegionCreatorTool(actionManager)));
        } else {
            tools.add(
                List.of(new SystemCreatorTool(actionManager), new SystemConnectionCreatorTool(actionManager), new VariableBlockCreatorTool(actionManager)));
        }
    }
}
