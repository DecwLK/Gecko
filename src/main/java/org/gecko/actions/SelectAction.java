package org.gecko.actions;

import java.util.ArrayList;
import java.util.List;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class SelectAction extends Action {
    private final EditorViewModel editorViewModel;
    private List<PositionableViewModelElement<?>> selectedElements;
    private final List<PositionableViewModelElement<?>> elementsToSelect;

    SelectAction(EditorViewModel editorViewModel, PositionableViewModelElement<?> element, boolean newSelection) {
        this.editorViewModel = editorViewModel;
        this.elementsToSelect = new ArrayList<>();
        this.elementsToSelect.add(element);
        if (newSelection) {
            this.selectedElements = new ArrayList<>();
        }
    }

    SelectAction(EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements, boolean newSelection) {
        this.editorViewModel = editorViewModel;
        this.elementsToSelect = new ArrayList<>();
        this.elementsToSelect.addAll(elements);
        if (newSelection) {
            this.selectedElements = new ArrayList<>();
        }
    }

    @Override
    void run() {
        this.selectedElements.addAll(this.elementsToSelect);
        this.editorViewModel.getSelectionManager().select(this.selectedElements);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
