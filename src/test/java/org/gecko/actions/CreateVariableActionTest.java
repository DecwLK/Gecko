package org.gecko.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.geometry.Point2D;
import org.gecko.exceptions.ModelException;
import org.gecko.util.TestHelper;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CreateVariableActionTest {
    private static ActionManager actionManager;
    private static ActionFactory actionFactory;
    private static SystemViewModel parent;

    @BeforeAll
    static void setUp() throws ModelException {
        GeckoViewModel geckoViewModel = TestHelper.createGeckoViewModel();
        actionManager = new ActionManager(geckoViewModel);
        actionFactory = new ActionFactory(geckoViewModel);
        ViewModelFactory viewModelFactory = geckoViewModel.getViewModelFactory();
        parent = viewModelFactory.createSystemViewModelFrom(geckoViewModel.getGeckoModel().getRoot());
    }

    @Test
    void run() {
        Action createVariableAction = actionFactory.createCreateVariableAction(new Point2D(0, 0));
        actionManager.run(createVariableAction);
        assertEquals(1, parent.getTarget().getVariables().size());
    }

    @Test
    void getUndoAction() {
        assertEquals(1, parent.getTarget().getVariables().size());
        actionManager.undo();
        assertEquals(0, parent.getTarget().getVariables().size());
    }
}
