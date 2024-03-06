package org.gecko.view.inspector.element.textfield;

import javafx.beans.binding.Bindings;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.SystemViewModel;

public class InspectorCodeSystemField extends InspectorAreaField {
    private static final int HEIGHT_THRESHOLD = 90;
    private final ActionManager actionManager;
    private final SystemViewModel systemViewModel;

    public InspectorCodeSystemField(ActionManager actionManager, SystemViewModel systemViewModel) {
        super(actionManager, systemViewModel.getCodeProperty(), true);
        this.actionManager = actionManager;
        this.systemViewModel = systemViewModel;

        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> getFont().getSize() * getParagraphs().size() + HEIGHT_THRESHOLD,
                fontProperty(), textProperty()));
    }

    @Override
    protected Action getAction() {
        return actionManager.getActionFactory().createChangeCodeSystemViewModelAction(systemViewModel, getText());
    }
}
