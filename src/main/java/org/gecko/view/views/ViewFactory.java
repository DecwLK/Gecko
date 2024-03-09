package org.gecko.view.views;

import javafx.scene.Node;
import org.gecko.actions.ActionManager;
import org.gecko.tools.ToolType;
import org.gecko.view.GeckoView;
import org.gecko.view.contextmenu.EdgeViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.RegionViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.StateViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.SystemConnectionViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.SystemViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.VariableBlockViewElementContextMenuBuilder;
import org.gecko.view.contextmenu.ViewContextMenuBuilder;
import org.gecko.view.views.shortcuts.AutomatonEditorViewShortcutHandler;
import org.gecko.view.views.shortcuts.SystemEditorViewShortcutHandler;
import org.gecko.view.views.viewelement.EdgeViewElement;
import org.gecko.view.views.viewelement.RegionViewElement;
import org.gecko.view.views.viewelement.StateViewElement;
import org.gecko.view.views.viewelement.SystemConnectionViewElement;
import org.gecko.view.views.viewelement.SystemViewElement;
import org.gecko.view.views.viewelement.VariableBlockViewElement;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.view.views.viewelement.decorator.BlockElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.ConnectionElementScalerViewElementDecorator;
import org.gecko.view.views.viewelement.decorator.SelectableViewElementDecorator;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a factory for the view elements of a Gecko project. Provides a method for the creation of each element.
 */
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

        ViewContextMenuBuilder contextMenuBuilder =
            new StateViewElementContextMenuBuilder(actionManager, stateViewModel);
        setContextMenu(newStateViewElement, contextMenuBuilder);
        return new SelectableViewElementDecorator(newStateViewElement);
    }

    public ViewElement<?> createViewElementFrom(RegionViewModel regionViewModel) {
        RegionViewElement newRegionViewElement = new RegionViewElement(regionViewModel);

        ViewContextMenuBuilder contextMenuBuilder =
            new RegionViewElementContextMenuBuilder(actionManager, regionViewModel);
        setContextMenu(newRegionViewElement, contextMenuBuilder);
        return new BlockElementScalerViewElementDecorator(new SelectableViewElementDecorator(newRegionViewElement));
    }

    public ViewElement<?> createViewElementFrom(PortViewModel portViewModel) {
        VariableBlockViewElement newVariableBlockViewElement = new VariableBlockViewElement(portViewModel);

        ViewContextMenuBuilder contextMenuBuilder =
            new VariableBlockViewElementContextMenuBuilder(actionManager, portViewModel);
        setContextMenu(newVariableBlockViewElement, contextMenuBuilder);

        return new SelectableViewElementDecorator(newVariableBlockViewElement);
    }

    public ViewElement<?> createViewElementFrom(EdgeViewModel edgeViewModel) {
        EdgeViewElement newEdgeViewElement = new EdgeViewElement(edgeViewModel);

        ViewContextMenuBuilder contextMenuBuilder = new EdgeViewElementContextMenuBuilder(actionManager, edgeViewModel);
        setContextMenu(newEdgeViewElement, contextMenuBuilder);

        return new ConnectionElementScalerViewElementDecorator(newEdgeViewElement);
    }

    public ViewElement<?> createViewElementFrom(SystemConnectionViewModel systemConnectionViewModel) {
        SystemConnectionViewElement newSystemConnectionViewElement =
            new SystemConnectionViewElement(systemConnectionViewModel);

        ViewContextMenuBuilder contextMenuBuilder =
            new SystemConnectionViewElementContextMenuBuilder(actionManager, systemConnectionViewModel);
        setContextMenu(newSystemConnectionViewElement, contextMenuBuilder);

        return new ConnectionElementScalerViewElementDecorator(newSystemConnectionViewElement);
    }

    public ViewElement<?> createViewElementFrom(SystemViewModel systemViewModel) {
        SystemViewElement newSystemViewElement = new SystemViewElement(systemViewModel);

        ViewContextMenuBuilder contextMenuBuilder =
            new SystemViewElementContextMenuBuilder(actionManager, systemViewModel);
        setContextMenu(newSystemViewElement, contextMenuBuilder);

        return new SelectableViewElementDecorator(newSystemViewElement);
    }

    private void setContextMenu(Node newViewElement, ViewContextMenuBuilder contextMenuBuilder) {
        newViewElement.setOnContextMenuRequested(event -> {
            actionManager.run(actionManager.getActionFactory().createSelectToolAction(ToolType.CURSOR));
            geckoView.getCurrentView().changeContextMenu(contextMenuBuilder.build());
            contextMenuBuilder.getContextMenu().show(newViewElement, event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    private EditorView createAutomatonEditorView(EditorViewModel editorViewModel) {
        EditorView editorView = new EditorView(this, actionManager, editorViewModel);
        editorView.setShortcutHandler(new AutomatonEditorViewShortcutHandler(actionManager, editorView));
        return editorView;
    }

    private EditorView createSystemEditorView(EditorViewModel editorViewModel) {
        EditorView editorView = new EditorView(this, actionManager, editorViewModel);
        editorView.setShortcutHandler(new SystemEditorViewShortcutHandler(actionManager, editorView));
        return editorView;
    }
}
