package org.gecko.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import org.gecko.actions.Action;
import org.gecko.view.menubar.MenuBarBuilder;
import org.gecko.view.views.EditorView;
import org.gecko.view.views.ViewFactory;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * Represents the View component of a Gecko project. Holds a {@link ViewFactory}, a current {@link EditorView} and a
 * reference to the {@link GeckoViewModel}. Contains methods for managing the {@link EditorView} shown in the graphic
 * editor.
 */
public class GeckoView {

    private static final String STYLE_SHEET_LIGHT = "/styles/gecko.css";
    private static final String STYLE_SHEET_DARK = "/styles/gecko-dark.css";


    @Getter
    private final BorderPane mainPane;
    private final TabPane centerPane;
    private final GeckoViewModel viewModel;
    private final ViewFactory viewFactory;
    private final MenuBar menuBar;

    @Getter
    private EditorView currentView;

    private final ArrayList<EditorView> openedViews;
    private boolean darkMode = false;

    public GeckoView(GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
        this.viewFactory = new ViewFactory(viewModel.getActionManager(), this);
        this.openedViews = new ArrayList<>();

        mainPane.getStylesheets()
            .add(Objects.requireNonNull(GeckoView.class.getResource(STYLE_SHEET_LIGHT)).toString());

        // Listener for current editor
        viewModel.getCurrentEditorProperty().addListener(this::onUpdateCurrentEditorFromViewModel);
        viewModel.getOpenedEditorsProperty().addListener(this::onOpenedEditorChanged);

        centerPane.setPickOnBounds(false);
        centerPane.getSelectionModel().selectedItemProperty().addListener(this::onUpdateCurrentEditorToViewModel);

        // Menubar
        menuBar = new MenuBarBuilder(this, viewModel.getActionManager()).build();
        mainPane.setTop(menuBar);

        // Initial view
        currentView = viewFactory.createEditorView(viewModel.getCurrentEditor(),
            viewModel.getCurrentEditor().isAutomatonEditor());
        constructTab(currentView, viewModel.getCurrentEditor());
        centerPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        centerPane.setPickOnBounds(false);
        refreshView();
    }

    private void onUpdateCurrentEditorFromViewModel(
        ObservableValue<? extends EditorViewModel> observable, EditorViewModel oldValue, EditorViewModel newValue) {
        for (EditorView editorView : openedViews) {
            if (editorView.getViewModel().equals(newValue)) {
                currentView = editorView;
                currentView.updateWorldSize();
                break;
            }
        }

        refreshView();
    }

    private void onOpenedEditorChanged(
        ObservableValue<? extends ObservableSet<EditorViewModel>> observable, ObservableSet<EditorViewModel> oldValue,
        ObservableSet<EditorViewModel> newValue) {
        if (newValue != null) {
            for (EditorViewModel editorViewModel : newValue) {
                if (openedViews.stream().anyMatch(editorView -> editorView.getViewModel().equals(editorViewModel))) {
                    continue;
                }

                EditorView newEditorView =
                    viewFactory.createEditorView(editorViewModel, editorViewModel.isAutomatonEditor());

                if (!openedViews.contains(newEditorView)) {
                    constructTab(newEditorView, editorViewModel);
                }
            }

            List<EditorViewModel> editorViewModelsToRemove = openedViews.stream()
                .map(EditorView::getViewModel)
                .filter(editorViewModel -> !newValue.contains(editorViewModel))
                .toList();
            if (!editorViewModelsToRemove.isEmpty()) {
                removeEditorViews(editorViewModelsToRemove);
            }
        }
        if (openedViews.size() == 1) {
            centerPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        } else {
            centerPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        }
    }

    private void removeEditorViews(List<EditorViewModel> editorViewModelsToRemove) {
        List<EditorView> editorViewsToRemove = openedViews.stream()
            .filter(editorView -> editorViewModelsToRemove.contains(editorView.getViewModel()))
            .toList();
        editorViewsToRemove.forEach(editorView -> {
            centerPane.getTabs().remove(editorView.getCurrentView());
        });
        openedViews.removeAll(editorViewsToRemove);
    }

    private void constructTab(EditorView editorView, EditorViewModel editorViewModel) {
        openedViews.add(editorView);
        centerPane.getTabs().add(editorView.getCurrentView());

        editorView.getCurrentView().setOnClosed(event -> {
            openedViews.remove(editorView);
            viewModel.getOpenedEditorsProperty().remove(editorViewModel);
        });
    }

    private void refreshView() {
        mainPane.setCenter(centerPane);

        // Focus current view
        centerPane.getSelectionModel().select(currentView.getCurrentView());

        mainPane.setLeft(currentView.drawToolbar());
        mainPane.setRight(currentView.drawInspector());
        currentView.getCurrentInspector().addListener((Observable observable) -> {
            mainPane.setRight(currentView.drawInspector());
        });

        currentView.updateWorldSize();
        currentView.focus();

        MenuBarBuilder.updateToolsMenu(menuBar, currentView.getViewModel().getTools());
    }

    private void onUpdateCurrentEditorToViewModel(
        ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
        if (newValue != null) {
            currentView = openedViews.stream()
                .filter(editorView -> editorView.getCurrentView() == newValue)
                .findFirst()
                .orElse(null);
            Action switchAction = viewModel.getActionManager()
                .getActionFactory()
                .createViewSwitchAction(currentView.getViewModel().getCurrentSystem(),
                    currentView.getViewModel().isAutomatonEditor());
            viewModel.getActionManager().run(switchAction);
        }
        refreshView();
    }

    /**
     * Returns all displayed elements in the current view.
     *
     * @return a set of all displayed elements in the current view
     */
    public Set<PositionableViewModelElement<?>> getAllDisplayedElements() {
        return viewModel.getCurrentEditor().getPositionableViewModelElements();
    }

    public void toggleAppearance() {
        darkMode = !darkMode;
        mainPane.getStylesheets().clear();
        mainPane.getStylesheets()
            .add(Objects.requireNonNull(GeckoView.class.getResource(darkMode ? STYLE_SHEET_DARK : STYLE_SHEET_LIGHT))
                .toString());
    }
}
