package org.gecko.actions;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.tools.Tool;
import org.gecko.view.views.EditorView;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.Renamable;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ActionFactory {
    private final GeckoViewModel geckoViewModel;

    public ActionFactory(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
    }

    public ChangeColorRegionViewModelElementAction createChangeColorRegionViewModelElementAction(RegionViewModel regionViewModel, Color color) {
        return new ChangeColorRegionViewModelElementAction(regionViewModel, color);
    }

    public ChangeInvariantViewModelElementAction createChangeInvariantViewModelElementAction(RegionViewModel regionViewModel, String newInvariant) {
        return new ChangeInvariantViewModelElementAction(regionViewModel, newInvariant);
    }

    public ChangePreconditionViewModelElementAction createChangePreconditionViewModelElementAction(ContractViewModel contractViewModel,
                                                                                                   String newPrecondition) {
        return new ChangePreconditionViewModelElementAction(contractViewModel, newPrecondition);
    }

    public ChangePostconditionViewModelElementAction createChangePostconditionViewModelElementAction(ContractViewModel contractViewModel,
                                                                                                     String newPostcondition) {
        return new ChangePostconditionViewModelElementAction(contractViewModel, newPostcondition);
    }

    public ChangeTypePortViewModelElementAction createChangeTypePortViewModelElementAction(PortViewModel portViewModel, String newType) {
        return new ChangeTypePortViewModelElementAction(portViewModel, newType);
    }

    public CopyPositionableViewModelElementAction createCopyPositionableViewModelElementAction(List<PositionableViewModelElement<?>> elements) {
        return new CopyPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), elements);
    }

    public CreateContractViewModelElementAction createCreateContractViewModelElementAction(StateViewModel stateViewModel) {
        return new CreateContractViewModelElementAction(geckoViewModel.getViewModelFactory(), stateViewModel);
    }

    public CreateEdgeViewModelElementAction createCreateEdgeViewModelElementAction(StateViewModel source, StateViewModel destination) {
        return new CreateEdgeViewModelElementAction(geckoViewModel, source, destination);
    }

    public CreatePortViewModelElementAction createCreatePortViewModelElementAction(Point2D position) {
        return new CreatePortViewModelElementAction(geckoViewModel, position);
    }

    public CreateRegionViewModelElementAction createCreateRegionViewModelElementAction(Point2D position) {
        return new CreateRegionViewModelElementAction(geckoViewModel, position);
    }

    public CreateStateViewModelElementAction createCreateStateViewModelElementAction(Point2D position) {
        return new CreateStateViewModelElementAction(geckoViewModel, position);
    }

    public CreateSystemConnectionViewModelElementAction createCreateSystemConnectionViewModelElementAction(PortViewModel source,
                                                                                                           PortViewModel destination) {
        return new CreateSystemConnectionViewModelElementAction(geckoViewModel, source, destination);
    }

    public CreateSystemViewModelElementAction createCreateSystemViewModelElementAction(Point2D position) {
        return new CreateSystemViewModelElementAction(geckoViewModel, position);
    }

    public CreateVariableAction createCreateVariableAction(SystemViewModel systemViewModel) {
        return new CreateVariableAction(geckoViewModel, systemViewModel);
    }

    public CutPositionableViewModelElementAction createCutPositionableViewModelElementAction(List<PositionableViewModelElement<?>> elements) {
        return new CutPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), elements);
    }

    public DeleteContractViewModelAction createDeleteContractViewModelAction(StateViewModel parent, ContractViewModel contractViewModel) {
        return new DeleteContractViewModelAction(geckoViewModel, parent, contractViewModel);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(PositionableViewModelElement<?> element) {
        return new DeletePositionableViewModelElementAction(geckoViewModel, element);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(List<PositionableViewModelElement<?>> elements) {
        return new DeletePositionableViewModelElementAction(geckoViewModel, elements);
    }

    public MoveBlockViewModelElementAction createMoveBlockViewModelElementAction(Point2D offset) {
        return new MoveBlockViewModelElementAction(geckoViewModel.getCurrentEditor().getSelectionManager(), offset);
    }

    public MoveEdgeViewModelElementAction createMoveEdgeViewModelElementAction(EdgeViewModel edgeViewModel, StateViewModel stateViewModel,
                                                                               boolean isSource) {
        return new MoveEdgeViewModelElementAction(edgeViewModel, stateViewModel, isSource);
    }

    public MoveSystemConnectionViewModelElementAction createMoveSystemConnectionViewModelElementAction(
        SystemConnectionViewModel systemConnectionViewModel, PortViewModel systemViewModel, boolean isSource) {
        return new MoveSystemConnectionViewModelElementAction(systemConnectionViewModel, systemViewModel, isSource);
    }

    public PanAction createPanAction(Point2D pivot) {
        return new PanAction(geckoViewModel.getCurrentEditor(), pivot);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction() {
        return new PastePositionableViewModelElementAction(geckoViewModel);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction(List<PositionableViewModelElement<?>> elements) {
        return new PastePositionableViewModelElementAction(geckoViewModel, elements);
    }

    public RenameViewModelElementAction createRenameViewModelElementAction(Renamable renamable, String name) {
        return new RenameViewModelElementAction(renamable, name);
    }

    public RestorePositionableViewModelElementAction createRestorePositionableViewModelElementAction(List<PositionableViewModelElement<?>> elements) {
        return new RestorePositionableViewModelElementAction(geckoViewModel, elements);
    }

    public ScaleBlockViewModelElementAction createScaleBlockViewModelElementAction(BlockViewModelElement<?> element, double scaleFactor) {
        return new ScaleBlockViewModelElementAction(element, scaleFactor);
    }

    public FocusPositionableViewModelElementAction createFocusPositionableViewModelElementAction(PositionableViewModelElement<?> element) {
        return new FocusPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), element);
    }

    public ModifyEdgeViewModelPriorityAction createModifyEdgeViewModelPriorityAction(EdgeViewModel edgeViewModel, int priority) {
        return new ModifyEdgeViewModelPriorityAction(edgeViewModel, priority);
    }

    public SelectAction createSelectAction(PositionableViewModelElement<?> element, boolean newSelection) {
        return new SelectAction(geckoViewModel.getCurrentEditor(), element, newSelection);
    }

    public SelectAction createSelectAction(List<PositionableViewModelElement<?>> elements, boolean newSelection) {
        return new SelectAction(geckoViewModel.getCurrentEditor(), elements, newSelection);
    }

    public SelectionHistoryBackAction createSelectionHistoryBackAction() {
        return new SelectionHistoryBackAction(geckoViewModel.getCurrentEditor().getSelectionManager());
    }

    public SelectionHistoryForwardAction createSelectionHistoryForwardAction() {
        return new SelectionHistoryForwardAction(geckoViewModel.getCurrentEditor().getSelectionManager());
    }

    public SelectToolAction createSelectToolAction(EditorView editorView, Tool tool) {
        return new SelectToolAction(editorView, geckoViewModel.getCurrentEditor(), tool);
    }

    public SetStartStateViewModelElementAction createSetStartStateViewModelElementAction(StateViewModel stateViewModel) {
        return new SetStartStateViewModelElementAction(geckoViewModel.getCurrentEditor(), stateViewModel);
    }

    public ViewSwitchAction createViewSwitchAction(SystemViewModel systemViewModel, boolean isAutomaton) {
        return new ViewSwitchAction(geckoViewModel, systemViewModel, isAutomaton);
    }

    public ZoomAction createZoomAction(Point2D pivot, double factor) {
        return new ZoomAction(geckoViewModel.getCurrentEditor(), pivot, factor);
    }
}
