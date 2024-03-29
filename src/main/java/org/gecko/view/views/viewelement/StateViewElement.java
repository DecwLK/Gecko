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
import javafx.scene.control.Labeled;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.view.ResourceHandler;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link BlockViewElement} implementing the {@link ViewElement} interface, which encapsulates an
 * {@link StateViewModel}.
 */
@Getter
public class StateViewElement extends BlockViewElement implements ViewElement<StateViewModel> {

    private static final int Z_PRIORITY = 30;
    private static final int SPACING = 5;
    private static final int MAX_CONTRACT_CNT = 4;

    private static final String START_STATE_STYLE = "state-view-element-start";
    private static final String NON_START_STATE_STYLE = "state-view-element-non-start";
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
    public StateViewModel getTarget() {
        return stateViewModel;
    }

    @Override
    public Point2D getPosition() {
        return stateViewModel.getPosition();
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
        Labeled contracts = new Label(
            ResourceHandler.getString("Labels", "contract_plural") + ": " + stateViewModel.getContractsProperty()
                .size());
        contracts.textProperty()
            .bind(Bindings.createStringBinding(() -> ResourceHandler.getString("Labels", "contract_plural") + ": "
                + stateViewModel.getContractsProperty().size(), stateViewModel.getContractsProperty()));

        contents.getChildren().add(contracts);

        VBox contractsPane = new VBox();

        refreshContracts(contractsPane);
        contractsProperty.addListener((observable, oldValue, newValue) -> refreshContracts(contractsPane));

        contents.getChildren().add(contractsPane);
        getChildren().addAll(contents);
    }

    private void refreshContracts(VBox contractsPane) {
        contractsPane.getChildren().clear();
        contractsPane.setSpacing(SPACING);

        for (int i = 0; i < MAX_CONTRACT_CNT; i++) {
            if (i >= stateViewModel.getContractsProperty().size()) {
                break;
            }

            ContractViewModel contract = stateViewModel.getContractsProperty().get(i);
            VBox contractBox = new VBox();
            contractBox.getStyleClass().add(INNER_INNER_STYLE);

            Label contractLabel = new Label(contract.getName());
            contractLabel.textProperty().bind(contract.getNameProperty());

            HBox preconditionBox = new HBox();
            Label preconditionLabel = new Label(ResourceHandler.getString("Labels", "pre_condition_short") + ": ");
            Label precondition = new Label(contract.getPrecondition());
            precondition.textProperty().bind(contract.getPreConditionProperty());
            preconditionBox.getChildren().addAll(preconditionLabel, precondition);

            HBox postconditionBox = new HBox();
            Label postconditionLabel = new Label(ResourceHandler.getString("Labels", "post_condition_short") + ": ");
            Label postcondition = new Label(contract.getPostcondition());
            postcondition.textProperty().bind(contract.getPostConditionProperty());
            postconditionBox.getChildren().addAll(postconditionLabel, postcondition);

            contractBox.getChildren().addAll(contractLabel, preconditionBox, postconditionBox);
            contractsPane.getChildren().add(contractBox);
        }
    }

    private void colorStateName(Pane stateName) {
        if (isStartStateProperty.getValue()) {
            stateName.getStyleClass().clear();
            stateName.getStyleClass().add(START_STATE_STYLE);
        } else {
            stateName.getStyleClass().clear();
            stateName.getStyleClass().add(NON_START_STATE_STYLE);
        }
    }

    private void bindViewModel() {
        nameProperty.bind(stateViewModel.getNameProperty());
        isStartStateProperty.bind(stateViewModel.getIsStartStateProperty());
        contractsProperty.bind(stateViewModel.getContractsProperty());
        prefWidthProperty().bind(
            Bindings.createDoubleBinding(() -> stateViewModel.getSize().getX(), stateViewModel.getSizeProperty()));
        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> stateViewModel.getSize().getY(), stateViewModel.getSizeProperty()));
    }
}
