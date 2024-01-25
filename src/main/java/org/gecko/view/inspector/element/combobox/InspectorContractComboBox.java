package org.gecko.view.inspector.element.combobox;

import javafx.scene.control.ComboBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorContractComboBox extends ComboBox<String> implements InspectorElement<ComboBox<String>> {

    private static final int PREF_WIDTH = 300;
    private final EdgeViewModel viewModel;

    public InspectorContractComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        this.viewModel = viewModel;
        setPrefWidth(PREF_WIDTH);
        getItems().setAll(viewModel.getSource().getContracts().stream().map(ContractViewModel::getName).toList());
        valueProperty().addListener((observable, oldValue, newValue) -> {
            //Should never not be present because we are choosing the name of a contract
            ContractViewModel newContract =
                viewModel.getSource().getContracts().stream().filter(contract -> contract.getName().equals(newValue)).findFirst().orElseThrow();
        });
    }

    @Override
    public ComboBox<String> getControl() {
        return this;
    }
}
