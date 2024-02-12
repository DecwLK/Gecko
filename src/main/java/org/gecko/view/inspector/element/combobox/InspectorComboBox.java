package org.gecko.view.inspector.element.combobox;

import java.util.List;
import javafx.beans.property.Property;
import javafx.scene.control.ComboBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;

/**
 * An abstract representation of a {@link ComboBox} implementing the {@link InspectorElement} interface.
 */
public abstract class InspectorComboBox<T> extends ComboBox<T> implements InspectorElement<ComboBox<T>> {

    public InspectorComboBox(ActionManager actionManager, List<T> items, Property<T> property) {
        getItems().setAll(items);
        setValue(property.getValue());
        property.addListener((observable, oldValue, newValue) -> setValue(newValue));
        setOnAction(event -> {
            if (getValue().equals(property.getValue())) {
                return;
            }
            actionManager.run(getAction());
        });
    }

    protected abstract Action getAction();

    @Override
    public ComboBox<T> getControl() {
        return this;
    }
}
