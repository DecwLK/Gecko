package org.gecko.view.inspector.element.textfield;

import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorPriorityField extends Spinner<Integer> implements InspectorElement<Spinner<Integer>> {
    private static final int MAX_PRIORITY = 100;
    private static final int MIN_PRIORITY = -100;
    private final ActionManager actionManager;
    private final EdgeViewModel edgeViewModel;

    public InspectorPriorityField(ActionManager actionManager, EdgeViewModel edgeViewModel) {
        super(MIN_PRIORITY, MAX_PRIORITY, edgeViewModel.getPriority());
        this.actionManager = actionManager;
        this.edgeViewModel = edgeViewModel;

        setEditable(true);
        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*")) {
                getEditor().setText(oldValue);
            }
        });
        edgeViewModel.getPriorityProperty()
            .addListener(event -> getValueFactory().setValue(edgeViewModel.getPriority()));


        setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) {
                return;
            }
            getParent().requestFocus();
            if (getEditor().getText().isEmpty()) {
                getEditor().setText(Integer.toString(edgeViewModel.getPriority()));
                commitValue();
                return;
            }
            if (getValue().equals(edgeViewModel.getPriority())) {
                return;
            }
            actionManager.run(
                actionManager.getActionFactory().createModifyEdgeViewModelPriorityAction(edgeViewModel, getValue()));
        });
    }

    @Override
    public void decrement(int steps) {
        super.decrement(steps);
        actionManager.run(
            actionManager.getActionFactory().createModifyEdgeViewModelPriorityAction(edgeViewModel, getValue()));
    }

    @Override
    public void increment(int steps) {
        super.increment(steps);
        actionManager.run(
            actionManager.getActionFactory().createModifyEdgeViewModelPriorityAction(edgeViewModel, getValue()));
    }

    @Override
    public Spinner<Integer> getControl() {
        return this;
    }
}
