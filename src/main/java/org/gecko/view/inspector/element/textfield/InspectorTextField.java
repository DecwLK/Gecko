package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.TextField;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.Renamable;

public class InspectorTextField extends TextField implements InspectorElement<TextField> {

    public InspectorTextField(Renamable renamable) {
        setText(renamable.getName());

        textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            renamable.setName(newValue);
                        });
    }

    @Override
    public TextField getControl() {
        return this;
    }
}
