package org.gecko.view.inspector.element.combobox;

import javafx.scene.control.ComboBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.PortViewModel;

public class InspectorVisibilityComboBox extends ComboBox<Visibility> implements InspectorElement<ComboBox<Visibility>> {
    public InspectorVisibilityComboBox(ActionManager actionManager, PortViewModel viewModel) {
        getItems().setAll(Visibility.values());
        setValue(viewModel.getVisibility());
        valueProperty().addListener((observable, oldValue, newValue) -> {
            Action changeVisibilityAction =
                actionManager.getActionFactory().createChangeVisibilityPortViewModelAction(viewModel, valueProperty().get());
            actionManager.run(changeVisibilityAction);
        });
    }

    @Override
    public ComboBox<Visibility> getControl() {
        return this;
    }
}
