package org.gecko.view.inspector.element.button;

import org.gecko.view.views.EditorView;

public class InspectorCollapseButton extends AbstractInspectorButton {
    public InspectorCollapseButton(EditorView editorView) {
        setOnAction(event -> {
            editorView.toggleInspector();
        });
    }
}
