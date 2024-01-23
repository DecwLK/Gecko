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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class StateViewElement extends BlockViewElement implements ViewElement<StateViewModel> {

    @Getter(AccessLevel.NONE)
    private final StateViewModel stateViewModel;
    private final StringProperty nameProperty;
    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    public StateViewElement(StateViewModel stateViewModel) {
        super(stateViewModel);
        this.nameProperty = new SimpleStringProperty();
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>();
        this.stateViewModel = stateViewModel;
        bindViewModel();
        constructVisualization();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {

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
        prefHeightProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getSize().getY(), stateViewModel.getSizeProperty()));
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    private void constructVisualization() {
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setFill(Color.LIGHTBLUE);
        GridPane gridPane = new GridPane();
        Label name = new Label("State: " + stateViewModel.getName());
        Bindings.createStringBinding(() -> "State: " + stateViewModel.getName(), stateViewModel.getNameProperty());
        Label contracts = new Label("Contracts: " + stateViewModel.getContractsProperty().size());
        Bindings.createStringBinding(() -> "Contracts: " + stateViewModel.getContractsProperty().size(), stateViewModel.getContractsProperty());
        gridPane.add(name, 0, 0);
        gridPane.add(contracts, 0, 1);
        getChildren().addAll(background, gridPane);
    }
}
