package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.PortViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.viewmodel.PortViewModel;

public class SystemConnectionCreatorTool extends Tool {

    private PortViewModel firstPortViewModel;

    public SystemConnectionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.CONNECTION_CREATOR);
        firstPortViewModel = null;
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
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            setPortViewModel(portViewElement.getViewModel());
        });
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        super.visit(variableBlockViewElement);
        variableBlockViewElement.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }
            setPortViewModel(variableBlockViewElement.getTarget());
        });
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        //Pass events to the port view elements
        systemViewElement.setOnMouseClicked(null);
    }

    private void setPortViewModel(PortViewModel portViewModel) {
        if (firstPortViewModel == null || firstPortViewModel.equals(portViewModel)) {
            firstPortViewModel = portViewModel;
        } else {
            Action createAction = actionManager.getActionFactory()
                .createCreateSystemConnectionViewModelElementAction(firstPortViewModel, portViewModel);
            actionManager.run(createAction);
            firstPortViewModel = null;
        }
    }
}
