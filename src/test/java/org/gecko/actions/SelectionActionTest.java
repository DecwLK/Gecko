package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SelectionActionTest {
    private static GeckoViewModel geckoViewModel;
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static StateViewModel stateViewModel1;
    private static StateViewModel stateViewModel2;
    private static StateViewModel stateViewModel3;
    private static StateViewModel stateViewModel4;

    @BeforeAll
    static void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel systemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        stateViewModel1 = viewModelFactory.createStateViewModelIn(systemViewModel);
        stateViewModel2 = viewModelFactory.createStateViewModelIn(systemViewModel);
        stateViewModel3 = viewModelFactory.createStateViewModelIn(systemViewModel);
        stateViewModel4 = viewModelFactory.createStateViewModelIn(systemViewModel);
    }

    @Test
    void singleSelectionTest() {
        Action selectAction = actionFactory.createSelectAction(stateViewModel1, true);
        actionManager.run(selectAction);
        assertEquals(1, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());

        actionManager.undo();
        assertEquals(1, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }

    @Test
    void multiSelectionTest() {
        selectBunch();

        actionManager.undo();
        assertEquals(4, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }

    @Test
    void deselectTest() {
        selectBunch();

        Action deselectAction = actionFactory.createDeselectAction();
        actionManager.run(deselectAction);
        assertEquals(0, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());

        actionManager.undo();
        assertEquals(0, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }

    @Test
    void selectEmpty() {
        selectBunch();

        Action selectAction = actionFactory.createSelectAction(Set.of(), true);
        actionManager.run(selectAction);
        assertEquals(0, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }

    @Test
    void selectionHistoryTest() {
        Action selectNone = actionFactory.createSelectAction(Set.of(), true);
        actionManager.run(selectNone);
        assertEquals(0, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
        selectBunch();

        Action previousSelectionAction = actionFactory.createSelectionHistoryBackAction();
        actionManager.run(previousSelectionAction);
        assertEquals(2, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());

        Action nextSelectionAction = actionFactory.createSelectionHistoryForwardAction();
        actionManager.run(nextSelectionAction);
        assertEquals(4, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }

    private static void selectBunch() {
        Action selectAction1 = actionFactory.createSelectAction(stateViewModel1, true);
        actionManager.run(selectAction1);
        Action selectAction2 = actionFactory.createSelectAction(stateViewModel2, false);
        actionManager.run(selectAction2);
        Action selectAction3 = actionFactory.createSelectAction(Set.of(stateViewModel3, stateViewModel4), false);
        actionManager.run(selectAction3);
        assertEquals(4, geckoViewModel.getCurrentEditor().getSelectionManager().getCurrentSelection().size());
    }
}
