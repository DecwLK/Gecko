package org.gecko.view.inspector.element.combobox;

import javafx.scene.control.ComboBox;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorElement;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;

public class InspectorContractComboBox extends ComboBox<String> implements InspectorElement<ComboBox<String>> {

    private static final int PREF_WIDTH = 300;

    public InspectorContractComboBox(ActionManager actionManager, EdgeViewModel viewModel) {
        setPrefWidth(PREF_WIDTH);
        getItems().setAll(viewModel.getSource().getContracts().stream().map(ContractViewModel::getName).toList());
        viewModel.getSource().getContractsProperty().addListener((observable, oldValue, newValue) -> {
            getItems().setAll(viewModel.getSource().getContracts().stream().map(ContractViewModel::getName).toList());
        });
        setValue(viewModel.getContract() == null ? null : viewModel.getContract().getName());
        viewModel.getContractProperty().addListener((observable, oldValue, newValue) -> {
            setValue(newValue == null ? null : newValue.getName());
        });

        setOnAction(event -> {
            if (getValue() == null || (viewModel.getContract() != null && getValue().equals(
                viewModel.getContract().getName()))) {
                return;
            }
            ContractViewModel newContract = viewModel.getSource()
                .getContracts()
                .stream()
                .filter(contract -> contract.getName().equals(getValue()))
                .findFirst()
                .orElseThrow();
            actionManager.run(
                actionManager.getActionFactory().createChangeContractEdgeViewModelAction(viewModel, newContract));
        });
    }

    @Override
    public ComboBox<String> getControl() {
        return this;
    }
}
