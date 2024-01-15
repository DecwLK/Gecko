package org.gecko.viewmodel;

import org.gecko.model.Contract;
import org.gecko.model.Edge;
import org.gecko.model.ModelFactory;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;

public class ViewModelFactory {
    private final ModelFactory modelFactory;
    private final GeckoViewModel geckoViewModel;

    public ViewModelFactory(GeckoViewModel geckoViewModel, ModelFactory modelFactory) {
        this.geckoViewModel = geckoViewModel;
        this.modelFactory = modelFactory;
    }

    public EditorViewModel createEditorViewModel(SystemViewModel systemViewModel, boolean isAutomatonEditor) {
        return new EditorViewModel(systemViewModel, isAutomatonEditor);
    }

    public StateViewModel createStateViewModelIn(SystemViewModel parentSystem) {
        State state = modelFactory.createState(parentSystem.target.getAutomaton());
        StateViewModel result = createStateViewModelFrom(state);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public StateViewModel createStateViewModelFrom(State state) {
        StateViewModel result = new StateViewModel(state);
        state.getContracts().stream().map(ContractViewModel::new).forEach(result::addContract);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem) {
        Edge edge = modelFactory.createEdge(parentSystem.target.getAutomaton(), null, null);
        EdgeViewModel result = createEdgeViewModelFrom(edge);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public EdgeViewModel createEdgeViewModelFrom(Edge edge) {
        EdgeViewModel result = new EdgeViewModel(edge);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelIn(SystemViewModel parentSystem) {
        SystemConnection systemConnection = modelFactory.createSystemConnection(parentSystem.target, null, null);
        SystemConnectionViewModel result = createSystemConnectionViewModelFrom(systemConnection);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemConnectionViewModel createSystemConnectionViewModelFrom(SystemConnection systemConnection) {
        SystemConnectionViewModel result = new SystemConnectionViewModel(systemConnection);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemViewModel createSystemViewModelIn(SystemViewModel parentSystem) {
        System system = modelFactory.createSystem(parentSystem.target);
        SystemViewModel result = createSystemViewModelFrom(system);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public SystemViewModel createSystemViewModelFrom(System system) {
        SystemViewModel result = new SystemViewModel(system);
        system.getVariables().stream().map(this::createPortViewModelFrom).forEach(result::addPort);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public RegionViewModel createRegionViewModelIn(SystemViewModel parentSystem) {
        Region region = modelFactory.createRegion(parentSystem.target.getAutomaton());
        RegionViewModel result = createRegionViewModelFrom(region);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public RegionViewModel createRegionViewModelFrom(Region region) {
        RegionViewModel result = new RegionViewModel(region);
        region.getStates().stream().map(this::createStateViewModelFrom).forEach(result::addState);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public PortViewModel createPortViewModelIn(SystemViewModel systemViewModel) {
        Variable variable = modelFactory.createVariable(systemViewModel.target);
        PortViewModel result = createPortViewModelFrom(variable);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public PortViewModel createPortViewModelFrom(Variable variable) {
        PortViewModel result = new PortViewModel(variable);
        geckoViewModel.addViewModelElement(result);
        return result;
    }

    public ContractViewModel createContractViewModelIn(StateViewModel stateViewModel) {
        Contract contract = modelFactory.createContract(stateViewModel.target);
        ContractViewModel result = new ContractViewModel(contract);
        stateViewModel.addContract(result);
        return result;
    }
}
