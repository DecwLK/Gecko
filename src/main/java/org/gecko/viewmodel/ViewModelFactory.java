package org.gecko.viewmodel;

import org.gecko.model.System;
import org.gecko.model.Region;
import org.gecko.model.State;
import org.gecko.model.Edge;
import org.gecko.model.SystemConnection;
import org.gecko.model.Variable;
import org.gecko.model.ModelFactory;

public class ViewModelFactory {
    private final ModelFactory modelFactory;
    private final GeckoViewModel geckoViewModel;

    public ViewModelFactory(GeckoViewModel geckoViewModel, ModelFactory modelFactory) {
        this.geckoViewModel = geckoViewModel;
        this.modelFactory = modelFactory;
    }
    public EditorViewModel createEditorViewModel(SystemViewModel systemViewModel, boolean isAutomatonEditor) {
        //TODO stub
        return null;
    }
    public StateViewModel createStateViewModelIn(SystemViewModel parentSystem) {
        //TODO stub
        return null;
    }
    public StateViewModel createStateViewModelFrom(State state) {
        //TODO stub
        return null;
    }
    public EdgeViewModel createEdgeViewModelIn(SystemViewModel parentSystem) {
        //TODO stub
        return null;
    }
    public EdgeViewModel createEdgeViewModelFrom(Edge edge) {
        //TODO stub
        return null;
    }
    public SystemConnectionViewModel createSystemConnectionViewModelIn(SystemViewModel parentSystem) {
        //TODO stub
        return null;
    }
    public SystemConnectionViewModel createSystemConnectionViewModelFrom(SystemConnection systemConnection) {
        //TODO stub
        return null;
    }
    public SystemViewModel createSystemViewModelIn(SystemViewModel parentSystem) {
        //TODO stub
        return null;
    }
    public SystemViewModel createSystemViewModelFrom(System system) {
        //TODO stub
        return null;
    }
    public RegionViewModel createRegionViewModelIn(SystemViewModel parentSystem) {
        //TODO stub
        return null;
    }
    public RegionViewModel createRegionViewModelFrom(Region region) {
        //TODO stub
        return null;
    }
    public PortViewModel createPortViewModelIn(SystemViewModel systemViewModel) {
        //TODO stub
        return null;
    }
    public PortViewModel createPortViewModelFrom(Variable variable) {
        //TODO stub
        return null;
    }
    public ContractViewModel createContractViewModelIn(StateViewModel stateViewModel) {
        //TODO stub
        return null;
    }

}
