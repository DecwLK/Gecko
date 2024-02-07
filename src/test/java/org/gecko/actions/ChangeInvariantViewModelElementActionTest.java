package org.gecko.actions;

import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeInvariantViewModelElementActionTest {

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
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action changeInvariantAction =
            actionFactory.createChangeInvariantViewModelElementAction(region1, "newInvariant");
        actionManager.run(changeInvariantAction);
        assertEquals("newInvariant", region1.getInvariant());
        assertEquals("newInvariant", region1.getTarget().getInvariant().getCondition());
    }

    @Test
    void getUndoAction() {
        Action changeInvariantAction =
            actionFactory.createChangeInvariantViewModelElementAction(region1, "newInvariant");
        String beforeChangeInvariant = region1.getInvariant();
        actionManager.run(changeInvariantAction);
        actionManager.undo();
        assertEquals(beforeChangeInvariant, region1.getInvariant());
        assertEquals(beforeChangeInvariant, region1.getTarget().getInvariant().getCondition());
    }
}