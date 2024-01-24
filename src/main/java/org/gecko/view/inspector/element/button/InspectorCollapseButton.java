package org.gecko.view.inspector.element.button;

import org.gecko.view.views.EditorView;

public class InspectorCollapseButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-collapse-button";
    private static final String COLLAPSED_ICON_STYLE_NAME = "inspector-uncollapse-button";

    public InspectorCollapseButton(EditorView editorView) {
        getStyleClass().add(ICON_STYLE_NAME);

        setOnAction(event -> {
            boolean collapsed = editorView.toggleInspector();

            if (collapsed) {
                getStyleClass().remove(ICON_STYLE_NAME);
                getStyleClass().add(COLLAPSED_ICON_STYLE_NAME);
            } else {
                getStyleClass().remove(COLLAPSED_ICON_STYLE_NAME);
                getStyleClass().add(ICON_STYLE_NAME);
            }
        });
    }
}
