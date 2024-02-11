package org.gecko.view.inspector.builder;

import javafx.collections.ObservableList;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.element.InspectorSeparator;
import org.gecko.view.inspector.element.button.InspectorSetStartStateButton;
import org.gecko.view.inspector.element.container.InspectorContractLabel;
import org.gecko.view.inspector.element.label.InspectorLabel;
import org.gecko.view.inspector.element.list.InspectorContractList;
import org.gecko.view.inspector.element.list.InspectorRegionList;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

/** Represents a type of {@link AbstractInspectorBuilder} of an {@link Inspector} for a {@link StateViewModel}. Adds to the list of {@link InspectorElement}s, which are added to a built {@link org.gecko.view.inspector.Inspector Inspector}, the following: an {@link InspectorLabel} for each {@link RegionViewModel} of the {@link StateViewModel}, an {@link InspectorSetStartStateButton} and an {@link InspectorContractList}. */
public class StateInspectorBuilder extends AbstractInspectorBuilder<StateViewModel> {

    public StateInspectorBuilder(
        ActionManager actionManager, EditorViewModel editorViewModel, StateViewModel viewModel) {
        super(actionManager, viewModel);

        // Region label
        addInspectorElement(new InspectorLabel("Regions"));
        ObservableList<RegionViewModel> regionViewModelList = editorViewModel.getRegionViewModels(viewModel);
        addInspectorElement(new InspectorRegionList(regionViewModelList));

        addInspectorElement(new InspectorSeparator());

        // Set start state
        addInspectorElement(new InspectorSetStartStateButton(actionManager, viewModel));
        addInspectorElement(new InspectorSeparator());

        // Contracts
        addInspectorElement(new InspectorContractLabel(actionManager, viewModel));
        addInspectorElement(new InspectorContractList(actionManager, viewModel));
    }
}
