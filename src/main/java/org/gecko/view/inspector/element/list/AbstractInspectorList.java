package org.gecko.view.inspector.element.list;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.gecko.view.inspector.element.InspectorElement;

/**
 * An abstract representation of a {@link ListView} encapsulating a type of {@link InspectorElement}.
 */
@Getter
public abstract class AbstractInspectorList<T extends InspectorElement<? extends Node>> extends VBox implements InspectorElement<VBox> {

    protected ObservableList<T> items;

    private static final String STYLE_CLASS = "inspector-list";

    protected AbstractInspectorList() {
        getStyleClass().add(STYLE_CLASS);
        setSpacing(5);
        items = FXCollections.observableArrayList();

        items.addListener((ListChangeListener.Change<? extends T> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<Node> added = change.getAddedSubList().stream().map(InspectorElement::getControl).map(element -> (Node) element).toList();
                    getChildren().addAll(added);
                    added.forEach(node -> VBox.setVgrow(node, Priority.ALWAYS));
                } else if (change.wasRemoved()) {
                    List<Node> removed = change.getRemoved().stream().map(InspectorElement::getControl).map(element -> (Node) element).toList();
                    getChildren().removeAll(removed);
                }
            }
        });
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
