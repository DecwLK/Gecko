package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.gecko.tools.Tool;

import java.util.List;

@Getter @Setter
public abstract class EditorViewModel {
    private Property<Point2D> pivot;
    private Property<Double> zoomScale;
    private ObservableObjectValue<Tool> currentTool;
    private ObservableObjectValue<PositionableViewModelElement<?>> focusedElement;
    private final SystemViewModel currentSystem;
    private final ObservableList<PositionableViewModelElement<?>> containedPositionableViewModelElements;
    private List<Tool> tools;
    private SelectionManager selectionManager;

    protected EditorViewModel(SystemViewModel systemViewModel) {
        currentSystem = systemViewModel;
        containedPositionableViewModelElements = FXCollections.observableArrayList();
        //TODO stub
    }

    public List<RegionViewModel> getRegionViewModels(StateViewModel stateViewModel) {
        //TODO stub
        return null;
    }

}
