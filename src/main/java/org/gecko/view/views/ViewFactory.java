package org.gecko.view.views;

import org.gecko.actions.ActionManager;
import org.gecko.view.GeckoView;
import org.gecko.view.contextmenu.AbstractContextMenuBuilder;
import org.gecko.view.contextmenu.StateViewElementContextMenuBuilder;
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
    private final GeckoView geckoView;

    public ViewFactory(ActionManager actionManager, GeckoView geckoView) {
        this.actionManager = actionManager;
        this.geckoView = geckoView;
    }

    public EditorView createEditorView(EditorViewModel editorViewModel, boolean isAutomatonEditor) {
        return (isAutomatonEditor) ? createAutomatonEditorView(editorViewModel) : createSystemEditorView(editorViewModel);
    }

    public StateViewElement createStateViewElementFrom(StateViewModel stateViewModel) {
        StateViewElement newStateViewElement = new StateViewElement();
        newStateViewElement.bindTo(stateViewModel);

        AbstractContextMenuBuilder contextMenuBuilder = new StateViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView());
        newStateViewElement.setOnContextMenuRequested(
            event -> contextMenuBuilder.build().show(newStateViewElement, event.getScreenX(), event.getScreenY()));
        return newStateViewElement;
    }

    public RegionViewElement createRegionViewElementFrom(RegionViewModel regionViewModel) {
        RegionViewElement newRegionViewElement = new RegionViewElement();
        newRegionViewElement.bindTo(regionViewModel);

        return newRegionViewElement;
    }

    public VariableBlockViewElement createVariableBlockViewElementFrom(PortViewModel portViewModel) {
        VariableBlockViewElement newVariableBlockViewElement = new VariableBlockViewElement();
        newVariableBlockViewElement.bindTo(portViewModel);

        return newVariableBlockViewElement;
    }

    public EdgeViewElement createEdgeViewElementFrom(EdgeViewModel edgeViewModel) {
        return new EdgeViewElement();
    }

    public SystemConnectionViewElement createSystemConnectionViewElementFrom(SystemConnectionViewModel systemConnectionViewModel) {
        return new SystemConnectionViewElement();
    }

    public SystemViewElement createSystemViewElementFrom(SystemViewModel systemViewModel) {
        return new SystemViewElement();
    }

    private EditorView createAutomatonEditorView(EditorViewModel editorViewModel) {
        return new EditorView(this, actionManager, editorViewModel, new AutomatonEditorViewShortcutHandler());
    }

    private EditorView createSystemEditorView(EditorViewModel editorViewModel) {
        return new EditorView(this, actionManager, editorViewModel, new SystemEditorViewShortcutHandler());
    }
}
