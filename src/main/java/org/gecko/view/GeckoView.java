package org.gecko.view;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.gecko.actions.ActionManager;
import org.gecko.view.menubar.MenuBarBuilder;
import org.gecko.view.views.EditorView;
import org.gecko.view.views.ViewFactory;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;

public class GeckoView {

    private final BorderPane mainPane;
    private final TabPane centerPane;
    private final GeckoViewModel viewModel;
    private final ViewFactory viewFactory;

    private EditorView currentView;

    public GeckoView(ActionManager actionManager, GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
        this.viewFactory = new ViewFactory(actionManager);

        // Listener for current editor
        viewModel.getCurrentEditorProperty().addListener(this::onNewEditorViewModel);

        // Menubar
        mainPane.setTop(MenuBarBuilder)
    }

    private void onNewEditorViewModel(ObservableValue<? extends EditorViewModel> observable, EditorViewModel oldValue, EditorViewModel newValue) {
        if (newValue != null) {
            currentView = viewFactory.createEditorView(newValue, newValue.isAutomatonEditor());
        } else {
            currentView = null;
        }

    }
}
