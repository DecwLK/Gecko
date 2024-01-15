package org.gecko.view.inspector.builder;

import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorDeleteButton;
import org.gecko.view.inspector.element.textfield.InspectorTextField;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.Renamable;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractInspectorBuilder<T extends PositionableViewModelElement<?>> {
    @Getter
    private final T viewModel;
    @Getter
    private final ActionManager actionManager;
    private final List<InspectorElement<?>> inspectorElements;

    protected AbstractInspectorBuilder(ActionManager actionManager, T viewModel) {
        this.actionManager = actionManager;
        this.viewModel = viewModel;

        this.inspectorElements = new LinkedList<>();

        // Name field if applicable
        try {
            addInspectorElement(new InspectorTextField((Renamable) viewModel));
            addInspectorElement(new InspectorSeparator());
        } catch (ClassCastException e) {
            // Do nothing
        }
    }

    protected void addInspectorElement(InspectorElement<?> element) {
        inspectorElements.add(element);
    }

    public Inspector build(EditorView editorView, EditorViewModel editorViewModel) {
        // Element delete button
        inspectorElements.add(new InspectorDeleteButton(actionManager, viewModel));

        return new Inspector(inspectorElements, actionManager, editorView, editorViewModel);
    }
}
