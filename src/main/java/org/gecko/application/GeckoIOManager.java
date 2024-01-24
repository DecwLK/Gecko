package org.gecko.application;

import java.io.File;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.gecko.exceptions.MissingViewModelElement;
import org.gecko.io.ProjectFileParser;
import org.gecko.io.ProjectFileSerializer;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.GeckoViewModel;

public class GeckoIOManager {
    @Getter
    @Setter
    private GeckoManager geckoManager;

    private static GeckoIOManager instance;
    @Getter
    @Setter
    private Stage stage;

    public static GeckoIOManager getInstance() {
        if (instance == null) {
            instance = new GeckoIOManager();
        }
        return instance;
    }

    public void loadGeckoProject(File file) {
        ProjectFileParser projectFileParser = new ProjectFileParser();
        Pair<GeckoModel, GeckoViewModel> geckoPair = null;
        try {
            geckoPair = projectFileParser.parse(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Corrupted file. Could not read project from " + file.getPath() + ".", ButtonType.OK);
            alert.showAndWait();
        } catch (MissingViewModelElement e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                "Corrupted file. Could not read project from " + file.getPath() + " for following reason: " + e.getMessage(),
                ButtonType.OK); // TODO: Maybe not show exception message?
            alert.showAndWait();
        }

        if (geckoPair != null) {
            geckoManager.setGecko(geckoPair.getKey(), geckoPair.getValue());
        }

    }

    public void importAutomatonFile(File file) {

    }

    public void saveGeckoProject(File file) {
        ProjectFileSerializer projectFileSerializer = new ProjectFileSerializer();
        try {
            projectFileSerializer.createFile(geckoManager.getGecko().getModel(), geckoManager.getGecko().getViewModel(), file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Designated file could not be created.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void exportAutomatonFile(File file) {

    }
}
