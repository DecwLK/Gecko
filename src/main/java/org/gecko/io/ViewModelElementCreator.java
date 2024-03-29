package org.gecko.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
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
    @Getter
    private boolean foundNullContainer;
    @Getter
    private boolean foundNonexistentStartState;
    private final GeckoViewModel viewModel;
    private final ViewModelFactory viewModelFactory;
    private final HashMap<Integer, ViewModelPropertiesContainer> viewModelProperties;
    private final HashMap<Integer, StartStateContainer> startStates;

    ViewModelElementCreator(
        GeckoViewModel viewModel, List<ViewModelPropertiesContainer> viewModelProperties,
        List<StartStateContainer> startStates) {
        highestId = 0;
        foundNullContainer = false;
        foundNonexistentStartState = false;
        this.viewModel = viewModel;
        this.viewModelFactory = viewModel.getViewModelFactory();
        this.viewModelProperties = new HashMap<>();
        this.startStates = new HashMap<>();

        for (ViewModelPropertiesContainer container : viewModelProperties) {
            this.viewModelProperties.put(container.getElementId(), container);
        }
        for (StartStateContainer container : startStates) {
            this.startStates.put(container.getSystemId(), container);
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
     * @throws IOException if a view model element cannot be created or no correspondent property container was found
     */
    protected void traverseModel(System system) throws IOException {
        for (Variable variable : system.getVariables()) {
            createPortViewModel(variable);
        }

        for (SystemConnection systemConnection : system.getConnections()) {
            createSystemConnectionViewModel(system, systemConnection);
        }

        Automaton automaton = system.getAutomaton();
        if (startStates.get(system.getId()) != null) {
            State startState = automaton.getStateByName(startStates.get(system.getId()).getStartStateName());
            if (automaton.getStates().isEmpty() || startState == null) {
                foundNonexistentStartState = true;
            } else {
                try {
                    automaton.setStartState(startState);
                } catch (ModelException e) {
                    foundNonexistentStartState = true;
                }
            }
        }

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
            foundNullContainer = true;
        }
        setPositionAndSize(portViewModel, container);
        updateHighestId(variable);
    }

    private void createStateViewModel(State state) throws IOException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelFrom(state);
        ViewModelPropertiesContainer container = viewModelProperties.get(state.getId());

        if (container == null) {
            foundNullContainer = true;
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
                foundNullContainer = true;
            } else {
                setPositionAndSize(regionViewModel, container);
                regionViewModel.setColor(Color.color(container.getRed(), container.getGreen(), container.getBlue()));
            }

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
                foundNullContainer = true;
            }
            setPositionAndSize(edgeViewModel, container);
            updateHighestId(edge);
        }
    }

    private void createSystemViewModel(System system) throws IOException {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelFrom(system);
        ViewModelPropertiesContainer container = viewModelProperties.get(system.getId());
        if (container == null) {
            foundNullContainer = true;
        }
        setPositionAndSize(systemViewModel, container);
        updateHighestId(system);
    }

    private void createSystemConnectionViewModel(System system, SystemConnection systemConnection) throws IOException {
        SystemConnectionViewModel systemConnectionViewModel = null;
        try {
            systemConnectionViewModel = viewModelFactory.createSystemConnectionViewModelFrom(system, systemConnection);
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
            createSystemConnectionViewModel(system, systemConnection);
        }

        if (systemConnectionViewModel != null) {
            ViewModelPropertiesContainer container = viewModelProperties.get(systemConnection.getId());
            if (container == null) {
                foundNullContainer = true;
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
