package org.gecko.actions;

import java.util.List;
import javafx.scene.paint.Color;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActionGroupTest {

    private RegionViewModel region1;
    private RegionViewModel region2;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    //These tests could fail because of an error in the ChangeColorAction but some Action had to be used for the tests
    @BeforeEach
    void setUp() {
        GeckoModel geckoModel = new GeckoModel();
        GeckoViewModel geckoViewModel = new GeckoViewModel(geckoModel);
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        region1 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        region2 = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void testUndoActionReturnsNullWithNonUndoableAction() {
        Action changeColor1Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(1, 1, 1, 0));
        Action focusAction = actionFactory.createFocusPositionableViewModelElementAction(region2);
        ActionGroup actionGroup = new ActionGroup(List.of(changeColor1Action, focusAction));
        actionManager.run(actionGroup);
        assertNull(actionGroup.getUndoAction(actionFactory));
    }

    @Test
    void testUndoActionNotNull() {
        Action changeColor1Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(1, 1, 1, 0));
        ActionGroup actionGroup = new ActionGroup(List.of(changeColor1Action));
        actionManager.run(actionGroup);
        Action undoAction = actionGroup.getUndoAction(actionFactory);
        assertNotEquals(undoAction, null);
    }

    @Test
    void testActionGroupRunsInOrder() {
        Action changeColor1Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(1, 1, 1, 0));
        Action changeColor2Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(0, 0, 0, 0));
        ActionGroup actionGroup = new ActionGroup(List.of(changeColor1Action, changeColor2Action));
        actionManager.run(actionGroup);
        assertEquals(new Color(0, 0, 0, 0), region1.getColor());
    }

    @Test
    void testOrderOfUndoActions() {
        Color beforeChange = region1.getColor();
        Action changeColor1Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(1, 1, 1, 0));
        Action changeColor2Action =
            actionFactory.createChangeColorRegionViewModelElementAction(region1, new Color(0, 0, 0, 0));
        ActionGroup actionGroup = new ActionGroup(List.of(changeColor1Action, changeColor2Action));
        actionManager.run(actionGroup);
        assertEquals(new Color(0, 0, 0, 0), region1.getColor());
        Action undoAction = actionGroup.getUndoAction(actionFactory);
        assertNotEquals(undoAction, null);
        actionManager.run(undoAction);
        assertEquals(beforeChange, region1.getColor());
    }
}