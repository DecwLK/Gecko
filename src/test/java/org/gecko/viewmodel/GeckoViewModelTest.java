package org.gecko.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.gecko.actions.ActionFactory;
import org.gecko.actions.ActionManager;
import org.gecko.model.GeckoModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GeckoViewModelTest {
    private static ActionManager actionManager;
    private static GeckoModel geckoModel;
    private static GeckoViewModel geckoViewModel;
    private static SystemViewModel rootSystemViewModel;
    private static SystemViewModel childSystemViewModel1;
    private static SystemViewModel childSystemViewModel2;
    private static StateViewModel stateViewModel;

    @BeforeAll
    static void setUp() {
        geckoModel = new GeckoModel();
        geckoViewModel = new GeckoViewModel(geckoModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        actionManager = new ActionManager(new ActionFactory(geckoViewModel));
        rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoModel.getRoot());
        childSystemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        childSystemViewModel2 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
    }

    @Test
    void switchEditor1() {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(1, geckoViewModel.getOpenedEditorsProperty().size());
        assertNotNull(geckoViewModel.getCurrentEditor());
    }

    @Test
    void switchEditor2() {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(1, geckoViewModel.getOpenedEditorsProperty().size());
    }

    @Test
    void switchEditor3() {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        assertEquals(2, geckoViewModel.getOpenedEditorsProperty().size());
    }

    @Test
    void switchEditorInitializeElements1() {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();
        assertFalse(editorViewModel.getContainedPositionableViewModelElementsProperty().contains(rootSystemViewModel));
        assertTrue(
            editorViewModel.getContainedPositionableViewModelElementsProperty().containsAll(List.of(childSystemViewModel1, childSystemViewModel2)));
    }

    @Test
    void switchEditorInitializeElements2() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        EditorViewModel editorViewModel = geckoViewModel.getCurrentEditor();
        assertTrue(editorViewModel.getContainedPositionableViewModelElementsProperty().contains(stateViewModel));
        assertFalse(editorViewModel.getContainedPositionableViewModelElementsProperty().contains(childSystemViewModel1));
    }

    @Test
    void deleteElement() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        assertTrue(geckoViewModel.getCurrentEditor().getContainedPositionableViewModelElementsProperty().isEmpty());
    }

    @Test
    void restoreElementCheckViewModel() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        actionManager.undo();
        assertTrue(geckoViewModel.getCurrentEditor().getContainedPositionableViewModelElementsProperty().contains(stateViewModel));
    }

    @Test
    void restoreElementCheckModel() {
        geckoViewModel.switchEditor(rootSystemViewModel, true);
        actionManager.run(actionManager.getActionFactory().createDeletePositionableViewModelElementAction(List.of(stateViewModel)));
        actionManager.undo();
        assertTrue(geckoModel.getRoot().getAutomaton().getStates().contains(stateViewModel.getTarget()));
    }
}