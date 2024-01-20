package org.gecko.viewmodel;

import javax.sound.sampled.Port;
import org.gecko.actions.ActionManager;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.InvalidConnectingPointType;
import org.gecko.exceptions.MissingViewModelElement;
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
 * Represents a factory for the view model elements of a Gecko project. Provides a method for the creation of each element.
 */
public class ViewModelFactory {
    private static int viewModelElementId = 0;
    private final ActionManager actionManager;
    private final ModelFactory modelFactory;
    private final GeckoViewModel geckoViewModel;

    public ViewModelFactory(ActionManager actionManager, GeckoViewModel geckoViewModel, ModelFactory modelFactory) {
        this.actionManager = actionManager;
        this.geckoViewModel = geckoViewModel;
        this.modelFactory = modelFactory;
    }

    public EditorViewModel createEditorViewModel(SystemViewModel systemViewModel, SystemViewModel parentSystem, boolean isAutomatonEditor) {
        return new EditorViewModel(actionManager, systemViewModel, parentSystem, isAutomatonEditor);
    }

    public StateViewModel createStateViewModelIn(SystemViewModel parentSystem) {
        State state = modelFactory.createState(parentSystem.getTarget().getAutomaton());
        StateViewModel result = new StateViewModel(getNewViewModelElementId(), state);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public StateViewModel createStateViewModelFrom(State state) {
        StateViewModel result = new StateViewModel(getNewViewModelElementId(), state);
        for (Contract contract : state.getContracts()) {
            ContractViewModel contractViewModel = createContractViewModelFrom(contract);
            result.addContract(contractViewModel);
        }
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem, StateViewModel source, StateViewModel destination) {
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(), destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem, PortViewModel source, StateViewModel destination) throws InvalidConnectingPointType{
        if (!source.getVisibility().equals(Visibility.INPUT)) {
            throw new InvalidConnectingPointType("The EdgeViewModel's source must be a PortViewModel with INPUT Visibility.");
        }
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(), destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem, StateViewModel source, PortViewModel destination) throws InvalidConnectingPointType{
        if (!destination.getVisibility().equals(Visibility.OUTPUT)) {
            throw new InvalidConnectingPointType("The EdgeViewModel's destination must be a PortViewModel with OUTPUT Visibility.");
        }
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(), destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem, PortViewModel source, PortViewModel destination) throws InvalidConnectingPointType{
        if (!source.getVisibility().equals(Visibility.INPUT) || !destination.getVisibility().equals(Visibility.OUTPUT)) {
            throw new InvalidConnectingPointType("The EdgeViewModel's source and destination PortViewModels have incompatible Visibilities.");
        }
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(), destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelFrom(Edge edge) throws GeckoException {
        PositionableViewModelElement<?> source = geckoViewModel.getViewModelElement(edge.getSource());
        PositionableViewModelElement<?> destination = geckoViewModel.getViewModelElement(edge.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElement(
                "Tried to create an EdgeViewModel from an Edge that contains a State that does not have a StateViewModel");
        }

        // TODO: Suggested -> getEdgeViewModel() method containing the following lines.
        EdgeViewModel result;
        switch (source) {
            case StateViewModel stateViewModel when destination instanceof StateViewModel ->
                result = new EdgeViewModel(getNewViewModelElementId(), edge, stateViewModel, (StateViewModel) destination);
            case StateViewModel stateViewModel when destination instanceof PortViewModel ->
                result = new EdgeViewModel(getNewViewModelElementId(), edge, stateViewModel, (PortViewModel) destination);
            case PortViewModel portViewModel when destination instanceof StateViewModel ->
                result = new EdgeViewModel(getNewViewModelElementId(), edge, portViewModel, (StateViewModel) destination);
            case PortViewModel portViewModel when destination instanceof PortViewModel ->
                result = new EdgeViewModel(getNewViewModelElementId(), edge, portViewModel, (PortViewModel) destination);
            default -> {
                result = null;
            }
        }

        if (result == null) {
            throw new GeckoException("Tried to create EdgeViewModel from invalid Edge.");
        }
        geckoViewModel.addViewModelElement(result);
        return result;

        /*StateViewModel source = (StateViewModel) geckoViewModel.getViewModelElement(edge.getSource());
        StateViewModel destination = (StateViewModel) geckoViewModel.getViewModelElement(edge.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElement(
                "Tried to create an EdgeViewModel from an Edge that contains a State that does not have a StateViewModel");
        }
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;*/
    }

    public SystemConnectionViewModel createSystemConnectionViewModelIn(SystemViewModel parentSystem, PortViewModel source,
                                                                       PortViewModel destination) throws InvalidConnectingPointType {
        if (!source.getVisibility().equals(Visibility.OUTPUT) || !destination.getVisibility().equals(Visibility.INPUT)) {
            throw new InvalidConnectingPointType("Tried to connect two incompatible PortViewModels.");
        }

        SystemConnection systemConnection =
            modelFactory.createSystemConnection(parentSystem.getTarget(), source.getTarget(), destination.getTarget());
        SystemConnectionViewModel result = new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelFrom(SystemConnection systemConnection) throws MissingViewModelElement {
        PortViewModel source = (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getSource());
        PortViewModel destination = (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElement(
                "Tried to create a SystemConnectionViewModel from a SystemConnection that contains a Variable that does not have a PortViewModel");
        }
        SystemConnectionViewModel result = new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemViewModel createSystemViewModelIn(SystemViewModel parentSystem) {
        System system = modelFactory.createSystem(parentSystem.getTarget());
        SystemViewModel result = new SystemViewModel(getNewViewModelElementId(), system);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

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

    public RegionViewModel createRegionViewModelIn(SystemViewModel parentSystem) {
        Region region = modelFactory.createRegion(parentSystem.getTarget().getAutomaton());
        RegionViewModel result =
            new RegionViewModel(getNewViewModelElementId(), region, createContractViewModelFrom(region.getPreAndPostCondition()));
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public RegionViewModel createRegionViewModelFrom(Region region) throws MissingViewModelElement {
        RegionViewModel result =
            new RegionViewModel(getNewViewModelElementId(), region, createContractViewModelFrom(region.getPreAndPostCondition()));
        for (State state : region.getStates()) {
            StateViewModel stateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(state);
            if (stateViewModel == null) {
                throw new MissingViewModelElement(
                    "Tried to create a region view model from a region that contains a state that does not have a view model");
            }
            result.addState(stateViewModel);
        }
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public PortViewModel createPortViewModelIn(SystemViewModel systemViewModel) {
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

    public ContractViewModel createContractViewModelIn(StateViewModel stateViewModel) {
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

    private static int getNewViewModelElementId() {
        return viewModelElementId++;
    }
}
