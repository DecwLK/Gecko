package org.gecko.application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.gecko.io.AutomatonFileSerializer;
import org.gecko.io.FileSerializer;
import org.gecko.io.AutomatonFileParser;
import org.gecko.exceptions.GeckoException;
import org.gecko.exceptions.ModelException;
import org.gecko.io.FileTypes;
import org.gecko.io.ProjectFileParser;
import org.gecko.io.ProjectFileSerializer;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

@Getter
@Setter
public class GeckoIOManager {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static GeckoIOManager instance;

    private GeckoManager geckoManager;
    private File file;
    private Stage stage;

    public static GeckoIOManager getInstance() {
        if (instance == null) {
            instance = new GeckoIOManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(e -> {
            try {
                launchSaveChangesAlert();
            } catch (GeckoException ex) {
                e.consume();
            }
        });
    }

    public void createNewProject() {
        File newFile = saveFileChooser(FileTypes.JSON);
        if (newFile != null) {
            file = newFile;
            Gecko newGecko;
            try {
                newGecko = new Gecko();
            } catch (ModelException e) {
                geckoManager.getGecko().getViewModel().getActionManager().showExceptionAlert(e.getMessage());
                return;
            }
            geckoManager.setGecko(newGecko);
            saveGeckoProject(file);
        }
    }

    public void loadGeckoProject() {
        File fileToLoad = openFileChooser(FileTypes.JSON);
        if (fileToLoad == null) {
            return;
        }
        ProjectFileParser projectFileParser = new ProjectFileParser();
        GeckoViewModel gvm;
        try {
            gvm = projectFileParser.parse(fileToLoad);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                "Corrupted file. Could not read project from " + fileToLoad.getPath() + ".", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Gecko newGecko = new Gecko(gvm);
        geckoManager.setGecko(newGecko);
        file = fileToLoad;
        List<PositionableViewModelElement<?>> generatedViewModelElements =
            projectFileParser.getGeneratedViewModelElements();
        if (generatedViewModelElements != null && !generatedViewModelElements.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Loaded file contains elements with missing attributes. "
                    + "Do you want to keep those elements with default attributes?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult().equals(ButtonType.YES)) {
                return;
            }
            for (PositionableViewModelElement<?> viewModelElement : generatedViewModelElements) {
                geckoManager.getGecko().getViewModel().deleteViewModelElement(viewModelElement);
            }
        }
    }

    public void importAutomatonFile(File file) {
        AutomatonFileParser automatonFileParser = new AutomatonFileParser();
        GeckoViewModel gvm;
        try {
            gvm = automatonFileParser.parse(file);
        } catch (IOException e) {
            String message =
                "Could not read file: %s.%s%s".formatted(file.getPath(), System.lineSeparator(),
                    e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Gecko newGecko = new Gecko(gvm);
        geckoManager.setGecko(newGecko);
    }

    public void saveGeckoProject(File file) {
        ProjectFileSerializer projectFileSerializer = new ProjectFileSerializer(geckoManager.getGecko().getViewModel());
        try {
            projectFileSerializer.writeToFile(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Designated file could not be created.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void exportAutomatonFile(File file) {
        FileSerializer fileSerializer = new AutomatonFileSerializer(geckoManager.getGecko().getModel());
        try {
            fileSerializer.writeToFile(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Designated file could not be created.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public File openFileChooser(FileTypes fileType) {
        FileChooser fileChooser = getNewFileChooser(fileType);
        return fileChooser.showOpenDialog(stage);
    }

    public File saveFileChooser(FileTypes fileType) {
        FileChooser fileChooser = getNewFileChooser(fileType);
        File result = fileChooser.showSaveDialog(stage);
        if (result == null) {
            return null;
        }
        if (!result.getName().endsWith("." + fileType.getFileExtension())) {
            result = new File(result.getPath() + "." + fileType.getFileExtension());
        }
        return result;
    }

    private FileChooser getNewFileChooser(FileTypes fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter(fileType.getFileDescription(), fileType.getFileNameRegex()));
        return fileChooser;
    }

    private void launchSaveChangesAlert() throws GeckoException {
        Alert saveChangesAlert
            = new Alert(Alert.AlertType.NONE, "Do you want to save changes?", ButtonType.YES, ButtonType.NO);
        saveChangesAlert.setTitle("Confirm Exit");
        saveChangesAlert.showAndWait();

        if (saveChangesAlert.getResult().equals(ButtonType.NO)) {
            return;
        }

        if (file == null) {
            File fileToSaveTo = saveFileChooser(FileTypes.JSON);
            if (fileToSaveTo == null) {
                throw new GeckoException("No file chosen.");
            }
            file = fileToSaveTo;
        }
        saveGeckoProject(file);
    }
}
