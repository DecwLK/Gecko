package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Visibility;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeletePositionableViewModelElementActionTest {
    private Set<PositionableViewModelElement<?>> elements;
    private StateViewModel stateViewModel1;
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

        stateViewModel1 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        StateViewModel stateViewModel2 = viewModelFactory.createStateViewModelIn(rootSystemViewModel);
        EdgeViewModel edge =
            viewModelFactory.createEdgeViewModelIn(rootSystemViewModel, stateViewModel1, stateViewModel2);
        ContractViewModel contractViewModel = viewModelFactory.createContractViewModelIn(stateViewModel1);
        edge.setContract(contractViewModel);
        RegionViewModel regionViewModel = viewModelFactory.createRegionViewModelIn(rootSystemViewModel);
        SystemViewModel systemViewModel1 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        SystemViewModel systemViewModel2 = viewModelFactory.createSystemViewModelIn(rootSystemViewModel);
        PortViewModel portViewModel1 = viewModelFactory.createPortViewModelIn(systemViewModel1);
        portViewModel1.setVisibility(Visibility.OUTPUT);
        portViewModel1.updateTarget();
        PortViewModel portViewModel2 = viewModelFactory.createPortViewModelIn(systemViewModel2);
        portViewModel2.setVisibility(Visibility.INPUT);
        portViewModel2.updateTarget();
        SystemConnectionViewModel systemConnectionViewModel =
            viewModelFactory.createSystemConnectionViewModelIn(rootSystemViewModel, portViewModel1, portViewModel2);

        elements = Set.of(stateViewModel1, stateViewModel2, edge, regionViewModel, systemViewModel1, systemViewModel2,
            systemConnectionViewModel);

        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void run() {
        Action deleteAction = actionFactory.createDeletePositionableViewModelElementAction(elements);
        actionManager.run(deleteAction);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);
    }

    @Test
    void getUndoAction() {
        Action deleteAction = actionFactory.createDeletePositionableViewModelElementAction(elements);
        actionManager.run(deleteAction);

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);

        actionManager.undo();

        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 3);
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 3);

        actionManager.redo();
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);
        geckoViewModel.switchEditor(rootSystemViewModel, false);
        assertEquals(geckoViewModel.getCurrentEditor().getPositionableViewModelElements().size(), 0);
    }
}
