package org.gecko.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;

public class CopyPositionableViewModelElementAction extends Action {
    CopiedElementsWrapper elementsToCopy;

    CopyPositionableViewModelElementAction(EditorViewModel editorViewModel) {
        elementsToCopy = new CopiedElementsWrapper(editorViewModel.getSelectionManager().getCurrentSelection(),
            editorViewModel);
    }

    @Override
    boolean run() throws GeckoException {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        ObjectMapper objectMapper = new ObjectMapper();
        String elementsToJson = null;
        try {
            elementsToJson = objectMapper.writeValueAsString(elementsToCopy);
        } catch (JsonProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something went wrong. "
                + "Please review the selected elements and try again.", ButtonType.OK);
            alert.showAndWait();
        }

        if (elementsToJson != null) {
            content.putString(elementsToJson);
            systemClipboard.setContent(content);
        }
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
