package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SelectionManager;

/**
 * A concrete representation of an {@link Action} that selects a set of {@link PositionableViewModelElement}s.
 */
public class SelectAction extends Action {
    private final SelectionManager selectionManager;
    private final Set<PositionableViewModelElement<?>> elementsToSelect;
    private final boolean newSelection;

    SelectAction(EditorViewModel editorViewModel, Set<PositionableViewModelElement<?>> elements, boolean newSelection) {
        this.selectionManager = editorViewModel.getSelectionManager();
        this.elementsToSelect = new HashSet<>(elements);
        this.newSelection = newSelection;
    }

    @Override
    boolean run() throws GeckoException {
        if (elementsToSelect.isEmpty()) {
            selectionManager.deselectAll();
        }
        if (!newSelection) {
            elementsToSelect.addAll(selectionManager.getCurrentSelection());
        }
        selectionManager.select(elementsToSelect);
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
