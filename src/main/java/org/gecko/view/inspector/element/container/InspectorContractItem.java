package org.gecko.view.inspector.element.container;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.view.inspector.element.button.InspectorCollapseContractButton;
import org.gecko.view.inspector.element.button.InspectorRemoveContractButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.textfield.InspectorContractField;
import org.gecko.view.inspector.element.textfield.InspectorTextField;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.Renamable;
import org.gecko.viewmodel.StateViewModel;

public class InspectorContractItem extends VBox implements InspectorElement<VBox> {
    /**
     * Constructor for the State contract item
     *
     * @param actionManager
     * @param stateViewModel
     * @param contractViewModel
     */
    public InspectorContractItem(ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        // Contract fields:
        List<InspectorContractField> contractFields = new ArrayList<>();

        HBox contractPreCondition = new HBox();

        InspectorContractField preConditionField = new InspectorContractField(contractViewModel.getPreConditionProperty());
        contractFields.add(preConditionField);
        contractPreCondition.getChildren().addAll(new InspectorLabel("L:Pre:"), preConditionField);

        HBox contractPostCondition = new HBox();

        InspectorContractField postConditionField = new InspectorContractField(contractViewModel.getPostConditionProperty());
        contractFields.add(postConditionField);
        contractPostCondition.getChildren().addAll(new InspectorLabel("L:Post:"), postConditionField);
        HBox contractNameBox = new HBox();

        // Contract name
        contractNameBox.getChildren()
                       .addAll(new InspectorCollapseContractButton(contractFields), new InspectorTextField((Renamable) contractViewModel),
                           new InspectorRemoveContractButton(actionManager, stateViewModel, contractViewModel));

        // Build the contract item
        getChildren().addAll(contractNameBox, contractPreCondition, contractPostCondition);
    }

    /**
     * Constructor for the Region contract item
     *
     * @param actionManager
     * @param contractViewModel
     * @param invariant
     */
    public InspectorContractItem(ActionManager actionManager, ContractViewModel contractViewModel, StringProperty invariant) {
        HBox contractPreCondition = new HBox();
        contractPreCondition.getChildren()
                            .addAll(new InspectorLabel("L:Pre:"), new InspectorContractField(contractViewModel.getPreConditionProperty()));

        HBox contractPostCondition = new HBox();
        contractPostCondition.getChildren()
                             .addAll(new InspectorLabel("L:Post:"), new InspectorContractField(contractViewModel.getPostConditionProperty()));

        HBox contractInvariant = new HBox();
        contractInvariant.getChildren().addAll(new InspectorLabel("L:Inv:"), new InspectorContractField(invariant));

        // Build the contract item
        getChildren().addAll(new InspectorTextField((Renamable) contractViewModel), contractPreCondition, contractPostCondition, contractInvariant);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
