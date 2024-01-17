package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangePreconditionViewModelElementActionTest {

    private RegionViewModel region1;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        actionManager = new ActionManager(new ActionFactory(geckoViewModel));
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        region1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        viewModelFactory.createContractViewModelIn(stateViewModel);
        region1.setContract(stateViewModel.getContractsProperty().getFirst());
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action changePreconditionAction = actionFactory.createChangePreconditionViewModelElementAction(region1.getContract(), "newPrecondition");
        actionManager.run(changePreconditionAction);
        assertEquals("newPrecondition", region1.getContract().getPrecondition());
    }

    @Test
    void getUndoAction() {
        Action changePreconditionAction = actionFactory.createChangePreconditionViewModelElementAction(region1.getContract(), "newPrecondition");
        String beforeChangePrecondition = region1.getContract().getPrecondition();
        actionManager.run(changePreconditionAction);
        actionManager.undo();
        assertEquals(beforeChangePrecondition, region1.getContract().getPrecondition());
    }
}