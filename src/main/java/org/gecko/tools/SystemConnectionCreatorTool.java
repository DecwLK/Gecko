package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.PortViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.viewmodel.PortViewModel;

public class SystemConnectionCreatorTool extends Tool {

    private PortViewElement firstPortViewElement;

    public SystemConnectionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.CONNECTION_CREATOR);
        firstPortViewElement = null;
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
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

    private void setPortViewModel(PortViewModel portViewModel) {

    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        super.visit(variableBlockViewElement);
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        //Pass events to the port view elements
        systemViewElement.setOnMouseClicked(null);
    }
}
