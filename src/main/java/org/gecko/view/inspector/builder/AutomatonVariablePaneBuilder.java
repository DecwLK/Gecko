package org.gecko.view.inspector.builder;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.container.InspectorVariableLabel;
import org.gecko.view.inspector.element.list.InspectorVariableList;
import org.gecko.viewmodel.SystemViewModel;

public class AutomatonVariablePaneBuilder {

    private static final int VARIABLE_PANE_WIDTH = 320;
    private static final int LIST_HEIGHT = 100;
    private static final int ELEMENT_SPACING = 10;

    private final ScrollPane scrollPane;

    public AutomatonVariablePaneBuilder(ActionManager actionManager, SystemViewModel systemViewModel) {
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(VARIABLE_PANE_WIDTH);

        VBox content = new VBox();
        InspectorVariableLabel inputLabel =
            new InspectorVariableLabel(actionManager, systemViewModel, Visibility.INPUT);
        InspectorVariableList inputList = new InspectorVariableList(actionManager, systemViewModel, Visibility.INPUT);
        InspectorVariableLabel outputLabel =
            new InspectorVariableLabel(actionManager, systemViewModel, Visibility.OUTPUT);
        InspectorVariableList outputList = new InspectorVariableList(actionManager, systemViewModel, Visibility.OUTPUT);
        inputList.getControl().setPrefHeight(LIST_HEIGHT);
        outputList.getControl().setPrefHeight(LIST_HEIGHT);

        content.getChildren()
            .addAll(inputLabel.getControl(), inputList.getControl(), outputLabel.getControl(), outputList.getControl());
        content.setSpacing(ELEMENT_SPACING);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(ELEMENT_SPACING / 2.0));
        scrollPane.setContent(content);
    }

    public ScrollPane build() {
        return scrollPane;
    }
}
