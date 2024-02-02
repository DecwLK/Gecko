package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class CopyPositionableViewModelElementAction extends Action {
    EditorViewModel currentEditor;
    CopiedElementsContainer elementsToCopy;

    CopyPositionableViewModelElementAction(EditorViewModel editorViewModel, CopiedElementsContainer elementsToCopy) {
        this.currentEditor = editorViewModel;
        this.elementsToCopy = elementsToCopy;
    }

    @Override
    boolean run() throws GeckoException {
        CopiedElementsContainer copiedElementsContainer = new CopiedElementsContainer(currentEditor.isAutomatonEditor(),
            currentEditor.getContainedPositionableViewModelElementsProperty().stream()
                .filter(element -> element instanceof SystemViewModel).toList(),
            currentEditor.getSelectionManager().getCurrentSelection());

        elementsToCopy.setAutomatonCopy(copiedElementsContainer.isAutomatonCopy());
        elementsToCopy.setContainedSystemViewModels(copiedElementsContainer.getContainedSystemViewModels());
        elementsToCopy.setCopiedSystems(copiedElementsContainer.getCopiedSystems());
        elementsToCopy.setCopiedPorts(copiedElementsContainer.getCopiedPorts());
        elementsToCopy.setCopiedSystemConnections(copiedElementsContainer.getCopiedSystemConnections());
        elementsToCopy.setCopiedStates(copiedElementsContainer.getCopiedStates());
        elementsToCopy.setCopiedRegions(copiedElementsContainer.getCopiedRegions());
        elementsToCopy.setCopiedEdges(copiedElementsContainer.getCopiedEdges());

        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
