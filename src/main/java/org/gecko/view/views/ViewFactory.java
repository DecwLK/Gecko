package org.gecko.view.views;

import org.gecko.actions.ActionManager;
import org.gecko.view.GeckoView;
import org.gecko.view.contextmenu.AbstractContextMenuBuilder;
import org.gecko.view.contextmenu.EdgeViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.RegionViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.StateViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.SystemConnectionViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.SystemViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.VariableBlockViewElementContextMenuBuilder;
import org.gecko.view.views.shortcuts.AutomatonEditorViewShortcutHandler;
import org.gecko.view.views.shortcuts.SystemEditorViewShortcutHandler;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.SelectableViewElementDecorator;
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
        return isAutomatonEditor ? createAutomatonEditorView(editorViewModel) : createSystemEditorView(editorViewModel);
    }

    public ViewElement<?> createViewElementFrom(StateViewModel stateViewModel) {
        StateViewElement newStateViewElement = new StateViewElement(stateViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new StateViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(), stateViewModel);
        newStateViewElement.setOnContextMenuRequested(
            event -> contextMenuBuilder.build().show(newStateViewElement, event.getScreenX(), event.getScreenY()));
        return new SelectableViewElementDecorator(newStateViewElement);
    }

    public ViewElement<?> createViewElementFrom(RegionViewModel regionViewModel) {
        RegionViewElement newRegionViewElement = new RegionViewElement(regionViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new RegionViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(), regionViewModel);
        newRegionViewElement.setOnContextMenuRequested(
            event -> contextMenuBuilder.build().show(newRegionViewElement, event.getScreenX(), event.getScreenY()));
        return new SelectableViewElementDecorator(new ElementScalerViewElementDecorator(newRegionViewElement));
    }

    public ViewElement<?> createViewElementFrom(PortViewModel portViewModel) {
        VariableBlockViewElement newVariableBlockViewElement = new VariableBlockViewElement(portViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new VariableBlockViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(), portViewModel);
        newVariableBlockViewElement.setOnContextMenuRequested(event -> contextMenuBuilder.build()
            .show(newVariableBlockViewElement, event.getScreenX(), event.getScreenY()));

        return new SelectableViewElementDecorator(newVariableBlockViewElement);
    }

    public ViewElement<?> createViewElementFrom(EdgeViewModel edgeViewModel) {
        EdgeViewElement newEdgeViewElement = new EdgeViewElement(edgeViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new EdgeViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(), edgeViewModel);
        newEdgeViewElement.setOnContextMenuRequested(
            event -> contextMenuBuilder.build().show(newEdgeViewElement, event.getScreenX(), event.getScreenY()));

        return new ConnectionElementScalerViewElementDecorator(newEdgeViewElement);
    }

    public ViewElement<?> createViewElementFrom(SystemConnectionViewModel systemConnectionViewModel) {
        SystemConnectionViewElement newSystemConnectionViewElement =
            new SystemConnectionViewElement(systemConnectionViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new SystemConnectionViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(),
                systemConnectionViewModel);
        newSystemConnectionViewElement.setOnContextMenuRequested(event -> contextMenuBuilder.build()
            .show(newSystemConnectionViewElement, event.getScreenX(), event.getScreenY()));

        return new ConnectionElementScalerViewElementDecorator(newSystemConnectionViewElement);
    }

    public ViewElement<?> createViewElementFrom(SystemViewModel systemViewModel) {
        SystemViewElement newSystemViewElement = new SystemViewElement(systemViewModel);

        AbstractContextMenuBuilder contextMenuBuilder =
            new SystemViewElementContextMenuBuilder(actionManager, geckoView.getCurrentView(), systemViewModel);
        newSystemViewElement.setOnContextMenuRequested(
            event -> contextMenuBuilder.build().show(newSystemViewElement, event.getScreenX(), event.getScreenY()));

        return new SelectableViewElementDecorator(newSystemViewElement);
    }

    private EditorView createAutomatonEditorView(EditorViewModel editorViewModel) {
        return new EditorView(this, actionManager, editorViewModel, new AutomatonEditorViewShortcutHandler());
    }

    private EditorView createSystemEditorView(EditorViewModel editorViewModel) {
        return new EditorView(this, actionManager, editorViewModel, new SystemEditorViewShortcutHandler());
    }
}
