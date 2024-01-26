package org.gecko.view.views;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.Inspector;
import org.gecko.viewmodel.EditorViewModel;

public class FloatingUIBuilder {

    private static final double ZOOM_SCALE = 1.1;

    private static final int DEFAULT_BUTTON_SIZE = 30;
    private static final String FLOATING_BUTTON_STYLE_CLASS = "floating-ui-button";
    private static final String ZOOM_IN_STYLE_CLASS = "floating-zoom-in-button";
    private static final String ZOOM_OUT_STYLE_CLASS = "floating-zoom-out-button";

    private final ActionManager actionManager;
    private final EditorViewModel editorViewModel;

    public FloatingUIBuilder(ActionManager actionManager, EditorViewModel editorViewModel) {
        this.actionManager = actionManager;
        this.editorViewModel = editorViewModel;
    }

    public Node buildZoomButtons() {
        VBox zoomButtons = new VBox();

        Button zoomInButton = createStyledButton();
        zoomInButton.getStyleClass().add(ZOOM_IN_STYLE_CLASS);
        zoomInButton.setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createZoomCenterAction(ZOOM_SCALE));
        });

        Label zoomLabel = new Label();
        zoomLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            double zoom = editorViewModel.getZoomScale();
            return String.format("%.0f%%", zoom * 100);
        }, editorViewModel.getZoomScaleProperty()));

        Button zoomOutButton = createStyledButton();
        zoomOutButton.getStyleClass().add(ZOOM_OUT_STYLE_CLASS);
        zoomOutButton.setOnAction(event -> {
            actionManager.run(actionManager.getActionFactory().createZoomCenterAction(1 / ZOOM_SCALE));
        });

        zoomButtons.getChildren().addAll(zoomInButton, zoomLabel, zoomOutButton);
        return zoomButtons;
    }

    public Node buildCurrentViewLabel() {
        Label currentViewLabel = new Label();
        currentViewLabel.textProperty()
            .bind(Bindings.createStringBinding(() -> editorViewModel.getCurrentSystem().getName(),
                currentViewLabel.textProperty()));
        return currentViewLabel;
    }

    public Node buildRegionsLabels() {
        return null;
    }

    public Node buildViewSwitchButtons() {
        HBox viewSwitchButtons = new HBox();
        final String switchToSystemStyleClass = "floating-switch-to-system-button";
        final String switchToAutomatonStyleClass = "floating-switch-to-automaton-button";

        Button switchViewButton = createStyledButton();
        switchViewButton.getStyleClass()
            .add(editorViewModel.isAutomatonEditor() ? switchToSystemStyleClass : switchToAutomatonStyleClass);
        switchViewButton.setOnAction(event -> {
            boolean automatonEditor = editorViewModel.isAutomatonEditor();
            actionManager.run(actionManager.getActionFactory()
                .createViewSwitchAction(editorViewModel.getCurrentSystem(), !automatonEditor));
            switchViewButton.getStyleClass()
                .remove(automatonEditor ? switchToAutomatonStyleClass : switchToSystemStyleClass);
            switchViewButton.getStyleClass()
                .add(automatonEditor ? switchToSystemStyleClass : switchToAutomatonStyleClass);
        });

        viewSwitchButtons.getChildren().add(switchViewButton);

        if (editorViewModel.getParentSystem() != null) {
            Button parentSystemSwitchButton = createStyledButton();
            parentSystemSwitchButton.getStyleClass().add("floating-parent-system-switch-button");
            parentSystemSwitchButton.setOnAction(event -> actionManager.run(actionManager.getActionFactory()
                .createViewSwitchAction(editorViewModel.getParentSystem(), editorViewModel.isAutomatonEditor())));

            viewSwitchButtons.getChildren().add(parentSystemSwitchButton);
        }


        return viewSwitchButtons;
    }

    public Button buildUncollapseInspectorButton(Inspector inspector) {
        Button uncollapseInspectorButton = createStyledButton();
        uncollapseInspectorButton.getStyleClass().add("floating-uncollapse-inspector-button");
        uncollapseInspectorButton.setOnAction(event -> {
            inspector.toggleCollapse();
        });
        return uncollapseInspectorButton;
    }

    private Button createStyledButton() {
        Button button = new Button();
        button.getStyleClass().add(FLOATING_BUTTON_STYLE_CLASS);
        button.setPrefSize(DEFAULT_BUTTON_SIZE, DEFAULT_BUTTON_SIZE);
        return button;
    }
}
