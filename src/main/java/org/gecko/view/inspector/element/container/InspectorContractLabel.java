package org.gecko.view.inspector.element.container;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.InspectorAddContractButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractLabel extends HBox implements InspectorElement<HBox> {
    public InspectorContractLabel(ActionManager actionManager, StateViewModel viewModel) {
        InspectorLabel label = new InspectorLabel("L:Contracts");
        InspectorAddContractButton button = new InspectorAddContractButton(actionManager, viewModel);
        HBox.setHgrow(label, Priority.ALWAYS);
        HBox.setHgrow(button, Priority.ALWAYS);
        getChildren().addAll(label, button);
    }

    @Override
    public HBox getControl() {
        return this;
    }
}
