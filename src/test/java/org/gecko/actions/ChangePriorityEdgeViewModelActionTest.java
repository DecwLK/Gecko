package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.gecko.exceptions.ModelException;
import org.gecko.model.Kind;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangePriorityEdgeViewModelActionTest {
    private EdgeViewModel edge;
    private ActionManager actionManager;
    private ActionFactory actionFactory;

    @BeforeEach
    void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);

        edge = viewModelFactory.createEdgeViewModelIn(rootSystemViewModel, stateViewModel1, stateViewModel2);
    }

    @Test
    void run() {
        Action changePriorityAction = actionFactory.createModifyEdgeViewModelPriorityAction(edge, 4);
        actionManager.run(changePriorityAction);
        assertEquals(4, edge.getPriority());
    }

    @Test
    void getUndoAction() {
        Action changePriorityAction = actionFactory.createModifyEdgeViewModelPriorityAction(edge, 4);
        int beforePriority = edge.getPriority();
        actionManager.run(changePriorityAction);
        actionManager.undo();
        assertEquals(beforePriority, edge.getPriority());
        assertEquals(beforePriority, edge.getTarget().getPriority());
    }
}
