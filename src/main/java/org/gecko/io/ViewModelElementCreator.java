package org.gecko.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.model.Automaton;
import org.gecko.model.Edge;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;

/**
 * Performs operations for every {@link org.gecko.model.Element Model-Element} from the subtree of a
 * {@link System}, creating for each of them a {@link org.gecko.viewmodel.AbstractViewModelElement ViewModel-Element},
 * depending on the attributes of the corresponding {@link ViewModelPropertiesContainer}.
 */
public class ViewModelElementCreator {
    @Getter
    private final List<PositionableViewModelElement<?>> generatedViewModelElements;
    private final GeckoViewModel viewModel;
    private final ViewModelFactory viewModelFactory;
    private final HashMap<Integer, ViewModelPropertiesContainer> viewModelProperties;

    ViewModelElementCreator(
        GeckoViewModel viewModel, List<ViewModelPropertiesContainer> viewModelProperties) {
        this.viewModel = viewModel;
        this.viewModelFactory = viewModel.getViewModelFactory();
        this.viewModelProperties = new HashMap<>();
        for (ViewModelPropertiesContainer container : viewModelProperties) {
            this.viewModelProperties.put(container.getElementId(), container);
        }
        this.generatedViewModelElements = new ArrayList<>();
    }

    private static void setPositionAndSize(
        PositionableViewModelElement<?> element, ViewModelPropertiesContainer container) {
        if (container != null) {
            element.setPosition(new Point2D(container.getPositionX(), container.getPositionY()));
            element.setSize(new Point2D(container.getSizeX(), container.getSizeY()));
        }
    }

    protected void traverseModel(System system) {
        for (SystemConnection systemConnection : system.getConnections()) {
            createSystemConnectionViewModel(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        for (State state : automaton.getStates()) {
            createStateViewModel(state);
        }

        for (Region region : automaton.getRegions()) {
            createRegionViewModel(region);
        }

        for (Edge edge : automaton.getEdges()) {
            createEdgeViewModel(edge);
        }

        for (System child : system.getChildren()) {
            createSystemViewModel(child);
            traverseModel(child);
        }
    }

    public void createStateViewModel(State state) {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
        ViewModelPropertiesContainer container = viewModelProperties.get(state.getId());
        if (container == null) {
            generatedViewModelElements.add(stateViewModel);
        } else {
            setPositionAndSize(stateViewModel, container);
        }
    }

    public void createSystemConnectionViewModel(SystemConnection systemConnection) {
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel = viewModelFactory.createSystemConnectionViewModelFrom(systemConnection);
        } catch (MissingViewModelElementException e) {
            PortViewModel source = (PortViewModel) viewModel.getViewModelElement(systemConnection.getSource());
            if (source == null) {
                source = viewModelFactory.createPortViewModelFrom(systemConnection.getSource());
            }
            PortViewModel destination =
                (PortViewModel) viewModel.getViewModelElement(systemConnection.getDestination());
            if (destination == null) {
                destination = viewModelFactory.createPortViewModelFrom(systemConnection.getDestination());
            }
            generatedViewModelElements.add(source);
            generatedViewModelElements.add(destination);
            createSystemConnectionViewModel(systemConnection);
        }

        if (systemConnectionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(systemConnection.getId());
            if (container == null) {
                generatedViewModelElements.add(systemConnectionViewModel);
            } else {
                setPositionAndSize(systemConnectionViewModel, container);
            }
        }
    }

    public void createSystemViewModel(System system) {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelFrom(system);
        ViewModelPropertiesContainer container = viewModelProperties.get(system.getId());
        if (container == null) {
            generatedViewModelElements.add(systemViewModel);
        } else {
            setPositionAndSize(systemViewModel, container);
        }
    }

    public void createRegionViewModel(Region region) {
        RegionViewModel regionViewModel = null;
        try {
            regionViewModel = viewModelFactory.createRegionViewModelFrom(region);
        } catch (MissingViewModelElementException e) {
            for (State state : region.getStates()) {
                StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
                generatedViewModelElements.add(stateViewModel);
            }
            createRegionViewModel(region);
        }

        if (regionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(region.getId());
            if (container == null) {
                generatedViewModelElements.add(regionViewModel);
            } else {
                setPositionAndSize(regionViewModel, container);
                regionViewModel.setColor(Color.color(container.getRed(), container.getGreen(), container.getBlue()));
            }
        }
    }

    public void createEdgeViewModel(Edge edge) {
        EdgeViewModel edgeViewModel = null;
        try {
            edgeViewModel = viewModelFactory.createEdgeViewModelFrom(edge);
        } catch (MissingViewModelElementException e) {
            StateViewModel source = viewModelFactory.createStateViewModelFrom(edge.getSource());
            StateViewModel destination = viewModelFactory.createStateViewModelFrom(edge.getDestination());
            generatedViewModelElements.add(source);
            generatedViewModelElements.add(destination);
            createEdgeViewModel(edge);
        }

        if (edgeViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(edge.getId());
            if (container == null) {
                generatedViewModelElements.add(edgeViewModel);
            } else {
                setPositionAndSize(edgeViewModel, container);
            }
        }
    }
}
