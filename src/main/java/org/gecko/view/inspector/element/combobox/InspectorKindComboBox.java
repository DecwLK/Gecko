package org.gecko.view.inspector.element.combobox;

import javafx.scene.control.ComboBox;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.model.Kind;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorKindComboBox extends ComboBox<Kind> implements InspectorElement<ComboBox<Kind>> {

    public InspectorKindComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        getItems().setAll(Kind.values());
        setValue(viewModel.getKind());
        valueProperty().addListener((observable, oldValue, newValue) -> {
            Action changeKindAction = actionManager.getActionFactory().createChangeKindAction(viewModel, valueProperty().get());
            actionManager.run(changeKindAction);
        });
    }

    @Override
    public ComboBox<Kind> getControl() {
        return this;
    }
}
