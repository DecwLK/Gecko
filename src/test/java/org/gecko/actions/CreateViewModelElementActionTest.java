package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Visibility;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
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
    void createContractAction() throws ModelException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel);
        actionManager.run(createContractAction);
        assertEquals(stateViewModel.getContracts().size(), 1);
    }

    @Test
    void undoContractAction() throws ModelException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createContractAction = actionFactory.createCreateContractViewModelElementAction(stateViewModel);
        actionManager.run(createContractAction);
        actionManager.undo();
        assertEquals(stateViewModel.getContracts().size(), 0);
    }

    @Test
    void createRegionViewModelElementAction() throws ModelException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createRegionViewModelElementAction =
            actionFactory.createCreateRegionViewModelElementAction(new Point2D(100, 100), new Point2D(200, 200),
                Color.RED);
        actionManager.run(createRegionViewModelElementAction);
        assertEquals(geckoViewModel.getCurrentEditor().getRegionViewModels(stateViewModel).size(), 1);
    }

    @Test
    void undoRegionViewModelElementAction() throws ModelException {
        StateViewModel stateViewModel = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createRegionViewModelElementAction =
            actionFactory.createCreateRegionViewModelElementAction(new Point2D(100, 100), new Point2D(200, 200),
                Color.RED);
        actionManager.run(createRegionViewModelElementAction);
        actionManager.undo();
        assertEquals(geckoViewModel.getCurrentEditor().getRegionViewModels(stateViewModel).size(), 0);
    }

    @Test
    void createEdgeViewModelElementAction() throws ModelException {
        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createEdgeViewModelElementAction =
            actionFactory.createCreateEdgeViewModelElementAction(stateViewModel1, stateViewModel2);
        actionManager.run(createEdgeViewModelElementAction);
        assertEquals(stateViewModel1.getOutgoingEdges().size(), 1);
        assertEquals(stateViewModel2.getIncomingEdges().size(), 1);
    }

    @Test
    void undoEdgeViewModelElementAction() throws ModelException {
        StateViewModel stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        Action createEdgeViewModelElementAction =
            actionFactory.createCreateEdgeViewModelElementAction(stateViewModel1, stateViewModel2);
        actionManager.run(createEdgeViewModelElementAction);
        actionManager.undo();
        assertEquals(stateViewModel1.getOutgoingEdges().size(), 0);
        assertEquals(stateViewModel2.getIncomingEdges().size(), 0);
    }

    @Test
    void createSystemViewModelElementAction() throws ModelException {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        Action createSystemViewModelElementAction =
            actionFactory.createCreateSystemViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createSystemViewModelElementAction);
        assertEquals(rootSystemViewModel.getTarget().getChildren().size(), 5);
    }

    @Test
    void undoSystemViewModelElementAction() throws ModelException {
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        Action createSystemViewModelElementAction =
            actionFactory.createCreateSystemViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createSystemViewModelElementAction);
        actionManager.undo();
        assertEquals(rootSystemViewModel.getTarget().getChildren().size(), 7);
    }

    @Test
    void createPortViewModelElementAction() throws ModelException {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        Action createPortViewModelElementAction = actionFactory.createCreatePortViewModelElementAction(systemViewModel);
        actionManager.run(createPortViewModelElementAction);
        assertEquals(systemViewModel.getPorts().size(), 1);
    }

    @Test
    void undoPortViewModelElementAction() throws ModelException {
        SystemViewModel systemViewModel = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        Action createPortViewModelElementAction = actionFactory.createCreatePortViewModelElementAction(systemViewModel);
        actionManager.run(createPortViewModelElementAction);
        actionManager.undo();
        assertEquals(systemViewModel.getPorts().size(), 0);
    }

    @Test
    void createSystemConnectionViewModelElementAction() throws ModelException {
        SystemViewModel systemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        SystemViewModel systemViewModel2 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        PortViewModel portViewModel1 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        portViewModel1.setVisibility(Visibility.OUTPUT);
        PortViewModel portViewModel2 = viewModelFactory.createPortViewModelIn(systemViewModel2);
        portViewModel2.setVisibility(Visibility.INPUT);
        Action createSystemConnectionViewModelElementAction =
            actionFactory.createCreateSystemConnectionViewModelElementAction(portViewModel1, portViewModel2);
        actionManager.run(createSystemConnectionViewModelElementAction);
        assertEquals(rootSystemViewModel.getTarget().getConnections().size(), 1);
    }

    @Test
    void undoSystemConnectionViewModelElementAction() throws ModelException {
        SystemViewModel systemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        SystemViewModel systemViewModel2 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        PortViewModel portViewModel1 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        portViewModel1.setVisibility(Visibility.OUTPUT);
        PortViewModel portViewModel2 = viewModelFactory.createPortViewModelIn(systemViewModel2);
        portViewModel2.setVisibility(Visibility.INPUT);
        Action createSystemConnectionViewModelElementAction =
            actionFactory.createCreateSystemConnectionViewModelElementAction(portViewModel1, portViewModel2);
        actionManager.run(createSystemConnectionViewModelElementAction);
        actionManager.undo();
        assertEquals(rootSystemViewModel.getTarget().getConnections().size(), 0);
    }

    @Test
    void createStateViewModel() throws ModelException {
        Action createStateViewModelElementAction =
            actionFactory.createCreateStateViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createStateViewModelElementAction);

        assertEquals(rootSystemViewModel.getTarget().getAutomaton().getStates().size(), 7);
    }

    @Test
    void undoStateViewModel() throws ModelException {
        Action createStateViewModelElementAction =
            actionFactory.createCreateStateViewModelElementAction(new Point2D(100, 100));
        actionManager.run(createStateViewModelElementAction);
        actionManager.undo();

        assertEquals(rootSystemViewModel.getTarget().getAutomaton().getStates().size(), 2);
    }
}