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
import org.gecko.view.inspector.builder.AbstractInspectorBuilder;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.AbstractInspectorButton;
import org.gecko.view.inspector.element.button.InspectorCollapseContractButton;
import org.gecko.view.inspector.element.button.InspectorRemoveContractButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorAreaField;
import org.gecko.view.inspector.element.textfield.InspectorInvariantField;
import org.gecko.view.inspector.element.textfield.InspectorPostconditionField;
import org.gecko.view.inspector.element.textfield.InspectorPreconditionField;
import org.gecko.view.inspector.element.textfield.InspectorRenameField;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

/**
 * Represents a type of {@link VBox} implementing the {@link InspectorElement} interface. Holds a reference to a
 * {@link ContractViewModel} and provides separate constructors for both a contract item of a
 * state-specific-{@link org.gecko.view.inspector.Inspector Inspector} and a
 * region-specific-{@link org.gecko.view.inspector.Inspector Inspector}. The {@link InspectorContractItem} contains thus
 * {@link InspectorPreconditionField}s, {@link InspectorPostconditionField}s and an {link InspectorInvariantField} in
 * the case of a region.
 */
@Getter
public class InspectorContractItem extends VBox implements InspectorElement<VBox> {
    private ContractViewModel viewModel;

    protected static final String PRE_CONDITION_KEY = "pre_condition";
    protected static final String POST_CONDITION_KEY = "post_condition";
    protected static final String INVARIANT_KEY = "invariant";
    private static final int CONTRACT_FIELD_OFFSET = FIELD_OFFSET + 10;

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
        List<InspectorAreaField> contractFields = new ArrayList<>();

        GridPane contractConditions = new GridPane();

        InspectorLabel preConditionLabel
            = new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, PRE_CONDITION_KEY));
        InspectorAreaField preConditionField = new InspectorPreconditionField(actionManager, contractViewModel);
        contractFields.add(preConditionField);
        preConditionField.prefWidthProperty().bind(widthProperty().subtract(FIELD_OFFSET));
        contractConditions.add(preConditionLabel, 0, 0);
        contractConditions.add(preConditionField, 1, 0);


        InspectorLabel postConditionLabel =
            new InspectorLabel(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, POST_CONDITION_KEY));
        InspectorAreaField postConditionField = new InspectorPostconditionField(actionManager, contractViewModel);
        contractFields.add(postConditionField);
        postConditionField.prefWidthProperty().bind(widthProperty().subtract(FIELD_OFFSET));
        contractConditions.add(postConditionLabel, 0, 1);
        contractConditions.add(postConditionField, 1, 1);
        HBox contractNameBox = new HBox();
        HBox deleteButtonSpacer = new HBox();
        HBox.setHgrow(deleteButtonSpacer, Priority.ALWAYS);

        // Contract name
        contractNameBox.getChildren()
            .addAll(new InspectorCollapseContractButton(contractFields),
                new InspectorRenameField(actionManager, contractViewModel), deleteButtonSpacer,
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
        addContractItem(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, PRE_CONDITION_KEY),
            new InspectorPreconditionField(actionManager, regionViewModel.getContract()), 0, regionConditions);
        addContractItem(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, POST_CONDITION_KEY),
            new InspectorPostconditionField(actionManager, regionViewModel.getContract()), 1, regionConditions);
        addContractItem(ResourceHandler.getString(AbstractInspectorBuilder.INSPECTOR, INVARIANT_KEY),
            new InspectorInvariantField(actionManager, regionViewModel), 2, regionConditions);

        // Build the contract item
        getChildren().add(regionConditions);
    }

    private void addContractItem(String label, InspectorAreaField field, int row, GridPane gridPane) {
        gridPane.add(new InspectorLabel(label), 0, row);
        field.prefWidthProperty().bind(widthProperty().subtract(CONTRACT_FIELD_OFFSET));
        gridPane.add(field, 1, row);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
