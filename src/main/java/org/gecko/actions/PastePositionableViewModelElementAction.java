package org.gecko.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SystemViewModel;

public class PastePositionableViewModelElementAction extends Action {
    GeckoViewModel geckoViewModel;
    CopiedElementsWrapper elementsToPaste;
    SystemViewModel currentSystem;

    PastePositionableViewModelElementAction(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();

        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOfElements =  systemClipboard.getString();

        elementsToPaste = null;
        try {
            elementsToPaste = objectMapper.readValue(jsonOfElements, CopiedElementsWrapper.class);
        } catch (JsonProcessingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The format of the content to paste is invalid.",
                ButtonType.OK);
            alert.showAndWait();
        }

        if (elementsToPaste == null || elementsToPaste.isWrapperEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Clipboard is empty, so there is nothing to paste.",
                ButtonType.OK);
            alert.showAndWait();
        }
    }

    PastePositionableViewModelElementAction(
        GeckoViewModel geckoViewModel, List<PositionableViewModelElement<?>> elements) {
        this.geckoViewModel = geckoViewModel;
        this.currentSystem = geckoViewModel.getCurrentEditor().getCurrentSystem();
        elementsToPaste = new CopiedElementsWrapper(new HashSet<>(elements), geckoViewModel.getCurrentEditor());
    }

    @Override
    boolean run() throws GeckoException {
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
