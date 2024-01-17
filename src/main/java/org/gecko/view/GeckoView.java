package org.gecko.view;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.view.menubar.MenuBarBuilder;
import org.gecko.view.views.EditorView;
import org.gecko.view.views.ViewFactory;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;

public class GeckoView {

    @Getter
    private final BorderPane mainPane;
    private final TabPane centerPane;
    private final GeckoViewModel viewModel;
    private final ViewFactory viewFactory;

    @Getter
    private EditorView currentView;

    public GeckoView(ActionManager actionManager, GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
        this.viewFactory = new ViewFactory(actionManager, this);

        // Listener for current editor
        viewModel.getCurrentEditorProperty().addListener(this::onNewEditorViewModel);

        // Menubar
        mainPane.setTop(new MenuBarBuilder(this, actionManager).build());

        // Initial view
        currentView = viewFactory.createEditorView(viewModel.getCurrentEditor(), viewModel.getCurrentEditor().isAutomatonEditor());

        refreshView();
    }

    private void onNewEditorViewModel(ObservableValue<? extends EditorViewModel> observable, EditorViewModel oldValue, EditorViewModel newValue) {
        if (newValue != null) {
            currentView = viewFactory.createEditorView(newValue, newValue.isAutomatonEditor());

            if (newValue != oldValue) {
                refreshView();
            }
        } else {
            currentView = null;
        }

        refreshView();
    }

    private void refreshView() {
        Tab tab = new Tab(viewModel.getCurrentEditor().getCurrentSystem().getName(), currentView.drawView());
        centerPane.getTabs().add(tab);

        mainPane.setCenter(centerPane);
        mainPane.setLeft(currentView.drawToolbar());
        mainPane.setRight(currentView.drawInspector());
    }
}
