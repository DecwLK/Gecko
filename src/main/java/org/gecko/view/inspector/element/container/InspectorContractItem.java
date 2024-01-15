package org.gecko.view.inspector.element.container;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gecko.actions.ActionManager;
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
    /**
     * Constructor for the State contract item.
     *
     * @param actionManager     Action manager
     * @param stateViewModel    State view model
     * @param contractViewModel Contract view model
     */
    public InspectorContractItem(ActionManager actionManager, StateViewModel stateViewModel, ContractViewModel contractViewModel) {
        // Contract fields:
        List<InspectorContractField> contractFields = new ArrayList<>();

        HBox contractPreCondition = new HBox();

        InspectorContractField preConditionField = new InspectorPreconditionField(actionManager, contractViewModel);
        contractFields.add(preConditionField);
        contractPreCondition.getChildren().addAll(new InspectorLabel("L:Pre:"), preConditionField);

        HBox contractPostCondition = new HBox();

        InspectorContractField postConditionField = new InspectorPostconditionField(actionManager, contractViewModel);
        contractFields.add(postConditionField);
        contractPostCondition.getChildren().addAll(new InspectorLabel("L:Post:"), postConditionField);
        HBox contractNameBox = new HBox();

        // Contract name
        contractNameBox.getChildren()
                       .addAll(new InspectorCollapseContractButton(contractFields), new InspectorTextField(actionManager, contractViewModel),
                           new InspectorRemoveContractButton(actionManager, stateViewModel, contractViewModel));

        // Build the contract item
        getChildren().addAll(contractNameBox, contractPreCondition, contractPostCondition);
    }

    /**
     * Constructor for the Region contract item.
     *
     * @param actionManager   Action manager
     * @param regionViewModel Region view model
     */
    public InspectorContractItem(ActionManager actionManager, RegionViewModel regionViewModel) {
        HBox contractPreCondition = new HBox();
        contractPreCondition.getChildren()
                            .addAll(new InspectorLabel("L:Pre:"), new InspectorPreconditionField(actionManager, regionViewModel.getContract()));

        HBox contractPostCondition = new HBox();
        contractPostCondition.getChildren()
                             .addAll(new InspectorLabel("L:Post:"), new InspectorPostconditionField(actionManager, regionViewModel.getContract()));

        HBox contractInvariant = new HBox();
        contractInvariant.getChildren().addAll(new InspectorLabel("L:Inv:"), new InspectorInvariantField(actionManager, regionViewModel));

        // Build the contract item
        getChildren().addAll(new InspectorTextField(actionManager, regionViewModel), contractPreCondition, contractPostCondition, contractInvariant);
    }

    @Override
    public VBox getControl() {
        return this;
    }
}
