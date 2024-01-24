package org.gecko.tools;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;

public class CursorTool extends Tool {

    private static final String NAME = "Cursor Tool";
    private static final String ICON_STYLE_NAME = "cursor-icon";

    public CursorTool(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconStyleName() {
        return ICON_STYLE_NAME;
    }

    @Override
    public void visitView(ScrollPane view) {
        super.visitView(view);
        view.setCursor(Cursor.DEFAULT);
        view.setPannable(false);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        stateViewElement.setOnMouseClicked(selectElement(stateViewElement));
    }

    @Override
    public void visit(SystemViewElement systemViewElement) {
        super.visit(systemViewElement);
        systemViewElement.setOnMouseClicked(selectElement(systemViewElement));
    }

    @Override
    public void visit(EdgeViewElement edgeViewElement) {
        super.visit(edgeViewElement);
        edgeViewElement.setOnMouseClicked(selectElement(edgeViewElement));
    }

    @Override
    public void visit(VariableBlockViewElement variableBlockViewElement) {
        super.visit(variableBlockViewElement);
        variableBlockViewElement.setOnMouseClicked(selectElement(variableBlockViewElement));
    }

    @Override
    public void visit(RegionViewElement regionViewElement) {
        super.visit(regionViewElement);
        regionViewElement.setOnMouseClicked(selectElement(regionViewElement));
    }

    @Override
    public void visit(SystemConnectionViewElement systemConnectionViewElement) {
        super.visit(systemConnectionViewElement);
        systemConnectionViewElement.setOnMouseClicked(selectElement(systemConnectionViewElement));
    }

    private EventHandler<MouseEvent>  selectElement(ViewElement<?> viewElement) {
        return event -> {
            Action selectAction = actionManager.getActionFactory().createSelectAction(viewElement.getTarget(), !event.isControlDown());
            actionManager.run(selectAction);
        };
    }

}
