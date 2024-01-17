package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class StateViewElement extends VBox implements ViewElement<StateViewModel> {

    private final StateViewModel stateViewModel;
    private final StringProperty nameProperty;
    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    public StateViewElement(StateViewModel stateViewModel) {
        this.nameProperty = new SimpleStringProperty();
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>();
        this.stateViewModel = stateViewModel;
        bindViewModel();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public StateViewModel getTarget() {
        return stateViewModel;
    }

    @Override
    public Point2D getPosition() {
        return stateViewModel.getPosition();
    }

    private void bindViewModel() {
        nameProperty.bind(stateViewModel.getNameProperty());
        isStartStateProperty.bind(stateViewModel.getIsStartStateProperty());
        contractsProperty.bind(stateViewModel.getContractsProperty());
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getPosition().getX(), stateViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getPosition().getY(), stateViewModel.getPositionProperty()));
        prefWidthProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getSize().getX(), stateViewModel.getSizeProperty()));
        //TODO temp
        prefHeightProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getSize().getY(), stateViewModel.getSizeProperty()));
        TextInputControl textField = new TextField();
        textField.textProperty().bindBidirectional(nameProperty);
        getChildren().add(textField);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
