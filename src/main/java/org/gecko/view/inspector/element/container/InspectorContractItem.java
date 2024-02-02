package org.gecko.view.inspector.element.container;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.InspectorCollapseContractButton;
import org.gecko.view.inspector.element.button.InspectorRemoveContractButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorContractField;
import org.gecko.view.inspector.element.textfield.InspectorInvariantField;
import org.gecko.view.inspector.element.textfield.InspectorPostconditionField;
import org.gecko.view.inspector.element.textfield.InspectorPreconditionField;
import org.gecko.view.inspector.element.textfield.InspectorTextField;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractItem extends VBox implements InspectorElement<VBox> {
    @Getter
    private ContractViewModel viewModel;

    /**
     * Constructor for the State contract item.
     *
     * @param actionManager     Action manager
     * @param stateViewModel    State view model
     * @param contractViewModel Contract view model
     */
    public InspectorContractItem(
        ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        this.viewModel = contractViewModel;

        // Contract fields:
        List<InspectorContractField> contractFields = new ArrayList<>();

        GridPane contractConditions = new GridPane();

        InspectorLabel preConditionLabel = new InspectorLabel(ResourceHandler.getString("Inspector", "pre_condition"));
        InspectorContractField preConditionField = new InspectorPreconditionField(actionManager, contractViewModel);
        contractFields.add(preConditionField);
        preConditionField.prefWidthProperty().bind(widthProperty().subtract(50));
        contractConditions.add(preConditionLabel, 0, 0);
        contractConditions.add(preConditionField, 1, 0);


        InspectorLabel postConditionLabel =
            new InspectorLabel(ResourceHandler.getString("Inspector", "post_condition"));
        InspectorContractField postConditionField = new InspectorPostconditionField(actionManager, contractViewModel);
        contractFields.add(postConditionField);
        postConditionField.prefWidthProperty().bind(widthProperty().subtract(50));
        contractConditions.add(postConditionLabel, 0, 1);
        contractConditions.add(postConditionField, 1, 1);
        HBox contractNameBox = new HBox();
        HBox deleteButtonSpacer = new HBox();
        HBox.setHgrow(deleteButtonSpacer, Priority.ALWAYS);

        // Contract name
        contractNameBox.getChildren()
            .addAll(new InspectorCollapseContractButton(contractFields),
                new InspectorTextField(actionManager, contractViewModel), deleteButtonSpacer,
                new InspectorRemoveContractButton(actionManager, stateViewModel, contractViewModel));

        // Build the contract item
        getChildren().addAll(contractNameBox, contractConditions);
    }

    /**
     * Constructor for the Region contract item.
     *
     * @param actionManager   Action manager
     * @param regionViewModel Region view model
     */
    public InspectorContractItem(ActionManager actionManager, RegionViewModel regionViewModel) {

        GridPane regionConditions = new GridPane();
        addContractItem(ResourceHandler.getString("Inspector", "pre_condition"),
            new InspectorPreconditionField(actionManager, regionViewModel.getContract()), 0, regionConditions);
        addContractItem(ResourceHandler.getString("Inspector", "post_condition"),
            new InspectorPostconditionField(actionManager, regionViewModel.getContract()), 1, regionConditions);
        addContractItem(ResourceHandler.getString("Inspector", "invariant"),
            new InspectorInvariantField(actionManager, regionViewModel), 2, regionConditions);

        // Build the contract item
        getChildren().add(regionConditions);
    }

    private void addContractItem(String label, InspectorContractField field, int row, GridPane gridPane) {
        gridPane.add(new InspectorLabel(label), 0, row);
        field.prefWidthProperty().bind(widthProperty().subtract(60));
        gridPane.add(field, 1, row);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
