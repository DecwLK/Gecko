package org.gecko.tools;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.viewmodel.StateViewModel;

/**
 * A concrete representation of an edge-creating-{@link Tool}, utilized for connecting a source- and a
 * destination-{@link StateViewModel} through an {@link org.gecko.viewmodel.EdgeViewModel}. Holds the
 * source-{@link StateViewModel}.
 */
public class EdgeCreatorTool extends Tool {
    private StateViewModel source;

    public EdgeCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.EDGE_CREATOR);
        source = null;
    }

    @Override
    public void visitView(VBox vbox, ScrollPane view, Group worldGroup, Group containerGroup) {
        super.visitView(vbox, view, worldGroup, containerGroup);
        view.setCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void visit(StateViewElement stateViewElement) {
        super.visit(stateViewElement);
        source = null;
        stateViewElement.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (source == null) {
                source = stateViewElement.getTarget();
            } else {
                Action createEdgeAction = actionManager.getActionFactory()
                    .createCreateEdgeViewModelElementAction(source, stateViewElement.getTarget());
                actionManager.run(createEdgeAction);
                source = null;
            }
        });
    }
}
