package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import org.gecko.actions.ActionManager;
import org.gecko.exceptions.MissingViewModelElementException;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

/**
 * Represents a factory for the view model elements of a Gecko project. Provides methods for the creation of each
 * element. The create[Element]ViewModelIn methods are used to create a new view model element and also new model
 * element. The create[Element]ViewModelFrom methods are used to create a new view model element from an existing model
 * element.
 */
public class ViewModelFactory {
    /**
     * The id of the next view model element to be created.
     */
    private int viewModelElementId = 0;
    private final ActionManager actionManager;
    private final ModelFactory modelFactory;
    private final GeckoViewModel geckoViewModel;

    public ViewModelFactory(ActionManager actionManager, GeckoViewModel geckoViewModel, ModelFactory modelFactory) {
        this.actionManager = actionManager;
        this.geckoViewModel = geckoViewModel;
        this.modelFactory = modelFactory;
    }

    public EditorViewModel createEditorViewModel(
        SystemViewModel systemViewModel, SystemViewModel parentSystem, boolean isAutomatonEditor) {
        return new EditorViewModel(actionManager, systemViewModel, parentSystem, isAutomatonEditor,
            getNewViewModelElementId());
    }

    /**
     * If no start state is set in the parent system, the new state will be set as the start state.
     */
    public StateViewModel createStateViewModelIn(SystemViewModel parentSystem) throws ModelException {
        State state = modelFactory.createState(parentSystem.getTarget().getAutomaton());
        StateViewModel result = new StateViewModel(getNewViewModelElementId(), state);
        geckoViewModel.addViewModelElement(result);

        if (parentSystem.getStartState() == null) {
            parentSystem.setStartState(result);
            parentSystem.updateTarget();
        }

        return result;
    }

    /**
     * New ContractViewModels are created for the contracts of the state. If the state is the start state of a system,
     * the system's start state is set to the new state.
     */
    public StateViewModel createStateViewModelFrom(State state) {
        StateViewModel result = new StateViewModel(getNewViewModelElementId(), state);
        for (Contract contract : state.getContracts()) {
            ContractViewModel contractViewModel = createContractViewModelFrom(contract);
            result.addContract(contractViewModel);
        }
        geckoViewModel.addViewModelElement(result);
        updateStartState(state);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(
        SystemViewModel parentSystem, StateViewModel source, StateViewModel destination) throws ModelException {
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(),
            destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    /**
     * Expects the source and destination of the edge to be in the view model.
     */
    public EdgeViewModel createEdgeViewModelFrom(Edge edge) throws MissingViewModelElementException {
        StateViewModel source = (StateViewModel) geckoViewModel.getViewModelElement(edge.getSource());
        StateViewModel destination = (StateViewModel) geckoViewModel.getViewModelElement(edge.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElementException("Missing source or destination for edge.");
        }
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        if (edge.getContract() != null) {
            //This should never be null because the Edge Model Element has a contract that should be coming
            //from its source
            ContractViewModel contract = source.getContractsProperty()
                .stream()
                .filter(contractViewModel -> contractViewModel.getTarget().equals(edge.getContract()))
                .findFirst()
                .orElse(null);
            result.setContract(contract);
        }
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelIn(
        SystemViewModel parentSystem, PortViewModel source, PortViewModel destination) throws ModelException {
        SystemConnection systemConnection =
            modelFactory.createSystemConnection(parentSystem.getTarget(), source.getTarget(), destination.getTarget());
        SystemConnectionViewModel result =
            new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
        geckoViewModel.addViewModelElement(result);

        setSystemConnectionEdgePoints(source, destination, result);
        result.updateTarget();
        return result;
    }

    /**
     * Expects the source and destination of the system connection to be in the view model.
     */
    public SystemConnectionViewModel createSystemConnectionViewModelFrom(SystemConnection systemConnection)
        throws MissingViewModelElementException {
        PortViewModel source = (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getSource());
        PortViewModel destination =
            (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElementException("Missing source or destination for system connection.");
        }
        SystemConnectionViewModel result =
            new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
        geckoViewModel.addViewModelElement(result);
        setSystemConnectionEdgePoints(source, destination, result);
        // Since target is already up to date and we're building from target, we don't need to call updateTarget
        return result;
    }

    public SystemViewModel createSystemViewModelIn(SystemViewModel parentSystem) throws ModelException {
        System system = modelFactory.createSystem(parentSystem.getTarget());
        SystemViewModel result = new SystemViewModel(getNewViewModelElementId(), system);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    /**
     * Missing PortViewModels are created for the variables of the system.
     */
    public SystemViewModel createSystemViewModelFrom(System system) {
        SystemViewModel result = new SystemViewModel(getNewViewModelElementId(), system);
        for (Variable variable : system.getVariables()) {
            PortViewModel portViewModel = (PortViewModel) geckoViewModel.getViewModelElement(variable);
            if (portViewModel == null) {
                portViewModel = createPortViewModelFrom(variable);
            }
            result.addPort(portViewModel);
        }
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public RegionViewModel createRegionViewModelIn(SystemViewModel parentSystem) throws ModelException {
        Region region = modelFactory.createRegion(parentSystem.getTarget().getAutomaton());
        RegionViewModel result = new RegionViewModel(getNewViewModelElementId(), region,
            createContractViewModelFrom(region.getPreAndPostCondition()));

        // Check for states in the region
        for (State state : parentSystem.getTarget().getAutomaton().getStates()) {
            result.checkStateInRegion((StateViewModel) geckoViewModel.getViewModelElement(state));
        }

        geckoViewModel.addViewModelElement(result);
        return result;
    }

    /**
     * Expects the states in the region to be in the view model.
     */
    public RegionViewModel createRegionViewModelFrom(Region region) throws MissingViewModelElementException {
        RegionViewModel result = new RegionViewModel(getNewViewModelElementId(), region,
            createContractViewModelFrom(region.getPreAndPostCondition()));
        for (State state : region.getStates()) {
            StateViewModel stateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(state);
            if (stateViewModel == null) {
                throw new MissingViewModelElementException("Region contains invalid state.");
            }
            result.addState(stateViewModel);
        }
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public PortViewModel createPortViewModelIn(SystemViewModel systemViewModel) throws ModelException {
        Variable variable = modelFactory.createVariable(systemViewModel.getTarget());
        PortViewModel result = new PortViewModel(getNewViewModelElementId(), variable);
        systemViewModel.addPort(result);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    /**
     * New PortViewModel is not added to the SystemViewModel.
     **/
    public PortViewModel createPortViewModelFrom(Variable variable) {
        PortViewModel result = new PortViewModel(getNewViewModelElementId(), variable);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public ContractViewModel createContractViewModelIn(StateViewModel stateViewModel) throws ModelException {
        Contract contract = modelFactory.createContract(stateViewModel.getTarget());
        ContractViewModel result = new ContractViewModel(getNewViewModelElementId(), contract);
        stateViewModel.addContract(result);
        return result;
    }

    /**
     * New ContractViewModel is not added to the StateViewModel.
     **/
    public ContractViewModel createContractViewModelFrom(Contract contract) {
        return new ContractViewModel(getNewViewModelElementId(), contract);
    }

    private int getNewViewModelElementId() {
        return viewModelElementId++;
    }

    private void updateStartState(State state) {
        System root = geckoViewModel.getGeckoModel().getRoot();
        System parentSystem = findSystemWithState(root, state);
        if (parentSystem != null && state.equals(parentSystem.getAutomaton().getStartState())) {
            SystemViewModel parentSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(parentSystem);
            parentSystemViewModel.setStartState((StateViewModel) geckoViewModel.getViewModelElement(state));
        }
    }

    private System findSystemWithState(System parentSystem, State state) {
        if (parentSystem.getAutomaton().getStates().contains(state)) {
            return parentSystem;
        }
        if (!parentSystem.getChildren().isEmpty()) {
            for (System childSystem : parentSystem.getChildren()) {
                System result = findSystemWithState(childSystem, state);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private Point2D calculateEndPortPosition(Point2D position, Point2D size, Visibility visibility, boolean isPort) {
        int sign = isPort ? 1 : -1;
        return position.add(size.multiply(0.5))
            .subtract((visibility == Visibility.INPUT ? 1 : -1) * sign * size.getX() / 2, 0);
    }

    private boolean isPort(PortViewModel portViewModel) {
        return geckoViewModel.getCurrentEditor()
            .getCurrentSystem()
            .getTarget()
            .getVariables()
            .stream()
            .filter(variable -> portViewModel.getTarget().equals(variable))
            .findFirst()
            .isEmpty();
    }

    private void setSystemConnectionEdgePoints(
        PortViewModel source, PortViewModel destination, SystemConnectionViewModel result) {
        boolean sourceIsPort = isPort(source);
        boolean destIsPort = isPort(destination);
        Property<Point2D> sourcePosition;

        // position the line at the tip of the port
        if (sourceIsPort) {
            sourcePosition = new SimpleObjectProperty<>(
                calculateEndPortPosition(source.getSystemPortPositionProperty().getValue(),
                    source.getSystemPortSizeProperty().getValue(), source.getVisibility(), true));

            source.getSystemPortPositionProperty()
                .addListener((observable, oldValue, newValue) -> sourcePosition.setValue(
                    calculateEndPortPosition(source.getSystemPortPositionProperty().getValue(),
                        source.getSystemPortSizeProperty().getValue(), source.getVisibility(), true)));
        } else {
            sourcePosition = new SimpleObjectProperty<>(
                calculateEndPortPosition(source.getPosition(), source.getSize(), source.getVisibility(), false));

            source.getPositionProperty().addListener((observable, oldValue, newValue) -> {
                sourcePosition.setValue(
                    calculateEndPortPosition(source.getPosition(), source.getSize(), source.getVisibility(), false));
            });
        }

        Property<Point2D> destinationPosition;

        if (destIsPort) {
            destinationPosition = new SimpleObjectProperty<>(
                calculateEndPortPosition(destination.getSystemPortPositionProperty().getValue(),
                    destination.getSystemPortSizeProperty().getValue(), destination.getVisibility(), true));

            destination.getSystemPortPositionProperty()
                .addListener((observable, oldValue, newValue) -> destinationPosition.setValue(
                    calculateEndPortPosition(destination.getSystemPortPositionProperty().getValue(),
                        destination.getSystemPortSizeProperty().getValue(), destination.getVisibility(), true)));
        } else {
            destinationPosition = new SimpleObjectProperty<>(
                calculateEndPortPosition(destination.getPosition(), destination.getSize(), destination.getVisibility(),
                    false));

            destination.getPositionProperty().addListener((observable, oldValue, newValue) -> {
                destinationPosition.setValue(calculateEndPortPosition(destination.getPosition(), destination.getSize(),
                    destination.getVisibility(), false));
            });
        }

        result.getEdgePoints().add(sourcePosition);
        result.getEdgePoints().add(destinationPosition);
    }
}
