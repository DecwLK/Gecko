package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.gecko.exceptions.ModelException;
import org.gecko.tools.ToolType;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectToolActionTest {
    private ActionManager actionManager;
    private ActionFactory actionFactory;
    private GeckoViewModel geckoViewModel;

    @BeforeEach
    void setUp() throws ModelException {
        geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        SystemViewModel rootSystemViewModel =
            viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());

        geckoViewModel.switchEditor(rootSystemViewModel, true);
    }

    @Test
    void selectCursorTool() {
        Action selectToolAction = actionFactory.createSelectToolAction(ToolType.CURSOR);
        actionManager.run(selectToolAction);
        assertEquals(ToolType.CURSOR, geckoViewModel.getCurrentEditor().getCurrentToolType());
    }

    @Test
    void undoSelectCursorTool() {
        Action stateCreatorToolAction = actionFactory.createSelectToolAction(ToolType.STATE_CREATOR);
        actionManager.run(stateCreatorToolAction);
        assertEquals(ToolType.STATE_CREATOR, geckoViewModel.getCurrentEditor().getCurrentToolType());
        Action selectToolAction = actionFactory.createSelectToolAction(ToolType.CURSOR);
        actionManager.run(selectToolAction);
        actionManager.undo();
        assertEquals(ToolType.CURSOR, geckoViewModel.getCurrentEditor().getCurrentToolType());
    }
}
