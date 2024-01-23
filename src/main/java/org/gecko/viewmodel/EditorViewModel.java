package org.gecko.viewmodel;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import javafx.scene.transform.Scale;
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
    private static final double ZOOM_FACTOR = 1.1;

    private final ActionManager actionManager;
    private final SystemViewModel currentSystem;
    private final SystemViewModel parentSystem;
    private final ObservableSet<PositionableViewModelElement<?>> containedPositionableViewModelElementsProperty;
    private final List<List<Tool>> tools;
    private final SelectionManager selectionManager;

    private final DoubleProperty hValueProperty;
    private final DoubleProperty vValueProperty;
    //private final Property<Double> zoomScaleProperty;
    private double zoomScale = 1.0;
    private final Property<Scale> scaleProperty;
    private final Property<Point2D> viewSizeProperty;
    private final Property<Point2D> worldSizeProperty;
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
        this.hValueProperty = new SimpleDoubleProperty(0.0);
        this.vValueProperty = new SimpleDoubleProperty(0.0);
        this.viewSizeProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.worldSizeProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.scaleProperty = new SimpleObjectProperty<>(new Scale(zoomScale, zoomScale));
        //this.zoomScaleProperty = new SimpleObjectProperty<>(1.0);
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

    public Point2D transformScreenToWorldCoordinates(Point2D screenCoordinates) {
        return screenCoordinates.multiply(1 / zoomScale).add(getMinPoint());
    }

    public void moveToFocusedElement() {
        if (focusedElementProperty.getValue() != null) {
            focusWorldPoint(focusedElementProperty.getValue().getCenter());
        }
    }

    private void focusWorldPoint(Point2D point) {
        Point2D center = new Point2D(viewSizeProperty.getValue().getX() / 2, viewSizeProperty.getValue().getY() / 2).multiply(1 / zoomScale);
        Point2D delta = point.subtract(center);
        Point2D scrollRange = new Point2D(worldSizeProperty.getValue().getX() - viewSizeProperty.getValue().getX(),
            worldSizeProperty.getValue().getY() - viewSizeProperty.getValue().getY()).multiply(1 / zoomScale);
        Point2D scrollPosition = new Point2D(delta.getX() / scrollRange.getX(), delta.getY() / scrollRange.getY()).add(hValueProperty.getValue(),
            vValueProperty.getValue());
        hValueProperty.setValue(scrollPosition.getX());
        vValueProperty.setValue(scrollPosition.getY());
    }

    private Point2D getMinPoint() {
        return new Point2D((worldSizeProperty.getValue().getX() - viewSizeProperty.getValue().getX()) * hValueProperty.getValue(),
            (worldSizeProperty.getValue().getY() - viewSizeProperty.getValue().getY()) * vValueProperty.getValue()).multiply(1 / zoomScale);
    }

    public void zoomIn(Point2D pivot, double zoomScaleAdditive) {
        zoomScale = Math.round(zoomScale * ZOOM_FACTOR * ROUND_SCALE) / (double) ROUND_SCALE;
        Scale newScale = new Scale(zoomScale, zoomScale, 0, 0);
        scaleProperty.setValue(newScale);
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
            tools.add(List.of(new SystemCreatorTool(actionManager), new SystemConnectionCreatorTool(actionManager),
                new VariableBlockCreatorTool(actionManager)));
        }
    }
}
