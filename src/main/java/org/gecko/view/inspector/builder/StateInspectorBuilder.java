package org.gecko.view.inspector.builder;

import java.util.List;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorAddContractButton;
import org.gecko.view.inspector.element.button.InspectorSetStartStateButton;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.list.InspectorContractList;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

public class StateInspectorBuilder extends AbstractInspectorBuilder<StateViewModel> {
    public StateInspectorBuilder(ActionManager actionManager, EditorViewModel editorViewModel, StateViewModel viewModel) {
        super(actionManager, viewModel);

        // Region label
        List<RegionViewModel> regionViewModelList = editorViewModel.getRegionViewModels(viewModel);
        for (RegionViewModel regionViewModel : regionViewModelList) {
            addInspectorElement(new InspectorLabel(regionViewModel.getName()));
        }
        addInspectorElement(new InspectorSeparator());

        // Set start state
        addInspectorElement(new InspectorSetStartStateButton(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorLabel("L:Contracts"));
        addInspectorElement(new InspectorAddContractButton(actionManager, viewModel));
        addInspectorElement(new InspectorContractList(actionManager, viewModel));
    }
}
