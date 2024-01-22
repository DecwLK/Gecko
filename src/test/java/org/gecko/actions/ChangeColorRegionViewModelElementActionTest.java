package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.paint.Color;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangeColorRegionViewModelElementActionTest {

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
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Color color = new Color(1, 1, 1, 0);
        Action changeColorRegionViewModelElementAction = actionFactory.createChangeColorRegionViewModelElementAction(region1, color);
        actionManager.run(changeColorRegionViewModelElementAction);
        assertEquals(color, region1.getColor());
    }

    @Test
    void getUndoAction() {
        Color color = new Color(0, 0, 0, 0);
        Action changeColorRegionViewModelElementAction = actionFactory.createChangeColorRegionViewModelElementAction(region1, color);
        Color beforeChangeColor = region1.getColor();
        actionManager.run(changeColorRegionViewModelElementAction);
        actionManager.undo();
        assertEquals(beforeChangeColor, region1.getColor());
    }

}