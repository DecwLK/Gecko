package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.geometry.Point2D;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RenameViewModelElementActionTest {
    public static final String NEW_NAME_2 = "newName2";
    public static final String NEW_NAME = "newName";
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static StateViewModel stateViewModel;

    @BeforeAll
    static void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel systemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
        stateViewModel = viewModelFactory.createStateViewModelIn(systemViewModel);
    }

    @Test
    void run() {
        Action renameAction = actionFactory.createRenameViewModelElementAction(stateViewModel, NEW_NAME);
        actionManager.run(renameAction);
        assertEquals(NEW_NAME, stateViewModel.getName());
    }

    @Test
    void getUndoAction() {
        Action renameAction = actionFactory.createRenameViewModelElementAction(stateViewModel, NEW_NAME_2);
        String beforeChangeName = stateViewModel.getName();
        actionManager.run(renameAction);
        assertEquals(NEW_NAME_2, stateViewModel.getName());
        actionManager.undo();
        assertEquals(beforeChangeName, stateViewModel.getName());
    }
}
