package org.gecko.viewmodel;

import org.gecko.actions.ActionManager;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;

/**
 * Represents a factory for the view model elements of a Gecko project. Provides a method for the creation of each
 * element.
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

    public EditorViewModel createEditorViewModel(
        SystemViewModel systemViewModel, SystemViewModel parentSystem, boolean isAutomatonEditor) {
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

    public EdgeViewModel createEdgeViewModelIn(
        SystemViewModel parentSystem, StateViewModel source, StateViewModel destination) {
        Edge edge = modelFactory.createEdge(parentSystem.getTarget().getAutomaton(), source.getTarget(),
            destination.getTarget());
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        actionManager.getActionFactory().createDeletePositionableViewModelElementAction(result);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelFrom(Edge edge) throws MissingViewModelElement {
        StateViewModel source = (StateViewModel) geckoViewModel.getViewModelElement(edge.getSource());
        StateViewModel destination = (StateViewModel) geckoViewModel.getViewModelElement(edge.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElement("Missing source or destination for edge.");
        }
        EdgeViewModel result = new EdgeViewModel(getNewViewModelElementId(), edge, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelIn(
        SystemViewModel parentSystem, PortViewModel source, PortViewModel destination) {
        SystemConnection systemConnection =
            modelFactory.createSystemConnection(parentSystem.getTarget(), source.getTarget(), destination.getTarget());
        SystemConnectionViewModel result =
            new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelFrom(SystemConnection systemConnection)
        throws MissingViewModelElement {
        PortViewModel source = (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getSource());
        PortViewModel destination =
            (PortViewModel) geckoViewModel.getViewModelElement(systemConnection.getDestination());
        if (source == null || destination == null) {
            throw new MissingViewModelElement("Missing source or destination for system connection.");
        }
        SystemConnectionViewModel result =
            new SystemConnectionViewModel(getNewViewModelElementId(), systemConnection, source, destination);
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
        RegionViewModel result = new RegionViewModel(getNewViewModelElementId(), region,
            createContractViewModelFrom(region.getPreAndPostCondition()));
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public RegionViewModel createRegionViewModelFrom(Region region) throws MissingViewModelElement {
        RegionViewModel result = new RegionViewModel(getNewViewModelElementId(), region,
            createContractViewModelFrom(region.getPreAndPostCondition()));
        for (State state : region.getStates()) {
            StateViewModel stateViewModel = (StateViewModel) geckoViewModel.getViewModelElement(state);
            if (stateViewModel == null) {
                throw new MissingViewModelElement("Region contains invalid state.");
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
