package org.gecko.view.inspector.element.container;

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
import org.gecko.viewmodel.StateViewModel;

import java.util.ArrayList;
import java.util.List;

public class InspectorContractItem extends VBox implements InspectorElement<VBox> {
    /**
     * Constructor for the State contract item
     * @param actionManager
     * @param stateViewModel
     * @param contractViewModel
     */
    public InspectorContractItem(
            ActionManager actionManager,
            StateViewModel stateViewModel,
            ContractViewModel contractViewModel) {
        // Contract fields:
        List<InspectorContractField> contractFields = new ArrayList<>();

        HBox contractPreCondition = new HBox();

        InspectorContractField preConditionField =
                new InspectorContractField(contractViewModel.getPreCondition());
        contractFields.add(preConditionField);
        contractPreCondition.getChildren().addAll(new InspectorLabel("L:Pre:"), preConditionField);

        HBox contractPostCondition = new HBox();

        InspectorContractField postConditionField =
                new InspectorContractField(contractViewModel.getPostCondition());
        contractFields.add(postConditionField);
        contractPostCondition
                .getChildren()
                .addAll(new InspectorLabel("L:Post:"), postConditionField);
        HBox contractNameBox = new HBox();

        // Contract name
        contractNameBox
                .getChildren()
                .addAll(
                        new InspectorCollapseContractButton(contractFields),
                        new InspectorTextField(contractViewModel.getNameProperty()),
                        new InspectorRemoveContractButton(
                                actionManager, stateViewModel, contractViewModel));

        // Build the contract item
        getChildren().addAll(contractNameBox, contractPreCondition, contractPostCondition);
    }

    /**
     * Constructor for the Region contract item
     * @param actionManager
     * @param contractViewModel
     * @param invariant
     */
    public InspectorContractItem(
            ActionManager actionManager,
            ContractViewModel contractViewModel,
            StringProperty invariant) {
        HBox contractPreCondition = new HBox();
        contractPreCondition
                .getChildren()
                .addAll(
                        new InspectorLabel("L:Pre:"),
                        new InspectorContractField(contractViewModel.getPreCondition()));

        HBox contractPostCondition = new HBox();
        contractPostCondition
                .getChildren()
                .addAll(
                        new InspectorLabel("L:Post:"),
                        new InspectorContractField(contractViewModel.getPostCondition()));

        HBox contractInvariant = new HBox();
        contractInvariant
                .getChildren()
                .addAll(new InspectorLabel("L:Inv:"), new InspectorContractField(invariant));

        // Build the contract item
        getChildren()
                .addAll(
                        new InspectorTextField(contractViewModel.getNameProperty()),
                        contractPreCondition,
                        contractPostCondition,
                        contractInvariant);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
