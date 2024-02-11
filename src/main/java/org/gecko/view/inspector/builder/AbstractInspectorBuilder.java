package org.gecko.view.inspector.builder;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorDeleteButton;
import org.gecko.view.inspector.element.textfield.InspectorRenameField;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.Renamable;

/** An abstract representation of a generic builder for an {@link Inspector} that corresponds to a {@link PositionableViewModelElement}. Holds a reference to the {@link ActionManager}, which allows for operations to be run from the inspector, and a list of {@link InspectorElement}s, which are added to a built {@link Inspector}. */
public abstract class AbstractInspectorBuilder<T extends PositionableViewModelElement<?>> {
    @Getter
    private final T viewModel;
    @Getter
    private final ActionManager actionManager;
    private final List<InspectorElement<?>> inspectorElements;

    protected AbstractInspectorBuilder(ActionManager actionManager, T viewModel) {
        this.actionManager = actionManager;
        this.viewModel = viewModel;

        this.inspectorElements = new ArrayList<>();

        // Name field if applicable
        try {
            addInspectorElement(new InspectorRenameField(actionManager, (Renamable) viewModel));
            addInspectorElement(new InspectorSeparator());
        } catch (ClassCastException e) {
            // Do nothing
        }
    }

    protected void addInspectorElement(InspectorElement<?> element) {
        inspectorElements.add(element);
    }

    protected void removeInspectorElement(InspectorElement<?> element) {
        inspectorElements.remove(element);
    }

    public Inspector build() {
        // Element delete button
        inspectorElements.add(new InspectorDeleteButton(actionManager, viewModel));
        return new Inspector(inspectorElements, actionManager);
    }
}
