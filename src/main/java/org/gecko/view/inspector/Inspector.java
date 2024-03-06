package org.gecko.view.inspector;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.AbstractInspectorButton;
import org.gecko.view.inspector.element.button.InspectorSelectionBackwardButton;
import org.gecko.view.inspector.element.button.InspectorSelectionForwardButton;

/**
 * Represents a {@link ScrollPane} that encapsulates {@link InspectorElement}s, which allow the properties of Gecko
 * elements to be modified from the view.
 */
public class Inspector extends ScrollPane {

    private static final int INSPECTOR_ELEMENT_SPACING = 10;
    private static final int INSPECTOR_WIDTH = 320;
    private static final String INSPECTOR_STYLE_NAME = "inspector";

    public Inspector(
        List<InspectorElement<?>> elements, ActionManager actionManager) {
        VBox vBox = new VBox();
        setPrefWidth(INSPECTOR_WIDTH);

        // Inspector decorations
        HBox inspectorDecorations = new HBox();

        // Selection forward/backward buttons
        HBox selectionButtons = new HBox();
        AbstractInspectorButton selectionBackwardButton = new InspectorSelectionBackwardButton(actionManager);
        AbstractInspectorButton selectionForwardButton = new InspectorSelectionForwardButton(actionManager);
        selectionButtons.getChildren().addAll(selectionBackwardButton, selectionForwardButton);

        HBox.setHgrow(selectionButtons, Priority.ALWAYS);
        inspectorDecorations.getChildren().addAll(selectionButtons);

        vBox.getChildren().add(inspectorDecorations);

        for (InspectorElement<?> element : elements) {
            vBox.getChildren().add(element.getControl());
        }
        setPadding(new Insets(INSPECTOR_ELEMENT_SPACING / 2.0));
        vBox.setSpacing(INSPECTOR_ELEMENT_SPACING);
        getStyleClass().add(INSPECTOR_STYLE_NAME);
        setContent(vBox);
    }

    public Node getView() {
        return this;
    }

    @Override
    public void requestFocus() {

    }
}
