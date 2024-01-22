package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.viewmodel.StateViewModel;

public class EdgeCreatorTool extends Tool {

    private static final String NAME = "Edge Creator Tool";
    private StateViewModel source;

    public EdgeCreatorTool(ActionManager actionManager) {
        super(actionManager);
        source = null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconPath() {
        //TODO stub
        return null;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        source = null;
        stateViewElement.setOnMouseClicked(event -> {
            if (source == null || source == stateViewElement.getTarget()) {
                source = stateViewElement.getTarget();
            } else {
                Action createEdgeAction =
                    actionManager.getActionFactory().createCreateEdgeViewModelElementAction(source, stateViewElement.getTarget());
                actionManager.run(createEdgeAction);
                source = null;
            }
            //TODO check if edge already exists and potentially remove it
        });
    }
}
