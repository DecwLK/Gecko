package org.gecko.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
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
import org.gecko.viewmodel.SystemViewModel;

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
    private Property<EditorView> currentViewProperty;

    private final ArrayList<EditorView> openedViews;
    private boolean darkMode = false;

    public GeckoView(GeckoViewModel viewModel) {
        this.viewModel = viewModel;
        this.mainPane = new BorderPane();
        this.centerPane = new TabPane();
        this.viewFactory = new ViewFactory(viewModel.getActionManager(), this);
        this.openedViews = new ArrayList<>();
        this.currentViewProperty = new SimpleObjectProperty<>();

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
        currentViewProperty.setValue(viewFactory.createEditorView(viewModel.getCurrentEditor(),
            viewModel.getCurrentEditor().isAutomatonEditor()));
        constructTab(currentViewProperty.getValue(), viewModel.getCurrentEditor());
        centerPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        centerPane.setPickOnBounds(false);
        refreshView();
    }

    private void onUpdateCurrentEditorFromViewModel(
        ObservableValue<? extends EditorViewModel> observable, EditorViewModel oldValue, EditorViewModel newValue) {
        currentViewProperty.setValue(openedViews.stream()
            .filter(editorView -> editorView.getViewModel().equals(newValue))
            .findFirst()
            .orElseThrow());
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
        Tab tab = editorView.getCurrentView();
        Node graphic = tab.getGraphic();
        graphic.setOnMouseClicked(event -> handleUserTabChange(tab));
        centerPane.getTabs().add(tab);

        editorView.getCurrentView().setOnClosed(event -> {
            openedViews.remove(editorView);
            viewModel.getOpenedEditorsProperty().remove(editorViewModel);
        });
    }

    private void handleUserTabChange(Tab tab) {
        if (tab == centerPane.getSelectionModel().getSelectedItem()) {
            return;
        }
        currentViewProperty.setValue(getView(tab));
        SystemViewModel next = currentViewProperty.getValue().getViewModel().getCurrentSystem();
        Action switchAction = viewModel.getActionManager()
            .getActionFactory()
            .createViewSwitchAction(next, currentViewProperty.getValue().getViewModel().isAutomatonEditor());
        viewModel.getActionManager().run(switchAction);
    }

    private void refreshView() {
        mainPane.setCenter(centerPane);

        // Focus current view
        centerPane.getSelectionModel().select(currentViewProperty.getValue().getCurrentView());

        mainPane.setLeft(currentViewProperty.getValue().drawToolbar());
        mainPane.setRight(currentViewProperty.getValue().drawInspector());
        currentViewProperty.getValue().getCurrentInspector().addListener((Observable observable) -> {
            mainPane.setRight(currentViewProperty.getValue().drawInspector());
        });
        viewModel.switchEditor(currentViewProperty.getValue().getViewModel().getCurrentSystem(),
            currentViewProperty.getValue().getViewModel().isAutomatonEditor());

        currentViewProperty.getValue().focus();
    }

    private void onUpdateCurrentEditorToViewModel(
        ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
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

    private EditorView getView(Tab tab) {
        return openedViews.stream().filter(editorView -> editorView.getCurrentView() == tab).findFirst().orElseThrow();
    }

    public EditorView getCurrentView() {
        return currentViewProperty.getValue();
    }
}
