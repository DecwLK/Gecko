package org.gecko.actions;

import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.gecko.model.Kind;
import org.gecko.model.Visibility;
import org.gecko.tools.ToolType;
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
        return new ChangeVisibilityPortViewModelAction(geckoViewModel, portViewModel, visibility);
    }

    public ChangeVisibilityPortViewModelAction createChangeVisibilityPortViewModelAction(
        PortViewModel portViewModel, Visibility visibility, Action systemConnectionDeleteActionGroup) {
        return new ChangeVisibilityPortViewModelAction(geckoViewModel, portViewModel, visibility,
            systemConnectionDeleteActionGroup);
    }

    public CopyPositionableViewModelElementAction createCopyPositionableViewModelElementAction(
        CopyPositionableViewModelElementVisitor copyVisitor) {
        return new CopyPositionableViewModelElementAction(geckoViewModel, copyVisitor);
    }

    public CreateContractViewModelElementAction createCreateContractViewModelElementAction(
        StateViewModel stateViewModel) {
        return new CreateContractViewModelElementAction(geckoViewModel.getViewModelFactory(), stateViewModel);
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

    public CreateRegionViewModelElementAction createCreateRegionViewModelElementAction(
        Point2D position, Point2D size, Color color) {
        return new CreateRegionViewModelElementAction(geckoViewModel, geckoViewModel.getCurrentEditor(), position, size,
            color);
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

    public DeleteContractViewModelAction createDeleteContractViewModelAction(
        StateViewModel parent, ContractViewModel contractViewModel) {
        return new DeleteContractViewModelAction(geckoViewModel, parent, contractViewModel);
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

    public MoveEdgeViewModelElementAction createMoveEdgeViewModelElementAction(
        EdgeViewModel edgeViewModel, ElementScalerBlock elementScalerBlock, Point2D delta) {
        return new MoveEdgeViewModelElementAction(geckoViewModel, edgeViewModel, elementScalerBlock, delta);
    }

    public MoveSystemConnectionViewModelElementAction createMoveSystemConnectionViewModelElementAction(
        SystemConnectionViewModel systemConnectionViewModel, ElementScalerBlock elementScalerBlock, Point2D delta) {
        return new MoveSystemConnectionViewModelElementAction(geckoViewModel, systemConnectionViewModel,
            elementScalerBlock, delta);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction(
        CopyPositionableViewModelElementVisitor elementsToPaste) {
        return new PastePositionableViewModelElementAction(geckoViewModel, elementsToPaste);
    }

    public RenameViewModelElementAction createRenameViewModelElementAction(Renamable renamable, String name) {
        return new RenameViewModelElementAction(renamable, name);
    }

    public RestoreContractViewModelElementAction createRestoreContractViewModelElementAction(
        StateViewModel parent, ContractViewModel contractViewModel, Set<EdgeViewModel> edgesWithContract) {
        return new RestoreContractViewModelElementAction(parent, contractViewModel, edgesWithContract);
    }

    public ScaleBlockViewModelElementAction createScaleBlockViewModelElementAction(
        BlockViewModelElement<?> blockViewModelElement, ElementScalerBlock elementScalerBlock, Point2D oldPos,
        Point2D oldSize) {
        return new ScaleBlockViewModelElementAction(geckoViewModel.getCurrentEditor(), blockViewModelElement,
            elementScalerBlock, oldPos, oldSize);
    }

    public ScaleBlockViewModelElementAction createScaleBlockViewModelElementAction(
        BlockViewModelElement<?> blockViewModelElement, ElementScalerBlock elementScalerBlock) {
        return new ScaleBlockViewModelElementAction(geckoViewModel.getCurrentEditor(), blockViewModelElement,
            elementScalerBlock);
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

    public DeselectAction createDeselectAction() {
        return new DeselectAction(geckoViewModel.getCurrentEditor());
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
