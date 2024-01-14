package org.gecko.view.views;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.InspectorFactory;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.viewmodel.EditorViewModel;

public class EditorView {
    private final EditorViewModel viewModel;
    private final ToolBar toolBar;
    private final ShortcutHandler shortcutHandler;
    private final InspectorFactory inspectorFactory;

    private Inspector currentInspector;
    private Pane currentView;

    public EditorView(
            ActionManager actionManager,
            EditorViewModel viewModel,
            ToolBar toolBar,
            ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = toolBar;
        this.shortcutHandler = shortcutHandler;
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);

        viewModel
                .getFocusedElement()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != null) {
                                currentInspector = inspectorFactory.createInspector(newValue);
                            } else {
                                currentInspector = null;
                            }
                        });
    }

    public void toggleInspector() {
        if (currentInspector != null) {
            currentInspector.toggleCollapse();
        }
    }

    public Node drawView() {
        return currentView;
    }

    public Node drawToolbar() {
        return toolBar;
    }

    public Node drawInspector() {
        return currentInspector;
    }
}
