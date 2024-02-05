package org.gecko.actions;

import java.util.List;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.model.Kind;
import org.gecko.model.Visibility;
import org.gecko.tools.ToolType;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerBlock;
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

    public ChangeColorRegionViewModelElementAction createChangeColorRegionViewModelElementAction(
        RegionViewModel regionViewModel, Color color) {
        return new ChangeColorRegionViewModelElementAction(regionViewModel, color);
    }

    public Action createChangeContractEdgeViewModelAction(EdgeViewModel viewModel, ContractViewModel contract) {
        return new ChangeContractEdgeViewModelAction(viewModel, contract);
    }

    public ChangeInvariantViewModelElementAction createChangeInvariantViewModelElementAction(
        RegionViewModel regionViewModel, String newInvariant) {
        return new ChangeInvariantViewModelElementAction(regionViewModel, newInvariant);
    }

    public ChangeKindEdgeViewModelAction createChangeKindAction(EdgeViewModel edgeViewModel, Kind kind) {
        return new ChangeKindEdgeViewModelAction(edgeViewModel, kind);
    }

    public ChangePreconditionViewModelElementAction createChangePreconditionViewModelElementAction(
        ContractViewModel contractViewModel, String newPrecondition) {
        return new ChangePreconditionViewModelElementAction(contractViewModel, newPrecondition);
    }

    public ChangePostconditionViewModelElementAction createChangePostconditionViewModelElementAction(
        ContractViewModel contractViewModel, String newPostcondition) {
        return new ChangePostconditionViewModelElementAction(contractViewModel, newPostcondition);
    }

    public ChangeTypePortViewModelElementAction createChangeTypePortViewModelElementAction(
        PortViewModel portViewModel, String newType) {
        return new ChangeTypePortViewModelElementAction(portViewModel, newType);
    }

    public ChangeVisibilityPortViewModelAction createChangeVisibilityPortViewModelAction(
        PortViewModel portViewModel, Visibility visibility) {
        return new ChangeVisibilityPortViewModelAction(portViewModel, visibility);
    }

    public CopyPositionableViewModelElementAction createCopyPositionableViewModelElementAction(
        List<PositionableViewModelElement<?>> elements) {
        return new CopyPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), elements);
    }

    public CreateContractViewModelElementAction createCreateContractViewModelElementAction(
        StateViewModel stateViewModel) {
        return new CreateContractViewModelElementAction(geckoViewModel.getViewModelFactory(), stateViewModel);
    }

    public CreateConnectionScalerBlockViewElementAction createCreateConnectionScalerBlockViewElementAction(
        ConnectionElementScalerViewElementDecorator decorator, Point2D position) {
        return new CreateConnectionScalerBlockViewElementAction(decorator, position);
    }

    public CreateEdgeViewModelElementAction createCreateEdgeViewModelElementAction(
        StateViewModel source, StateViewModel destination) {
        return new CreateEdgeViewModelElementAction(geckoViewModel, source, destination);
    }

    public CreatePortViewModelElementAction createCreatePortViewModelElementAction(SystemViewModel parentSystem) {
        return new CreatePortViewModelElementAction(geckoViewModel, parentSystem);
    }

    public CreateRegionViewModelElementAction createCreateRegionViewModelElementAction(Point2D position, Point2D size) {
        return new CreateRegionViewModelElementAction(geckoViewModel, geckoViewModel.getCurrentEditor(), position,
            size);
    }

    public CreateStateViewModelElementAction createCreateStateViewModelElementAction(Point2D position) {
        return new CreateStateViewModelElementAction(geckoViewModel, geckoViewModel.getCurrentEditor(), position);
    }

    public CreateSystemConnectionViewModelElementAction createCreateSystemConnectionViewModelElementAction(
        PortViewModel source, PortViewModel destination) {
        return new CreateSystemConnectionViewModelElementAction(geckoViewModel, source, destination);
    }

    public CreateSystemViewModelElementAction createCreateSystemViewModelElementAction(Point2D position) {
        return new CreateSystemViewModelElementAction(geckoViewModel, geckoViewModel.getCurrentEditor(), position);
    }

    public CreateVariableAction createCreateVariableAction(Point2D position) {
        return new CreateVariableAction(geckoViewModel, geckoViewModel.getCurrentEditor(), position);
    }

    public CutPositionableViewModelElementAction createCutPositionableViewModelElementAction(
        List<PositionableViewModelElement<?>> elements) {
        return new CutPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), elements);
    }

    public DeleteContractViewModelAction createDeleteContractViewModelAction(
        StateViewModel parent, ContractViewModel contractViewModel) {
        return new DeleteContractViewModelAction(geckoViewModel, parent, contractViewModel);
    }

    public DeleteConnectionScalerBlockViewElementAction createDestroyConnectionScalerBlockViewElementAction(
        ConnectionElementScalerViewElementDecorator decorator, ElementScalerBlock scalerBlock) {
        return new DeleteConnectionScalerBlockViewElementAction(decorator, scalerBlock);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(
        PositionableViewModelElement<?> element) {
        return new DeletePositionableViewModelElementAction(geckoViewModel, element);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(
        Set<PositionableViewModelElement<?>> elements) {
        return new DeletePositionableViewModelElementAction(geckoViewModel, elements);
    }

    public MoveBlockViewModelElementAction createMoveBlockViewModelElementAction(Point2D delta) {
        return new MoveBlockViewModelElementAction(geckoViewModel.getCurrentEditor(), delta);
    }

    public MoveBlockViewModelElementAction createMoveBlockViewModelElementAction(
        Set<PositionableViewModelElement<?>> elementsToMove, Point2D delta) {
        return new MoveBlockViewModelElementAction(geckoViewModel.getCurrentEditor(), elementsToMove, delta);
    }

    public MoveConnectionScalerBlockViewElementAction createMoveConnectionScalerBlockViewElementAction(
        EdgeViewModel edgeViewModel, ElementScalerBlock scalerBlock, Point2D delta) {
        return new MoveConnectionScalerBlockViewElementAction(geckoViewModel, edgeViewModel, scalerBlock, delta);
    }

    public MoveConnectionScalerBlockViewElementAction createMoveConnectionScalerBlockViewElementAction(
        SystemConnectionViewModel systemConnectionViewModel, ElementScalerBlock scalerBlock, Point2D delta) {
        return new MoveConnectionScalerBlockViewElementAction(geckoViewModel, systemConnectionViewModel, scalerBlock,
            delta);
    }

    public MoveSystemConnectionViewModelElementAction createMoveSystemConnectionViewModelElementAction(
        SystemConnectionViewModel systemConnectionViewModel, PortViewModel systemViewModel, boolean isSource) {
        return new MoveSystemConnectionViewModelElementAction(geckoViewModel, systemConnectionViewModel,
            systemViewModel, isSource);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction() {
        return new PastePositionableViewModelElementAction(geckoViewModel);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction(
        List<PositionableViewModelElement<?>> elements) {
        return new PastePositionableViewModelElementAction(geckoViewModel, elements);
    }

    public RenameViewModelElementAction createRenameViewModelElementAction(Renamable renamable, String name) {
        return new RenameViewModelElementAction(renamable, name);
    }

    public RestoreContractViewModelElementAction createRestoreContractViewModelElementAction(
        StateViewModel parent, ContractViewModel contractViewModel) {
        return new RestoreContractViewModelElementAction(parent, contractViewModel);
    }

    public ScaleBlockViewModelElementAction createScaleBlockViewModelElementAction(
        BlockViewModelElement<?> element, double scaleFactor) {
        return new ScaleBlockViewModelElementAction(element, scaleFactor);
    }

    public FocusPositionableViewModelElementAction createFocusPositionableViewModelElementAction(
        PositionableViewModelElement<?> element) {
        return new FocusPositionableViewModelElementAction(geckoViewModel.getCurrentEditor(), element);
    }

    public ModifyEdgeViewModelPriorityAction createModifyEdgeViewModelPriorityAction(
        EdgeViewModel edgeViewModel, int priority) {
        return new ModifyEdgeViewModelPriorityAction(edgeViewModel, priority);
    }

    public SelectAction createSelectAction(PositionableViewModelElement<?> element, boolean newSelection) {
        return createSelectAction(Set.of(element), newSelection);
    }

    public SelectAction createSelectAction(Set<PositionableViewModelElement<?>> elements, boolean newSelection) {
        return new SelectAction(geckoViewModel.getCurrentEditor(), elements, newSelection);
    }

    public SelectionHistoryBackAction createSelectionHistoryBackAction() {
        return new SelectionHistoryBackAction(geckoViewModel.getCurrentEditor().getSelectionManager());
    }

    public SelectionHistoryForwardAction createSelectionHistoryForwardAction() {
        return new SelectionHistoryForwardAction(geckoViewModel.getCurrentEditor().getSelectionManager());
    }

    public SelectToolAction createSelectToolAction(ToolType tool) {
        return new SelectToolAction(geckoViewModel.getCurrentEditor(), tool);
    }

    public SetStartStateViewModelElementAction createSetStartStateViewModelElementAction(
        StateViewModel stateViewModel) {
        return new SetStartStateViewModelElementAction(geckoViewModel, stateViewModel);
    }

    public ViewSwitchAction createViewSwitchAction(SystemViewModel systemViewModel, boolean isAutomaton) {
        return new ViewSwitchAction(geckoViewModel, systemViewModel, isAutomaton);
    }

    public ZoomAction createZoomAction(Point2D pivot, double factor) {
        return new ZoomAction(geckoViewModel.getCurrentEditor(), pivot, factor);
    }

    public ZoomCenterAction createZoomCenterAction(double factor) {
        return new ZoomCenterAction(geckoViewModel.getCurrentEditor(), factor);
    }
}
