package org.gecko.actions;

import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ChangePostconditionViewModelElementActionTest {

    private RegionViewModel region1;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        region1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel = null;
        try {
            stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        } catch (Exception e) {
            fail();
        }
        viewModelFactory.createContractViewModelIn(stateViewModel);
        String preCondition = stateViewModel.getContractsProperty().getFirst().getPrecondition();
        String postCondition = stateViewModel.getContractsProperty().getFirst().getPostcondition();
        region1.getContract().setPrecondition(preCondition);
        region1.getContract().setPostcondition(postCondition);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action changePostconditionAction =
            actionFactory.createChangePostconditionViewModelElementAction(region1.getContract(), "newPostcondition");
        actionManager.run(changePostconditionAction);
        assertEquals("newPostcondition", region1.getContract().getPostcondition());
        assertEquals("newPostcondition",
            region1.getTarget().getPreAndPostCondition().getPostCondition().getCondition());
    }

    @Test
    void getUndoAction() {
        Action changePostconditionAction =
            actionFactory.createChangePostconditionViewModelElementAction(region1.getContract(), "newPostcondition");
        String beforeChangePostcondition = region1.getContract().getPostcondition();
        actionManager.run(changePostconditionAction);
        actionManager.undo();
        assertEquals(beforeChangePostcondition, region1.getContract().getPostcondition());
        assertEquals(beforeChangePostcondition,
            region1.getTarget().getPreAndPostCondition().getPostCondition().getCondition());
    }
}