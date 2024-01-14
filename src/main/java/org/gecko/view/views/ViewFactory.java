package org.gecko.view.views;

import org.gecko.actions.ActionManager;
import org.gecko.model.Automaton;
import org.gecko.view.toolbar.ToolBarBuilder;
import org.gecko.view.views.shortcuts.AutomatonEditorViewShortcutHandler;
import org.gecko.view.views.shortcuts.SystemEditorViewShortcutHandler;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewFactory {

    private final ActionManager actionManager;

    public ViewFactory(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public EditorView createEditorView(EditorViewModel editorViewModel, boolean isAutomatonEditor) {
        return (isAutomatonEditor)
                ? createAutomatonEditorView(editorViewModel)
                : createSystemEditorView(editorViewModel);
    }

    public StateViewElement createStateViewElementFrom(StateViewModel stateViewModel) {
        return new StateViewElement();
    }

    public RegionViewElement createRegionViewElementFrom(RegionViewModel regionViewModel) {
        return new RegionViewElement();
    }

    public VariableBlockViewElement createVariableBlockViewElementFrom(
            PortViewModel portViewModel) {
        return new VariableBlockViewElement();
    }

    public EdgeViewElement createEdgeViewElementFrom(EdgeViewModel edgeViewModel) {
        return new EdgeViewElement();
    }

    public SystemConnectionViewElement createSystemConnectionViewElementFrom(
            SystemConnectionViewModel systemConnectionViewModel) {
        return new SystemConnectionViewElement();
    }

    public SystemViewElement createSystemViewElementFrom(SystemViewModel systemViewModel) {
        return new SystemViewElement();
    }

    private EditorView createAutomatonEditorView(EditorViewModel editorViewModel) {
        return new EditorView(
                this,
                actionManager,
                editorViewModel,
                new ToolBarBuilder(actionManager, editorViewModel).build(),
                new AutomatonEditorViewShortcutHandler());
    }

    private EditorView createSystemEditorView(EditorViewModel editorViewModel) {
        return new EditorView(
                this,
                actionManager,
                editorViewModel,
                new ToolBarBuilder(actionManager, editorViewModel).build(),
                new SystemEditorViewShortcutHandler());
    }
}
