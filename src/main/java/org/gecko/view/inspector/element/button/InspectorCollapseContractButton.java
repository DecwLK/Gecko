package org.gecko.view.inspector.element.button;

import java.util.List;

import org.gecko.view.inspector.element.textfield.InspectorContractField;

public class InspectorCollapseContractButton extends AbstractInspectorButton {
    public InspectorCollapseContractButton(List<InspectorContractField> fields) {
        setOnAction(event -> {
            fields.forEach(InspectorContractField::toggleExpand);
        });
    }
}
