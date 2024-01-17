package org.gecko.view;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

/**
 * Represents the View component of a Gecko project. Holds a {@link ViewFactory}, a current {@link EditorView} and a reference to the
 * {@link GeckoViewModel}. Contains methods for managing the {@link EditorView} shown in the graphic editor.
 */
public class GeckoView {

    @Getter
    private final BorderPane mainPane;
    private final TabPane centerPane;
    private final GeckoViewModel viewModel;
    private final ViewFactory viewFactory;

    @Getter
    private EditorView currentView;

    private List<EditorView> openedViews;

    public GeckoView(ActionManager actionManager, GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
        this.viewFactory = new ViewFactory(actionManager, this);
        this.openedViews = new ArrayList<>();

        // Listener for current editor
        viewModel.getCurrentEditorProperty().addListener(this::onUpdateCurrentEditorFromViewModel);
        viewModel.getOpenedEditorsProperty().addListener(this::onOpenedEditorChanged);

        // Menubar
        mainPane.setTop(new MenuBarBuilder(this, actionManager).build());

        // Initial view
        currentView = viewFactory.createEditorView(viewModel.getCurrentEditor(), viewModel.getCurrentEditor().isAutomatonEditor());
        centerPane.getTabs().add(currentView.getCurrentView());
        openedViews.add(currentView);

        centerPane.getSelectionModel().selectedItemProperty().addListener(this::onUpdateCurrentEditorToViewModel);

        refreshView();
    }

    private void onUpdateCurrentEditorFromViewModel(ObservableValue<? extends EditorViewModel> observable, EditorViewModel oldValue,
                                                    EditorViewModel newValue) {
        for (EditorView editorView : openedViews) {
            if (editorView.getViewModel() == newValue) {
                currentView = editorView;
                break;
            }
        }

        refreshView();
    }

    private void onOpenedEditorChanged(ObservableValue<? extends ObservableList<EditorViewModel>> observable,
                                       ObservableList<EditorViewModel> oldValue, ObservableList<EditorViewModel> newValue) {
        if (newValue != null) {
            for (EditorViewModel editorViewModel : newValue) {
                if (openedViews.stream().anyMatch(editorView -> editorView.getViewModel() == editorViewModel)) {
                    continue;
                }

                EditorView newEditorView = viewFactory.createEditorView(editorViewModel, editorViewModel.isAutomatonEditor());

                if (!openedViews.contains(newEditorView)) {
                    openedViews.add(newEditorView);
                    centerPane.getTabs().add(newEditorView.getCurrentView());
                }
            }
        }
    }

    private void refreshView() {
        mainPane.setCenter(centerPane);

        // Focus current view
        centerPane.getSelectionModel().select(currentView.getCurrentView());

        mainPane.setLeft(currentView.drawToolbar());
        mainPane.setRight(currentView.drawInspector());
    }

    private void onUpdateCurrentEditorToViewModel(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
        if (newValue != null) {
            currentView = openedViews.stream().filter(editorView -> editorView.getCurrentView() == newValue).findFirst().orElse(null);
            refreshView();
        }
    }
}
