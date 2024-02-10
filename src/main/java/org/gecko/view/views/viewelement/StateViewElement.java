package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class StateViewElement extends BlockViewElement implements ViewElement<StateViewModel> {

    private static final int Z_PRIORITY = 30;
    private static final int CORNER_RADIUS = 10;
    private static final int INNER_CORNER_RADIUS = 20;
    private static final int SPACING = 5;
    private static final int MAX_CONTRACT_CNT = 4;

    private static final String STYLE = "state-view-element";
    private static final String INNER_STYLE = "state-inner-view-element";
    private static final String INNER_INNER_STYLE = "state-inner-inner-view-element";

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
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getPosition().getX(),
            stateViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> stateViewModel.getPosition().getY(),
            stateViewModel.getPositionProperty()));
        prefWidthProperty().bind(
            Bindings.createDoubleBinding(() -> stateViewModel.getSize().getX(), stateViewModel.getSizeProperty()));
        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> stateViewModel.getSize().getY(), stateViewModel.getSizeProperty()));
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void constructVisualization() {
        getStyleClass().add(STYLE);

        VBox contents = new VBox();
        contents.prefWidthProperty().bind(prefWidthProperty());
        contents.prefHeightProperty().bind(prefHeightProperty());
        contents.setPadding(new Insets(SPACING));

        // State name:
        Pane stateName = new Pane();
        colorStateName(stateName);
        // Color the state name according to its type
        isStartStateProperty.addListener((observable, oldValue, newValue) -> {
            colorStateName(stateName);
        });
        stateName.getStyleClass().add(INNER_STYLE);

        Label name = new Label(stateViewModel.getName());
        name.textProperty().bind(stateViewModel.getNameProperty());

        // center the label
        name.layoutXProperty()
            .bind(Bindings.createDoubleBinding(() -> (stateName.getWidth() - name.getWidth()) / 2,
                stateName.widthProperty(), name.widthProperty()));
        name.layoutYProperty()
            .bind(Bindings.createDoubleBinding(() -> (stateName.getHeight() - name.getHeight()) / 2,
                stateName.heightProperty(), name.heightProperty()));

        stateName.getChildren().add(name);

        contents.getChildren().add(stateName);
        contents.getChildren().add(new Separator());

        // Contracts
        Label contracts = new Label("Contracts: " + stateViewModel.getContractsProperty().size());
        contracts.textProperty()
            .bind(Bindings.createStringBinding(() -> "Contracts: " + stateViewModel.getContractsProperty().size(),
                stateViewModel.getContractsProperty()));

        contents.getChildren().add(contracts);

        VBox contractsPane = new VBox();
        double maxHeight = getHeight() - stateName.getHeight() - 2 * SPACING;

        refreshContracts(contractsPane);
        contractsProperty.addListener((observable, oldValue, newValue) -> refreshContracts(contractsPane));

        contents.getChildren().add(contractsPane);
        getChildren().addAll(contents);
    }

    private void refreshContracts(VBox contractsPane) {
        contractsPane.getChildren().clear();
        contractsPane.setSpacing(SPACING);

        int contractCount = 0;

        for (ContractViewModel contract : stateViewModel.getContracts()) {
            VBox contractBox = new VBox();
            contractBox.getStyleClass().add(INNER_INNER_STYLE);

            Label contractLabel = new Label(contract.getName());
            contractLabel.textProperty().bind(contract.getNameProperty());

            HBox preconditionBox = new HBox();
            Label preconditionLabel = new Label("PRE: ");
            Label precondition = new Label(contract.getPrecondition());
            precondition.textProperty().bind(contract.getPreConditionProperty());
            preconditionBox.getChildren().addAll(preconditionLabel, precondition);

            HBox postconditionBox = new HBox();
            Label postconditionLabel = new Label("POST: ");
            Label postcondition = new Label(contract.getPostcondition());
            postcondition.textProperty().bind(contract.getPostConditionProperty());
            postconditionBox.getChildren().addAll(postconditionLabel, postcondition);

            contractBox.getChildren().addAll(contractLabel, preconditionBox, postconditionBox);
            if (contractCount >= MAX_CONTRACT_CNT) {
                return;
            }

            contractCount++;
            contractsPane.getChildren().add(contractBox);
        }
    }

    private void colorStateName(Pane stateName) {
        if (isStartStateProperty.getValue()) {
            stateName.setStyle("-fx-background-color: green;");
        } else {
            stateName.setStyle("-fx-background-color: lightgray;");
        }
    }
}
