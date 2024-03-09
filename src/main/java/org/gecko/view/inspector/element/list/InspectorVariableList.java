package org.gecko.view.inspector.element.list;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.gecko.actions.ActionManager;
import org.gecko.model.Visibility;
import org.gecko.view.inspector.element.container.InspectorVariableField;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * A concrete representation of an {@link AbstractInspectorList} encapsulating an {@link InspectorVariableField}.
 */
public class InspectorVariableList extends AbstractInspectorList<InspectorVariableField> {

    private static final int MIN_HEIGHT = 60;

    private final ActionManager actionManager;
    private final SystemViewModel viewModel;
    private final Visibility visibility;

    public InspectorVariableList(ActionManager actionManager, SystemViewModel viewModel, Visibility visibility) {
        super();
        this.actionManager = actionManager;
        this.viewModel = viewModel;
        this.visibility = visibility;

        setMinHeight(MIN_HEIGHT);

        viewModel.getPortsProperty().addListener(this::onPortsListChanged);
        viewModel.getPorts().stream().filter(port -> port.getVisibility() == visibility).forEach(this::addPortItem);
        viewModel.getPorts().forEach(port -> port.getVisibilityProperty().addListener(this::onVisibilityChanged));
    }

    private void onPortsListChanged(ListChangeListener.Change<? extends PortViewModel> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(port -> {
                    port.getVisibilityProperty().addListener(this::onVisibilityChanged);
                    if (port.getVisibility() == visibility) {
                        addPortItem(port);
                    }
                });
            }
            if (change.wasRemoved()) {
                change.getRemoved().forEach(port -> {
                    if (port.getVisibility() == visibility) {
                        removePortItem(port);
                    }
                });
            }
        }
    }

    private void onVisibilityChanged(
        ObservableValue<? extends Visibility> observable, Visibility oldValue, Visibility newValue) {
        //Since the visibility property of a port changed we should always find that port
        PortViewModel port = viewModel.getPorts()
            .stream()
            .filter(p -> p.getVisibilityProperty() == observable)
            .findFirst()
            .orElseThrow();
        if (port.getVisibility() == visibility) {
            addPortItem(port);
        } else {
            removePortItem(port);
        }
    }

    private void addPortItem(PortViewModel port) {
        InspectorVariableField field = new InspectorVariableField(actionManager, port);
        getItems().add(field);
    }

    private void removePortItem(PortViewModel port) {
        getItems().removeIf(field -> field.getViewModel().equals(port));
    }

}
