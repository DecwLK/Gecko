package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ChangeCodeSystemViewModelElementActionTest {
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static SystemViewModel systemViewModel;

    @BeforeAll
    static void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        systemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
    }

    @Test
    void run() {
        Action changeCodeAction =
            actionFactory.createChangeCodeSystemViewModelAction(systemViewModel, "newCode");
        actionManager.run(changeCodeAction);
        assertEquals("newCode", systemViewModel.getCode());

        Action changeCodeAction2 =
            actionFactory.createChangeCodeSystemViewModelAction(systemViewModel, "");
        actionManager.run(changeCodeAction2);
        assertNull(systemViewModel.getCode());
    }

    @Test
    void getUndoAction() {
        Action changeCodeAction = actionFactory.createChangeCodeSystemViewModelAction(systemViewModel, "newCode2");
        String beforeChangeCode = systemViewModel.getCode();
        actionManager.run(changeCodeAction);
        assertEquals("newCode2", systemViewModel.getCode());
        actionManager.undo();
        assertEquals(beforeChangeCode, systemViewModel.getCode());
    }
}
