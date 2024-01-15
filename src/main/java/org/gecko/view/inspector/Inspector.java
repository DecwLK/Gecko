package org.gecko.view.inspector;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;

import javafx.scene.control.ScrollPane;
import org.gecko.view.inspector.element.button.InspectorCollapseButton;
import org.gecko.view.inspector.element.button.InspectorSelectionBackwardButton;
import org.gecko.view.inspector.element.button.InspectorSelectionForwardButton;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;

import java.util.List;

public class Inspector extends ScrollPane {

    private boolean isCollapsed = false;

    public Inspector(List<InspectorElement<?>> elements, ActionManager actionManager, EditorView editorView, EditorViewModel editorViewModel) {
        VBox vBox = new VBox();

        // Inspector decorations
        HBox inspectorDecorations = new HBox();

        // Selection forward/backward buttons
        HBox selectionButtons = new HBox();
        selectionButtons.getChildren()
                        .addAll(new InspectorSelectionBackwardButton(actionManager).getControl(),
                            new InspectorSelectionForwardButton(actionManager).getControl());
        inspectorDecorations.getChildren().addAll(selectionButtons,
            // Collapse button
            new InspectorCollapseButton(editorView));

        vBox.getChildren().add(inspectorDecorations);

        for (InspectorElement<?> element : elements) {
            vBox.getChildren().add(element.getControl());
        }

        setContent(vBox);
        setFitToWidth(true);
    }

    public void toggleCollapse() {
        isCollapsed = !isCollapsed;

        if (isCollapsed) {
            setPrefWidth(0);
        } else {
            setPrefWidth(300);
        }
    }

    public Node getView() {
        return this;
    }
}
