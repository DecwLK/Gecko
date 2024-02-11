package org.gecko.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point2D;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.model.Automaton;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.Element;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
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
 * Performs operations for every {@link org.gecko.model.Element Model-Element} from the subtree of a {@link System},
 * creating for each of them a {@link org.gecko.viewmodel.AbstractViewModelElement ViewModel-Element}, depending on the
 * attributes of the corresponding {@link ViewModelPropertiesContainer}.
 */
public class ViewModelElementCreator {
    @Getter
    private int highestId;
    private final GeckoViewModel viewModel;
    private final ViewModelFactory viewModelFactory;
    private final HashMap<Integer, ViewModelPropertiesContainer> viewModelProperties;

    ViewModelElementCreator(
        GeckoViewModel viewModel, List<ViewModelPropertiesContainer> viewModelProperties) {
        highestId = 0;
        this.viewModel = viewModel;
        this.viewModelFactory = viewModel.getViewModelFactory();
        this.viewModelProperties = new HashMap<>();
        for (ViewModelPropertiesContainer container : viewModelProperties) {
            this.viewModelProperties.put(container.getElementId(), container);
        }
    }

    private static void setPositionAndSize(
        PositionableViewModelElement<?> element, ViewModelPropertiesContainer container) {
        if (container != null) {
            element.setPosition(new Point2D(container.getPositionX(), container.getPositionY()));
            element.setSize(new Point2D(container.getSizeX(), container.getSizeY()));
        }
    }

     /**
     * Traverses the model that is beneath the given system and creates the corresponding view model elements.
     *
     * @param system the system to traverse
     */
    protected void traverseModel(System system) throws IOException {
        for (Variable variable : system.getVariables()) {
            createPortViewModel(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            createSystemConnectionViewModel(systemConnection);
        }

        Automaton automaton = system.getAutomaton();

        for (State state : automaton.getStates()) {
            createStateViewModel(state);
            for (Contract contract : state.getContracts()) {
                createContractViewModel(contract);
            }
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

    private void createPortViewModel(Variable variable) throws IOException {
        PortViewModel portViewModel = viewModelFactory.createPortViewModelFrom(variable);
        ViewModelPropertiesContainer container = viewModelProperties.get(variable.getId());

        if (container == null) {
            throw new IOException("Cannot create port view model for variable " + variable.getId());
        }
        setPositionAndSize(portViewModel, container);
        updateHighestId(variable);
    }

    private void createStateViewModel(State state) throws IOException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
        ViewModelPropertiesContainer container = viewModelProperties.get(state.getId());

        if (container == null) {
            throw new IOException("Cannot create state view model for state " + state.getId());
        }
        setPositionAndSize(stateViewModel, container);
        updateHighestId(state);
    }

    private void createContractViewModel(Contract contract) throws IOException {
        viewModelFactory.createContractViewModelFrom(contract);
        updateHighestId(contract);
    }

    private void createRegionViewModel(Region region) throws IOException {
        RegionViewModel regionViewModel = null;
        try {
            regionViewModel = viewModelFactory.createRegionViewModelFrom(region);
        } catch (MissingViewModelElementException e) {
            for (State state : region.getStates()) {
                StateViewModel stateViewModel = (StateViewModel) viewModel.getViewModelElement(state);
                if (stateViewModel == null) {
                    createStateViewModel(state);
                }
            }
            createRegionViewModel(region);
        }

        if (regionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(region.getId());
            if (container == null) {
                throw new IOException("Cannot create region view model for region " + region.getId());
            }
            setPositionAndSize(regionViewModel, container);
            updateHighestId(region);
        }
    }

    private void createEdgeViewModel(Edge edge) throws IOException {
        EdgeViewModel edgeViewModel = null;
        try {
            edgeViewModel = viewModelFactory.createEdgeViewModelFrom(edge);
        } catch (MissingViewModelElementException e) {
            StateViewModel source = (StateViewModel) viewModel.getViewModelElement(edge.getSource());
            if (source == null) {
                viewModelFactory.createStateViewModelFrom(edge.getSource());
            }
            StateViewModel destination = (StateViewModel) viewModel.getViewModelElement(edge.getDestination());
            if (destination == null) {
                viewModelFactory.createStateViewModelFrom(edge.getDestination());
            }
            createEdgeViewModel(edge);
        }

        if (edgeViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(edge.getId());

            if (container == null) {
                throw new IOException("Cannot create edge view model for edge " + edge.getId());
            }
            setPositionAndSize(edgeViewModel, container);
            updateHighestId(edge);
        }
    }

    private void createSystemViewModel(System system) throws IOException {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelFrom(system);
        ViewModelPropertiesContainer container = viewModelProperties.get(system.getId());
        if (container == null) {
            throw new IOException("Cannot create system view model for system" + system.getId());
        }
        setPositionAndSize(systemViewModel, container);
        updateHighestId(system);
    }

    private void createSystemConnectionViewModel(SystemConnection systemConnection) throws IOException {
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel = viewModelFactory.createSystemConnectionViewModelFrom(systemConnection);
        } catch (MissingViewModelElementException e) {
            PortViewModel source = (PortViewModel) viewModel.getViewModelElement(systemConnection.getSource());
            if (source == null) {
                createPortViewModel(systemConnection.getSource());
            }
            PortViewModel destination =
                (PortViewModel) viewModel.getViewModelElement(systemConnection.getDestination());
            if (destination == null) {
                createPortViewModel(systemConnection.getDestination());
            }
            createSystemConnectionViewModel(systemConnection);
        }

        if (systemConnectionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(systemConnection.getId());
            if (container == null) {
                throw new IOException("Cannot create system connection view model for system " + "connection "
                    + systemConnection.getId());
            }
            setPositionAndSize(systemConnectionViewModel, container);
            updateHighestId(systemConnection);
        }
    }

    private void updateHighestId(Element element) throws IOException {
        if (element.getId() < 0) {
            throw new IOException("Negative IDs are not allowed.");
        }
        if (element.getId() > highestId) {
            highestId = element.getId();
        }
    }
}
