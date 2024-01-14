package org.gecko.actions;

import javafx.geometry.Point2D;
import org.gecko.model.SystemConnection;
import org.gecko.model.Visibility;
import org.gecko.tools.Tool;
import org.gecko.viewmodel.ContractViewModel;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.Renamable;
import org.gecko.viewmodel.SelectionManager;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;
import org.gecko.viewmodel.ViewModelFactory;

import java.util.List;

public class ActionFactory {
    private GeckoViewModel geckoViewModel;

    public ActionFactory(GeckoViewModel geckoViewModel) {
        this.geckoViewModel = geckoViewModel;
    }

    public CopyPositionableViewModelElementAction createCopyPositionableViewModelElementAction(
            EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements) {
        return new CopyPositionableViewModelElementAction(this, editorViewModel, elements);
    }

    public CreateContractViewModelElementAction createCreateContractViewModelElementAction(
            StateViewModel stateViewModel) {
        return new CreateContractViewModelElementAction(this, geckoViewModel, stateViewModel);
    }

    public CreateEdgeViewModelElementAction createCreateEdgeViewModelElementAction(
            ViewModelFactory viewModelFactory, StateViewModel source, StateViewModel destination) {
        return new CreateEdgeViewModelElementAction(
                this, geckoViewModel, viewModelFactory, source, destination);
    }

    public CreatePortViewModelElementAction createCreatePortViewModelElementAction(
            ViewModelFactory viewModelFactory) {
        return new CreatePortViewModelElementAction(this, geckoViewModel, viewModelFactory);
    }

    public CreatePortViewModelElementAction createCreatePortViewModelElementAction(
            ViewModelFactory viewModelFactory, Point2D position) {
        return new CreatePortViewModelElementAction(
                this, geckoViewModel, viewModelFactory, position);
    }

    public CreateRegionViewModelElementAction createCreateRegionViewModelElementAction(
            ViewModelFactory viewModelFactory, Point2D position) {
        return new CreateRegionViewModelElementAction(
                this, geckoViewModel, viewModelFactory, position);
    }

    public CreateStateViewModelElementAction createCreateStateViewModelElementAction(
            ViewModelFactory viewModelFactory, Point2D position) {
        return new CreateStateViewModelElementAction(
                this, geckoViewModel, viewModelFactory, position);
    }

    public CreateSystemConnectionViewModelElementAction
            createCreateSystemConnectionViewModelElementAction(
                    ViewModelFactory viewModelFactory,
                    PortViewModel source,
                    PortViewModel destination) {
        return new CreateSystemConnectionViewModelElementAction(
                this, geckoViewModel, viewModelFactory, source, destination);
    }

    public CreateSystemViewModelElementAction createCreateSystemViewModelElementAction(
            ViewModelFactory viewModelFactory, Point2D position) {
        return new CreateSystemViewModelElementAction(
                this, geckoViewModel, viewModelFactory, position);
    }

    public CreateVariableAction createCreateVariableAction(SystemViewModel systemViewModel, Visibility visibility) {
        return new CreateVariableAction(this, systemViewModel, visibility);
    }

    public CutPositionableViewModelElementAction createCutPositionableViewModelElementAction(
            EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements) {
        return new CutPositionableViewModelElementAction(this, editorViewModel, elements);
    }

    public DeleteContractViewModelAction createDeleteContractViewModelAction(
            StateViewModel parent, ContractViewModel contractViewModel) {
        return new DeleteContractViewModelAction(this, geckoViewModel, parent, contractViewModel);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(
            PositionableViewModelElement<?> element) {
        return new DeletePositionableViewModelElementAction(this, geckoViewModel, element);
    }

    public DeletePositionableViewModelElementAction createDeletePositionableViewModelElementAction(
            List<PositionableViewModelElement<?>> elements) {
        return new DeletePositionableViewModelElementAction(this, geckoViewModel, elements);
    }

    public MoveBlockViewModelElementAction createMoveBlockViewModelElementAction(
            SelectionManager selectionManager, Point2D offset) {
        return new MoveBlockViewModelElementAction(this, selectionManager, offset);
    }

    public MoveEdgeViewModelElementAction createMoveEdgeViewModelElementAction(
            EdgeViewModel edgeViewModel, StateViewModel stateViewModel, boolean isSource) {
        return new MoveEdgeViewModelElementAction(this, edgeViewModel, stateViewModel, isSource);
    }

    public MoveSystemConnectionViewModelElementAction
            createMoveSystemConnectionViewModelElementAction(
                    SystemConnectionViewModel systemConnectionViewModel,
                    SystemViewModel systemViewModel) {
        return new MoveSystemConnectionViewModelElementAction(
                this, systemConnectionViewModel, systemViewModel);
    }

    public PanAction createPanAction(EditorViewModel editorViewModel, Point2D pivot) {
        return new PanAction(this, editorViewModel, pivot);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction() {
        return new PastePositionableViewModelElementAction(this, geckoViewModel);
    }

    public PastePositionableViewModelElementAction createPastePositionableViewModelElementAction(
            List<PositionableViewModelElement<?>> elements) {
        return new PastePositionableViewModelElementAction(this, geckoViewModel, elements);
    }

    public RenameViewModelElementAction createRenameViewModelElementAction(
            Renamable renamable, String name) {
        return new RenameViewModelElementAction(this, renamable, name);
    }

    public RestorePositionableViewModelElementAction
            createRestorePositionableViewModelElementAction(
                    List<PositionableViewModelElement<?>> elements) {
        return new RestorePositionableViewModelElementAction(this, geckoViewModel, elements);
    }

    public ScaleBlockViewModelElementAction createScaleBlockViewModelElementAction(
            SelectionManager selectionManager, double scaleFactor) {
        return new ScaleBlockViewModelElementAction(this, selectionManager, scaleFactor);
    }

    public FocusPositionableViewModelElementAction createFocusPositionableViewModelElementAction(
            EditorViewModel editorViewModel, PositionableViewModelElement<?> element) {
        return new FocusPositionableViewModelElementAction(this, editorViewModel, element);
    }

    public SelectAction createSelectAction(
            EditorViewModel editorViewModel,
            PositionableViewModelElement<?> element,
            boolean newSelection) {
        return new SelectAction(this, editorViewModel, element, newSelection);
    }

    public SelectAction createSelectAction(
            EditorViewModel editorViewModel,
            List<PositionableViewModelElement<?>> elements,
            boolean newSelection) {
        return new SelectAction(this, editorViewModel, elements, newSelection);
    }

    public SelectionHistoryBackAction createSelectionHistoryBackAction(
            SelectionManager selectionManager) {
        return new SelectionHistoryBackAction(this, selectionManager);
    }

    public SelectionHistoryForwardAction createSelectionHistoryForwardAction(
            SelectionManager selectionManager) {
        return new SelectionHistoryForwardAction(this, selectionManager);
    }

    public SelectToolAction createSelectToolAction(EditorViewModel editorViewModel, Tool tool) {
        return new SelectToolAction(this, editorViewModel, tool);
    }

    public SetStartStateViewModelElementAction createSetStartStateViewModelElementAction(
            EditorViewModel editorViewModel, StateViewModel stateViewModel) {
        return new SetStartStateViewModelElementAction(this, editorViewModel, stateViewModel);
    }

    public ViewSwitchAction createViewSwitchAction(
            SystemViewModel systemViewModel,
            EditorViewModel currentEditorViewModel,
            boolean isAutomaton) {
        return new ViewSwitchAction(this, systemViewModel, currentEditorViewModel, isAutomaton);
    }

    public ZoomAction createZoomAction(
            EditorViewModel editorViewModel, Point2D pivot, double factor) {
        return new ZoomAction(this, editorViewModel, pivot, factor);
    }
}
