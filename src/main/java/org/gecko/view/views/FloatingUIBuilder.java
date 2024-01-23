package org.gecko.view.views;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;

public class FloatingUIBuilder {

    private static final double ZOOM_SCALE_STEP = 0.1;

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
            actionManager.run(actionManager.getActionFactory().createZoomAction(new Point2D(0, 0), ZOOM_SCALE_STEP));
        });

        Label zoomLabel = new Label();
        //Bindings.bindBidirectional(zoomLabel.textProperty(), editorViewModel.getZoomScaleProperty(), new DoubleStringConverter());
        zoomLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            editorViewModel.zoomIn(new Point2D(0, 0), ZOOM_SCALE_STEP);
        });

        Button zoomOutButton = new Button();
        zoomOutButton.setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createZoomAction(new Point2D(0, 0), -ZOOM_SCALE_STEP));
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
        HBox viewSwitchButtons = new HBox();

        Button switchViewButton = new Button("Switch View");
        switchViewButton.setOnAction(event -> {
            actionManager.run(
                actionManager.getActionFactory().createViewSwitchAction(editorViewModel.getCurrentSystem(), !editorViewModel.isAutomatonEditor()));
        });

        viewSwitchButtons.getChildren().add(switchViewButton);

        if (editorViewModel.getParentSystem() != null) {
            Button parentSystemSwitchButton = new Button("Switch Parent View: " + editorViewModel.getParentSystem().getName());
            parentSystemSwitchButton.setOnAction(event -> {
                actionManager.run(
                    actionManager.getActionFactory().createViewSwitchAction(editorViewModel.getParentSystem(), editorViewModel.isAutomatonEditor()));
            });

            viewSwitchButtons.getChildren().add(parentSystemSwitchButton);
        }


        return viewSwitchButtons;
    }
}
