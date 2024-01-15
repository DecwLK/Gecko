package org.gecko.view.views;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;

public class FloatingUIBuilder {

    private static final double ZOOM_SCALE_STEP = 0.1;
    private static final String ZOOM_LABEL_FORMAT = "%.2f";

    private final ActionManager actionManager;
    private final EditorViewModel editorViewModel;

    public FloatingUIBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorViewModel = editorViewModel;
    }

    public Node buildZoomButtons() {
        VBox zoomButtons = new VBox();

        Button zoomInButton = new Button();
        zoomInButton.setOnAction(event -> {
            editorViewModel.setZoomScale(editorViewModel.getZoomScale() + ZOOM_SCALE_STEP);
        });

        Label zoomLabel = new Label();
        Bindings.bindBidirectional(zoomLabel.textProperty(), editorViewModel.getZoomScaleProperty(), new DoubleStringConverter());

        Button zoomOutButton = new Button();
        zoomOutButton.setOnAction(event -> {
            editorViewModel.setZoomScale(editorViewModel.getZoomScale() - ZOOM_SCALE_STEP);
        });

        zoomButtons.getChildren().addAll(zoomInButton, zoomLabel, zoomOutButton);
        return zoomButtons;
    }

    public Node buildCurrentViewLabel() {
        Label currentViewLabel = new Label();
        currentViewLabel.textProperty()
                        .bind(Bindings.createStringBinding(() -> editorViewModel.getCurrentSystem().getName(), currentViewLabel.textProperty()));


        return currentViewLabel;
    }

    public Node buildRegionsLabels() {
        return null;
    }

    public Node buildViewSwitchButton() {
        Button switchViewButton = new Button();
        switchViewButton.setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createViewSwitchAction(editorViewModel.getParentSystem(), editorViewModel.isAutomatonEditor()));
        });

        return switchViewButton;
    }
}
