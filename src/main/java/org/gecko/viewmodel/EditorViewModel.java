package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
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

/**
 * Represents the view model correspondent to an {@link org.gecko.view.views.EditorView EditorView}, holding relevant
 * items like the {@link ActionManager}, the current- and parent-{@link SystemViewModel SystemsViewModel}s, the
 * contained {@link PositionableViewModelElement}s, the {@link SelectionManager} and others, updating the view model of
 * the Gecko project.
 */
@Data
public class EditorViewModel {
    private static final double MAX_ZOOM_SCALE = 2.5;
    private static final double MIN_ZOOM_SCALE = 0.1;
    private final int id;
    private final ActionManager actionManager;
    private final SystemViewModel currentSystem;
    private final SystemViewModel parentSystem;
    private final ObservableSet<PositionableViewModelElement<?>> containedPositionableViewModelElementsProperty;
    private final List<List<Tool>> tools;
    private final SelectionManager selectionManager;

    private final Property<Point2D> pivotProperty;
    private final DoubleProperty zoomScaleProperty;
    private final BooleanProperty needsRefocusProperty;


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
        this.currentToolProperty = new SimpleObjectProperty<>();
        this.focusedElementProperty = new SimpleObjectProperty<>();
        this.pivotProperty = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.zoomScaleProperty = new SimpleDoubleProperty(1);
        this.needsRefocusProperty = new SimpleBooleanProperty(false);
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

    /**
     * Updates the regions of the automaton that is displayed by this {@link EditorViewModel}. This works by checking if
     * a state of the automaton is in a region and updating the regions accordingly.
     *
     * @throws ModelException if the update of the regions fails
     */
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

    /**
     * Returns the {@link RegionViewModel}s that contain the given {@link StateViewModel} by checking if the state is in
     * set of states of the region.
     *
     * @param stateViewModel the {@link StateViewModel} to get the containing {@link RegionViewModel}s for
     * @return the {@link RegionViewModel}s that contain the given {@link StateViewModel}
     */
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

    /**
     * Adds the given elements to the current {@link EditorViewModel}. They will then be displayed in the view.
     *
     * @param elements the elements to add
     */
    public void addPositionableViewModelElements(Set<PositionableViewModelElement<?>> elements) {
        elements.removeAll(containedPositionableViewModelElementsProperty);
        containedPositionableViewModelElementsProperty.addAll(elements);
    }

    /**
     * Removes the given elements from the current {@link EditorViewModel}. They will then no longer be displayed in the
     * view.
     *
     * @param elements the elements to remove
     */
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

    public void moveToFocusedElement() {
        if (focusedElementProperty.getValue() != null) {
            setPivot(focusedElementProperty.getValue().getCenter());
        }
    }

    public Point2D getPivot() {
        return pivotProperty.getValue();
    }

    public void setPivot(Point2D pivot) {
        pivotProperty.setValue(pivot);
        needsRefocus();
    }

    public void updatePivot(Point2D pivot) {
        pivotProperty.setValue(pivot);
    }

    public void needsRefocus() {
        needsRefocusProperty.set(true);
    }

    public double getZoomScale() {
        return zoomScaleProperty.get();
    }

    public void zoom(double factor, Point2D pivot) {
        if (factor < 0) {
            throw new IllegalArgumentException("Zoom factor must be positive");
        }
        double oldScale = zoomScaleProperty.get();
        zoomScaleProperty.set((Math.clamp(zoomScaleProperty.get() * factor, MIN_ZOOM_SCALE, MAX_ZOOM_SCALE)));
        setPivot(getPivot().add(pivot.subtract(getPivot()).multiply(zoomScaleProperty.get() / oldScale - 1)));
    }

    public void zoomCenter(double factor) {
        zoom(factor, pivotProperty.getValue());
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

    /**
     * Returns the elements that are in the given area.
     *
     * @param bound the area in world coordinates
     * @return the elements that are in the given area
     */
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
        if (!(o instanceof EditorViewModel editorViewModel)) {
            return false;
        }
        return id == editorViewModel.id;
    }
}
