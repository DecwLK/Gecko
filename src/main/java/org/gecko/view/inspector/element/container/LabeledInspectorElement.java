package org.gecko.view.inspector.element.container;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.label.InspectorLabel;

/**
 * Represents a type of {@link HBox} implementing the {@link InspectorElement} interface. Contains an
 * {@link InspectorLabel} and the labeled {@link InspectorElement}.
 */
public class LabeledInspectorElement extends HBox implements InspectorElement<HBox> {

    public LabeledInspectorElement(InspectorLabel label, InspectorElement<?> element) {
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox labelBox = new VBox();
        labelBox.getChildren().add(label.getControl());
        labelBox.setAlignment(Pos.CENTER);
        getChildren().addAll(labelBox, spacer, element.getControl());
    }

    @Override
    public HBox getControl() {
        return this;
    }
}
