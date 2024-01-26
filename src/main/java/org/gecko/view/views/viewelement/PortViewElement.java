package org.gecko.view.views.viewelement;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;

@Getter
public class PortViewElement extends Pane {

    @Getter(AccessLevel.NONE)
    private final PortViewModel viewModel;
    private final StringProperty nameProperty;
    private final ObjectProperty<Visibility> visibilityProperty;

    public PortViewElement(PortViewModel viewModel) {
        this.viewModel = viewModel;
        this.nameProperty = new SimpleStringProperty(viewModel.getName());
        this.visibilityProperty = new SimpleObjectProperty<>(viewModel.getVisibility());
        bindToViewModel();
    }

    public void setName(String name) {
        nameProperty.setValue(name);
    }

    public String getName() {
        return nameProperty.getValue();
    }

    private void bindToViewModel() {
        viewModel.getNameProperty().bindBidirectional(nameProperty);
        viewModel.getVisibilityProperty().bindBidirectional(visibilityProperty);
    }
}
