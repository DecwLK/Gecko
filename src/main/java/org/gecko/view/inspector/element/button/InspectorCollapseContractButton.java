package org.gecko.view.inspector.element.button;

import org.gecko.view.inspector.element.textfield.InspectorContractField;

import java.util.List;

public class InspectorCollapseContractButton extends AbstractInspectorButton {
    public InspectorCollapseContractButton(List<InspectorContractField> fields) {
        setOnAction(event -> {
            fields.forEach(InspectorContractField::toggleExpand);
        });
    }
}
