package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.PortViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;

public class SystemConnectionCreatorTool extends Tool {

    private PortViewElement firstPortViewElement;

    public SystemConnectionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.CONNECTION_CREATOR);
        firstPortViewElement = null;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void visit(PortViewElement portViewElement) {
        super.visit(portViewElement);
        portViewElement.setOnMouseClicked(event -> {
            if (firstPortViewElement == null || firstPortViewElement == portViewElement) {
                firstPortViewElement = portViewElement;
            } else {
                Action createAction = actionManager.getActionFactory()
                    .createCreateSystemConnectionViewModelElementAction(firstPortViewElement.getViewModel(),
                        portViewElement.getViewModel());
                actionManager.run(createAction);
                firstPortViewElement = null;
            }
        });
    }

    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        //Pass events to the port view elements
        systemViewElement.setOnMouseClicked(null);
    }
}
