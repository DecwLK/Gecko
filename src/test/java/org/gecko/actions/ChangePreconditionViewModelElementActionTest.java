package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.util.TestHelper;
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
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        region1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        viewModelFactory.createContractViewModelIn(stateViewModel);
        String preCondition = stateViewModel.getContractsProperty().getFirst().getPrecondition();
        String postCondition = stateViewModel.getContractsProperty().getFirst().getPostcondition();
        region1.getContract().setPrecondition(preCondition);
        region1.getContract().setPostcondition(postCondition);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action changePreconditionAction = actionFactory.createChangePreconditionViewModelElementAction(region1.getContract(), "newPrecondition");
        actionManager.run(changePreconditionAction);
        assertEquals("newPrecondition", region1.getContract().getPrecondition());
        assertEquals("newPrecondition", region1.getTarget().getPreAndPostCondition().getPreCondition().getCondition());
    }

    @Test
    void getUndoAction() {
        Action changePreconditionAction = actionFactory.createChangePreconditionViewModelElementAction(region1.getContract(), "newPrecondition");
        String beforeChangePrecondition = region1.getContract().getPrecondition();
        actionManager.run(changePreconditionAction);
        actionManager.undo();
        assertEquals(beforeChangePrecondition, region1.getContract().getPrecondition());
        assertEquals(beforeChangePrecondition, region1.getTarget().getPreAndPostCondition().getPreCondition().getCondition());
    }
}