package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeleteStateViewModelElementActionTest {
    private Set<PositionableViewModelElement<?>> elements;
    private ActionManager actionManager;
    private ActionFactory actionFactory;
    private GeckoViewModel geckoViewModel;
    private SystemViewModel rootSystemViewModel;

    @BeforeEach
    void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        elements = Set.of(stateViewModel1, stateViewModel2);
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void newStateBecomesStartStateAfterAllStatesAreDeleted() throws ModelException {
        Action deleteAction = actionFactory.createDeletePositionableViewModelElementAction(elements);
        actionManager.run(deleteAction);
        assertEquals(0, rootSystemViewModel.getTarget().getAutomaton().getStates().size());
        StateViewModel newState = geckoViewModel.getViewModelFactory().createStateViewModelIn(rootSystemViewModel);
        assertEquals(rootSystemViewModel.getTarget().getAutomaton().getStartState(), newState.getTarget());
    }
}