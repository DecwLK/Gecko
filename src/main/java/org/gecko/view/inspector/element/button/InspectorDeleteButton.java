package org.gecko.view.inspector.element.button;

import javafx.scene.paint.Color;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.PositionableViewModelElement;

public class InspectorDeleteButton extends AbstractInspectorButton {

    private static final int WIDTH = 300;
    private static final Color COLOR = Color.MISTYROSE;

    public InspectorDeleteButton(ActionManager actionManager, PositionableViewModelElement<?> elementToRemove) {
        setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createDeletePositionableViewModelElementAction(elementToRemove));
        });
        setText("L:Delete");
        setPrefWidth(WIDTH);
        setStyle("-fx-background-color: " + COLOR.toString().replace("0x", "#")); //TODO correct?
    }
}
