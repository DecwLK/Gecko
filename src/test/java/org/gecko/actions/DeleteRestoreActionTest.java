package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DeleteRestoreActionTest {
    private static ActionManager actionManager;
    private static GeckoModel geckoModel;
    private static GeckoViewModel geckoViewModel;
    private static SystemViewModel rootSystemViewModel;
    private static SystemViewModel childSystemViewModel1;
    private static StateViewModel stateViewModel;
    private static StateViewModel stateViewModel2;

    @BeforeAll
    static void setUp() {
        geckoModel = new GeckoModel();
        geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        actionManager = new ActionManager(geckoViewModel);
        rootSystemViewModel = geckoViewModel.getCurrentEditor().getCurrentSystem();
        childSystemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        stateViewModel2 = viewModelFactory.createStateViewModelIn(childSystemViewModel1);
    }

    @Test
    void deleteElement() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        assertTrue(geckoViewModel.getCurrentEditor().getContainedPositionableViewModelElementsProperty().isEmpty());
    }

    @Test
    void restoreElementCheckViewModel() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        actionManager.undo();
        assertTrue(geckoViewModel.getCurrentEditor()
            .getContainedPositionableViewModelElementsProperty()
            .contains(stateViewModel));
    }

    @Test
    void restoreElementCheckModel() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        actionManager.undo();
        assertTrue(geckoModel.getRoot().getAutomaton().getStates().contains(stateViewModel.getTarget()));
    }

    @Test
    void deleteElementInChildSystem() {
        geckoViewModel.switchEditor(childSystemViewModel1, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel2)));
        assertTrue(geckoViewModel.getCurrentEditor().getContainedPositionableViewModelElementsProperty().isEmpty());
    }

    @Test
    void restoreElementCheckChildSystemViewModel() {
        geckoViewModel.switchEditor(childSystemViewModel1, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel2)));
        actionManager.undo();
        assertTrue(geckoViewModel.getCurrentEditor()
            .getContainedPositionableViewModelElementsProperty()
            .contains(stateViewModel2));
    }

    @Test
    void restoreElementCheckChildSystemModel() {
        geckoViewModel.switchEditor(childSystemViewModel1, true);
        actionManager.run(
            actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel2)));
        actionManager.undo();
        assertTrue(childSystemViewModel1.getTarget().getAutomaton().getStates().contains(stateViewModel2.getTarget()));
    }
}
