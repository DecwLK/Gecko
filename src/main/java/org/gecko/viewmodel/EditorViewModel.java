package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import lombok.Data;
import org.gecko.actions.ActionManager;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Automaton;
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
import org.gecko.tools.ToolType;
import org.gecko.tools.VariableBlockCreatorTool;
import org.gecko.tools.ZoomTool;
import org.gecko.view.views.ViewElementSearchVisitor;

@Data
public class EditorViewModel {
    private static final double MAX_ZOOM_SCALE = 5;
    private final int id;
    private final ActionManager actionManager;
    private final SystemViewModel currentSystem;
    private final SystemViewModel parentSystem;
    private final ObservableSet<PositionableViewModelElement<?>> containedPositionableViewModelElementsProperty;
    private final List<List<Tool>> tools;
    private final SelectionManager selectionManager;

    private final SimpleDoubleProperty zoomScaleProperty;
    private final Property<Point2D> viewPortPositionProperty;
    private final Property<Point2D> requestedViewPortPositionProperty;
    private final Property<Point2D> viewPortSizeProperty;
    private final Property<Point2D> worldSizeProperty;
    private final Property<Tool> currentToolProperty;
    private final Property<PositionableViewModelElement<?>> focusedElementProperty;
    private final boolean isAutomatonEditor;

    public EditorViewModel(
        ActionManager actionManager, SystemViewModel currentSystem, SystemViewModel parentSystem,
        boolean isAutomatonEditor, int id) {
        this.actionManager = actionManager;
        this.currentSystem = currentSystem;
        this.parentSystem = parentSystem;
        this.containedPositionableViewModelElementsProperty = FXCollections.observableSet();
        this.isAutomatonEditor = isAutomatonEditor;
        this.id = id;
        this.tools = FXCollections.observableArrayList();
        this.selectionManager = new SelectionManager();
        this.zoomScaleProperty = new SimpleDoubleProperty(1);
        this.currentToolProperty = new SimpleObjectProperty<>();
        this.focusedElementProperty = new SimpleObjectProperty<>();
        this.viewPortPositionProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.requestedViewPortPositionProperty = new SimpleObjectProperty<>();
        this.viewPortSizeProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.worldSizeProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        initializeTools();

        selectionManager.getCurrentSelectionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.size() == 1) {
                setFocusedElement(newValue.iterator().next());
            } else {
                setFocusedElement(null);
            }
        });

        setCurrentTool(ToolType.CURSOR);
    }

    public void updateRegions() throws ModelException {
        Automaton automaton = getCurrentSystem().getTarget().getAutomaton();
        Set<RegionViewModel> regionViewModels = containedPositionableViewModelElementsProperty.stream()
            .filter(element -> automaton.getRegions().contains(element.getTarget()))
            .map(element -> (RegionViewModel) element)
            .collect(Collectors.toSet());
        Set<StateViewModel> stateViewModels = containedPositionableViewModelElementsProperty.stream()
            .filter(element -> automaton.getStates().contains(element.getTarget()))
            .map(element -> (StateViewModel) element)
            .collect(Collectors.toSet());
        for (RegionViewModel regionViewModel : regionViewModels) {
            for (StateViewModel stateViewModel : stateViewModels) {
                regionViewModel.checkStateInRegion(stateViewModel);
                regionViewModel.updateTarget();
            }
        }
    }

    public ObservableList<RegionViewModel> getRegionViewModels(StateViewModel stateViewModel) {
        ObservableList<RegionViewModel> regionViewModels = FXCollections.observableArrayList();
        Set<Region> regions = currentSystem.getTarget().getAutomaton().getRegions();
        List<Region> containingStateRegions =
            regions.stream().filter(region -> region.getStates().contains(stateViewModel.getTarget())).toList();
        List<RegionViewModel> containedRegionViewModels = containedPositionableViewModelElementsProperty.stream()
            .filter(element -> containingStateRegions.contains(element.getTarget()))
            .map(element -> (RegionViewModel) element)
            .toList();
        regionViewModels.addAll(containedRegionViewModels);

        for (RegionViewModel region : containedPositionableViewModelElementsProperty.stream()
            .filter(element -> regions.contains(element.getTarget()))
            .map(element -> (RegionViewModel) element)
            .toList()) {
            region.getStatesProperty().addListener((ListChangeListener<StateViewModel>) change -> {
                updateStateRegionList(stateViewModel, region, change, regionViewModels);
            });
        }

        return regionViewModels;
    }

    private static void updateStateRegionList(
        StateViewModel stateViewModel, RegionViewModel region,
        ListChangeListener.Change<? extends StateViewModel> change, ObservableList<RegionViewModel> regionViewModels) {
        while (change.next()) {
            if (change.wasAdded()) {
                if (change.getAddedSubList().contains(stateViewModel)) {
                    regionViewModels.add(region);
                }
            }
            if (change.wasRemoved()) {
                if (change.getRemoved().contains(stateViewModel)) {
                    regionViewModels.remove(region);
                }
            }
        }
    }

    public Point2D transformScreenToWorldCoordinates(Point2D screenCoordinates) {
        return screenCoordinates.multiply(1 / getZoomScale()).add(viewPortPositionProperty.getValue());
    }

    public void moveToFocusedElement() {
        if (focusedElementProperty.getValue() != null) {
            focusWorldPoint(focusedElementProperty.getValue().getCenter());
        }
    }

    private void focusWorldPoint(Point2D point) {
        requestedViewPortPositionProperty.setValue(point.subtract(getViewPortSize().multiply(0.5)));
    }

    public void zoom(Point2D pivot, double zoomFactor) {
        if (!isZoomAllowed(zoomFactor)) {
            return;
        }
        zoomScaleProperty.set(getZoomScale() * zoomFactor);
        pivot = pivot.multiply(1 / getZoomScale());
        Point2D viewPortPosition = viewPortPositionProperty.getValue();
        double newX = pivot.getX() * (zoomFactor - 1) + zoomFactor * viewPortPosition.getX();
        double newY = pivot.getY() * (zoomFactor - 1) + zoomFactor * viewPortPosition.getY();
        requestedViewPortPositionProperty.setValue(new Point2D(newX, newY));
    }

    public void zoomCenter(double zoomFactor) {
        zoom(getCenterOfViewPort(), zoomFactor);
    }

    private Point2D getCenterOfViewPort() {
        return new Point2D(viewPortSizeProperty.getValue().getX() / 2, viewPortSizeProperty.getValue().getY() / 2);
    }

    public ToolType getCurrentToolType() {
        return currentToolProperty.getValue().getToolType();
    }

    public Tool getCurrentTool() {
        return currentToolProperty.getValue();
    }

    public void setCurrentTool(ToolType currentToolType) {
        Tool tool = getTool(currentToolType);
        if (tool != null) {
            currentToolProperty.setValue(tool);
        }
    }

    private Tool getTool(ToolType toolType) {
        return tools.stream()
            .flatMap(List::stream)
            .filter(tool -> tool.getToolType() == toolType)
            .findFirst()
            .orElse(null);
    }

    public PositionableViewModelElement<?> getFocusedElement() {
        return focusedElementProperty.getValue();
    }

    private void setFocusedElement(PositionableViewModelElement<?> focusedElement) {
        focusedElementProperty.setValue(focusedElement);
    }

    public void addPositionableViewModelElement(PositionableViewModelElement<?> element) {
        addPositionableViewModelElements(Set.of(element));
    }

    public void addPositionableViewModelElements(Set<PositionableViewModelElement<?>> elements) {
        elements.removeAll(containedPositionableViewModelElementsProperty);
        containedPositionableViewModelElementsProperty.addAll(elements);
        //don't select elements if no elements are added to the current view
        /*if (!elements.isEmpty()) {
            actionManager.run(actionManager.getActionFactory().createSelectAction(elements, true));
        }*/
    }

    public void removePositionableViewModelElement(PositionableViewModelElement<?> element) {
        containedPositionableViewModelElementsProperty.remove(element);
    }

    public void removePositionableViewModelElements(Set<PositionableViewModelElement<?>> elements) {
        elements.forEach(containedPositionableViewModelElementsProperty::remove);
    }

    public Set<PositionableViewModelElement<?>> getPositionableViewModelElements() {
        return containedPositionableViewModelElementsProperty;
    }

    private void initializeTools() {
        tools.add(List.of(new CursorTool(actionManager, selectionManager, this), new MarqueeTool(actionManager, this),
            new PanTool(actionManager), new ZoomTool(actionManager)));
        if (isAutomatonEditor()) {
            tools.add(List.of(new StateCreatorTool(actionManager), new EdgeCreatorTool(actionManager),
                new RegionCreatorTool(actionManager)));
        } else {
            tools.add(List.of(new SystemCreatorTool(actionManager), new SystemConnectionCreatorTool(actionManager),
                new VariableBlockCreatorTool(actionManager)));
        }
    }

    public double getZoomScale() {
        return zoomScaleProperty.get();
    }

    public Point2D getViewPortSize() {
        return viewPortSizeProperty.getValue();
    }

    public Point2D getWorldSize() {
        return worldSizeProperty.getValue();
    }

    private boolean isZoomAllowed(double zoomFactor) {
        if (zoomFactor <= 0) {
            return false;
        }
        if (zoomFactor < 1) {
            return getViewPortSize().getX() / getZoomScale() <= getWorldSize().getX() * zoomFactor
                && getViewPortSize().getY() / getZoomScale() <= getWorldSize().getY() * zoomFactor;
        } else {
            return getZoomScale() * zoomFactor <= MAX_ZOOM_SCALE;
        }
    }

    public List<PositionableViewModelElement<?>> getElementsByName(String name) {
        List<PositionableViewModelElement<?>> matches = new ArrayList<>();
        PositionableViewModelElementVisitor visitor = new ViewElementSearchVisitor(name);
        containedPositionableViewModelElementsProperty.forEach(element -> {
            PositionableViewModelElement<?> searchResult = (PositionableViewModelElement<?>) element.accept(visitor);
            if (searchResult != null) {
                matches.add(searchResult);
            }
        });
        return matches;
    }

    public Set<PositionableViewModelElement<?>> getElementsInArea(Bounds bound) {
        return containedPositionableViewModelElementsProperty.stream().filter(element -> {
            Bounds elementBound =
                new BoundingBox(element.getPosition().getX(), element.getPosition().getY(), element.getSize().getX(),
                    element.getSize().getY());
            return bound.intersects(elementBound);
        }).collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EditorViewModel editorViewModel = (EditorViewModel) o;
        return id == editorViewModel.id;
    }
}
