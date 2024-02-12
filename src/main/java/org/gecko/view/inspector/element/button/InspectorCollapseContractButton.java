package org.gecko.view.inspector.element.button;

import java.util.List;
import org.gecko.view.inspector.element.textfield.InspectorAreaField;

/**
 * Represents a type of {@link AbstractInspectorButton} used for expanding or collapsing a list of
 * {@link org.gecko.view.inspector.element.container.InspectorContractItem InspectorContractItem}.
 */
public class InspectorCollapseContractButton extends AbstractInspectorButton {
    private static final String ICON_STYLE_NAME = "inspector-contract-expand-button";
    private static final String ICON_STYLE_NAME_EXPANDED = "inspector-contract-expanded-button";
    private static final int DEFAULT_SIZE = 18;

    private boolean expanded = false;

    public InspectorCollapseContractButton(List<InspectorAreaField> fields) {
        getStyleClass().add(ICON_STYLE_NAME);
        setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE);

        setOnAction(event -> {
            fields.forEach(InspectorAreaField::toggleExpand);
            expanded = !expanded;

            if (expanded) {
                getStyleClass().remove(ICON_STYLE_NAME);
                getStyleClass().add(ICON_STYLE_NAME_EXPANDED);
            } else {
                getStyleClass().remove(ICON_STYLE_NAME_EXPANDED);
                getStyleClass().add(ICON_STYLE_NAME);
            }
        });
    }
}
