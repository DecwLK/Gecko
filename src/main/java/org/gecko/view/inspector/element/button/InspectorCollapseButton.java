package org.gecko.view.inspector.element.button;

import org.gecko.view.views.EditorView;

public class InspectorCollapseButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-collapse-button";

    public InspectorCollapseButton(EditorView editorView) {
        getStyleClass().add(ICON_STYLE_NAME);
        setOnAction(event -> editorView.toggleInspector());
    }
}
