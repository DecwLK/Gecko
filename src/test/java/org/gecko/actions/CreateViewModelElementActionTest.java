package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateViewModelElementActionTest {
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static GeckoViewModel geckoViewModel;
    private static ViewModelFactory viewModelFactory;
    private static SystemViewModel rootSystemViewModel;

    @BeforeAll
    static void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        viewModelFactory = geckoViewModel.getViewModelFactory();
        rootSystemViewModel = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() throws ModelException {
        Action createStateViewModelElementAction =
            actionFactory.createCreateStateViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createStateViewModelElementAction);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 1);

        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel1);
        actionManager.run(createContractAction);

        assertEquals(stateViewModel1.getContracts().size(), 1);

        Action createRegionViewModelElementAction =
            actionFactory.createCreateRegionViewModelElementAction(new Point2D(100, 100), new Point2D(200, 200),
                Color.RED);
        actionManager.run(createRegionViewModelElementAction);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 4);
        assertEquals(geckoViewModel.getCurrentEditor().getRegionViewModels(stateViewModel1).size(), 1);

        Action createEdgeViewModelElementAction =
            actionFactory.createCreateEdgeViewModelElementAction(stateViewModel1, stateViewModel2);
        actionManager.run(createEdgeViewModelElementAction);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 5);
        assertEquals(stateViewModel1.getOutgoingEdges().size(), 1);
        assertEquals(stateViewModel2.getIncomingEdges().size(), 1);

        geckoViewModel.switchEditor(rootSystemViewModel, false);

        Action createSystemViewModelElementAction =
            actionFactory.createCreateSystemViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createSystemViewModelElementAction);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 1);

        SystemViewModel systemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        SystemViewModel systemViewModel2 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 3);

        Action createPortViewModelElementAction1 =
            actionFactory.createCreatePortViewModelElementAction(systemViewModel1);
        actionManager.run(createPortViewModelElementAction1);
        Action createPortViewModelElementAction2 =
            actionFactory.createCreatePortViewModelElementAction(systemViewModel2);
        actionManager.run(createPortViewModelElementAction2);

        Action createSystemConnectionViewModelElementAction =
            actionFactory.createCreateSystemConnectionViewModelElementAction(systemViewModel1.getPorts().getFirst(),
                systemViewModel2.getPorts().getFirst());
        actionManager.run(createSystemConnectionViewModelElementAction);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 3);
    }

    @Test
    void getUndoAction() throws ModelException {
        Action createStateViewModelElementAction =
            actionFactory.createCreateStateViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createStateViewModelElementAction);

        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);

        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel1);
        actionManager.run(createContractAction);

        Action createRegionViewModelElementAction =
            actionFactory.createCreateRegionViewModelElementAction(new Point2D(100, 100), new Point2D(200, 200),
                Color.RED);
        actionManager.run(createRegionViewModelElementAction);

        Action createEdgeViewModelElementAction =
            actionFactory.createCreateEdgeViewModelElementAction(stateViewModel1, stateViewModel2);
        actionManager.run(createEdgeViewModelElementAction);

        actionManager.run(createEdgeViewModelElementAction.getUndoAction(actionFactory));
        assertEquals(stateViewModel1.getOutgoingEdges().size(), 0);
        assertEquals(stateViewModel2.getIncomingEdges().size(), 0);

        actionManager.run(createContractAction.getUndoAction(actionFactory));
        assertEquals(stateViewModel1.getContracts().size(), 0);

        actionManager.run(createStateViewModelElementAction.getUndoAction(actionFactory));
        actionManager.run(createRegionViewModelElementAction.getUndoAction(actionFactory));
        assertEquals(geckoViewModel.getCurrentEditor().getRegionViewModels(stateViewModel1).size(), 0);

        geckoViewModel.switchEditor(rootSystemViewModel, false);

        Action createSystemViewModelElementAction =
            actionFactory.createCreateSystemViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createSystemViewModelElementAction);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 4);
        actionManager.run(createSystemViewModelElementAction.getUndoAction(actionFactory));
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 3);
    }
}