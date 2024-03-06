package org.gecko.view.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.view.ResourceHandler;
import org.gecko.view.contextmenu.ViewContextMenuBuilder;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.InspectorFactory;
import org.gecko.view.toolbar.ToolBarBuilder;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;

/**
 * Represents a displayable view in the Gecko Graphic Editor, holding a collection of displayed {@link ViewElement}s and
 * other items specific to their vizualization.
 */
public class EditorView {
    private static final double AUTOMATON_INSPECTOR_HEIGHT = 680;

    private final ViewFactory viewFactory;
    @Getter
    private final EditorViewModel viewModel;
    @Getter
    private final Tab currentView;
    private final StackPane currentViewPane;
    @Getter
    private final ToolBar toolBar;
    private final ToolBarBuilder toolBarBuilder;
    private final InspectorFactory inspectorFactory;
    private final Inspector emptyInspector;
    private final Node searchWindow;

    private final ViewElementPane viewElementPane;

    @Getter
    private ObjectProperty<Inspector> currentInspector;

    private ShortcutHandler shortcutHandler;
    private ContextMenu contextMenu;

    public EditorView(
        ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel) {
        this.viewFactory = viewFactory;
        this.viewModel = viewModel;
        this.toolBarBuilder = new ToolBarBuilder(actionManager, this, viewModel);
        this.toolBar = toolBarBuilder.build();
        this.inspectorFactory = new InspectorFactory(actionManager, viewModel);

        this.currentViewPane = new StackPane();
        this.currentInspector = new SimpleObjectProperty<>(null);

        this.emptyInspector = new Inspector(new ArrayList<>(), actionManager);
        this.currentInspector = new SimpleObjectProperty<>(emptyInspector);
        this.viewElementPane = new ViewElementPane(viewModel);
        this.currentView = new Tab("Error_Name", currentViewPane);
        currentView.textProperty().bind(Bindings.createStringBinding(() -> {
            String name = viewModel.getCurrentSystem().getName();
            return name + (viewModel.isAutomatonEditor() ? " (" + ResourceHandler.getString("View", "automaton") + ")"
                : " (" + ResourceHandler.getString("View", "system") + ")");
        }, viewModel.getCurrentSystem().getNameProperty()));

        // Floating UI
        FloatingUIBuilder floatingUIBuilder = new FloatingUIBuilder(actionManager, viewModel, viewElementPane);
        Node zoomButtons = floatingUIBuilder.buildZoomButtons();
        AnchorPane.setBottomAnchor(zoomButtons, 18.0);
        AnchorPane.setRightAnchor(zoomButtons, 18.0);

        Node currentViewLabel = floatingUIBuilder.buildCurrentViewLabel();
        AnchorPane.setTopAnchor(currentViewLabel, 18.0);
        AnchorPane.setLeftAnchor(currentViewLabel, 15.0);

        Node viewSwitchButton = floatingUIBuilder.buildViewSwitchButtons();
        AnchorPane.setTopAnchor(viewSwitchButton, 18.0);
        AnchorPane.setRightAnchor(viewSwitchButton, 18.0);

        searchWindow = floatingUIBuilder.buildSearchWindow(this);
        activateSearchWindow(false);

        AnchorPane floatingUI = new AnchorPane();
        floatingUI.getChildren().addAll(zoomButtons, currentViewLabel, viewSwitchButton, searchWindow);
        floatingUI.setPickOnBounds(false);

        // Build stack pane
        currentViewPane.getChildren().addAll(viewElementPane.draw(), floatingUI);

        // View element creator listener
        viewModel.getContainedPositionableViewModelElementsProperty().addListener(this::onUpdateViewElements);

        // Inspector creator listener
        viewModel.getFocusedElementProperty().addListener(this::focusedElementChanged);
        viewModel.getSelectionManager().getCurrentSelectionProperty().addListener(this::selectionChanged);

        // Set current tool
        viewModel.getCurrentToolProperty().addListener(this::onToolChanged);

        ViewContextMenuBuilder contextMenuBuilder = new ViewContextMenuBuilder(viewModel.getActionManager(), viewModel);
        this.contextMenu = contextMenuBuilder.build();
        currentViewPane.setOnContextMenuRequested(event -> {
            changeContextMenu(contextMenuBuilder.getContextMenu());
            this.contextMenu.getItems()
                .stream()
                .filter(menuItem -> menuItem.getText().equals("Copy"))
                .findAny()
                .ifPresent(copyMenuItem -> copyMenuItem.setDisable(
                    viewModel.getSelectionManager().getCurrentSelection().isEmpty()));

            this.contextMenu.getItems()
                .stream()
                .filter(menuItem -> menuItem.getText().equals("Cut"))
                .findAny()
                .ifPresent(cutMenuItem -> cutMenuItem.setDisable(
                    viewModel.getSelectionManager().getCurrentSelection().isEmpty()));

            this.contextMenu.getItems()
                .stream()
                .filter(menuItem -> menuItem.getText().equals("Select All"))
                .findAny()
                .ifPresent(selectMenuItem -> selectMenuItem.setDisable(
                    viewModel.getContainedPositionableViewModelElementsProperty().isEmpty()));

            this.contextMenu.getItems()
                .stream()
                .filter(menuItem -> menuItem.getText().equals("Deselect All"))
                .findAny()
                .ifPresent(deselectMenuItem -> deselectMenuItem.setDisable(
                    viewModel.getSelectionManager().getCurrentSelection().isEmpty()));

            this.contextMenu.show(currentViewPane, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        initializeViewElements();
        acceptTool(viewModel.getCurrentTool());
        focus();
    }

    /**
     * Set the shortcut handler for the editor view. This will be used to handle keyboard shortcuts.
     *
     * @param shortcutHandler the shortcut handler
     */
    public void setShortcutHandler(ShortcutHandler shortcutHandler) {
        this.shortcutHandler = shortcutHandler;
        currentViewPane.addEventHandler(KeyEvent.ANY, shortcutHandler);
        toolBar.addEventHandler(KeyEvent.ANY, shortcutHandler);
        if (currentInspector.get() != null) {
            currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
        }
    }

    /**
     * Focus the center pane of the editor view.
     */
    public void focus() {
        currentViewPane.requestFocus();
    }

    /**
     * Draw the toolbar of the editor view.
     *
     * @return the toolbar
     */
    public Node drawToolbar() {
        toolBar.addEventHandler(KeyEvent.ANY, shortcutHandler);
        return toolBar;
    }

    /**
     * Draw the inspector of the editor view.
     *
     * @return the inspector
     */
    public Node drawInspector() {
        if (!viewModel.isAutomatonEditor()) {
            currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
            return currentInspector.get();
        } else {
            VBox vbox = new VBox();
            vbox.addEventHandler(KeyEvent.ANY, shortcutHandler);
            Inspector currentInspectorNode = currentInspector.get();
            currentInspectorNode.setPrefHeight(AUTOMATON_INSPECTOR_HEIGHT);
            VBox inspectorBox = new VBox(currentInspectorNode);
            VBox.setVgrow(inspectorBox, Priority.ALWAYS);

            ScrollPane automatonVariablePane = inspectorFactory.createAutomatonVariablePane();
            vbox.getChildren().addAll(inspectorBox, automatonVariablePane);
            return vbox;
        }
    }

    /**
     * Activate or deactivate the floating search window.
     *
     * @param activate true if the search window should be activated, false otherwise
     */
    public void activateSearchWindow(boolean activate) {
        AnchorPane.setTopAnchor(searchWindow, currentViewPane.getHeight() / 2);
        AnchorPane.setLeftAnchor(searchWindow, currentViewPane.getWidth() / 2);
        searchWindow.setVisible(activate);
        searchWindow.requestFocus();
    }

    public void toggleSearchWindow() {
        activateSearchWindow(!searchWindow.isVisible());
    }

    private void onUpdateViewElements(SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            addElement(change.getElementAdded());
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                viewElementPane.removeElement(viewElement);
            }
        }
        postUpdate();
    }

    private void postUpdate() {
        if (viewModel.getCurrentToolType() != null) {
            for (ViewElement<?> viewElement : viewElementPane.getElements()) {
                viewElement.accept(getViewModel().getCurrentTool());
            }
        }
    }

    private void initializeViewElements() {
        viewModel.getContainedPositionableViewModelElementsProperty().forEach(this::addElement);
        postUpdate();
    }

    private void addElement(PositionableViewModelElement<?> element) {
        PositionableViewModelElementVisitor visitor = new ViewElementCreatorVisitor(viewFactory);
        ViewElement<?> viewElement = (ViewElement<?>) element.accept(visitor);

        // Add view element to current view elements
        viewElementPane.addElement(viewElement);
        if (viewModel.getCurrentToolType() != null) {
            viewElement.accept(getViewModel().getCurrentTool());
        }
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return viewElementPane.findViewElement(element);
    }

    private void onToolChanged(ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) {
        acceptTool(newValue);
    }

    private void acceptTool(Tool tool) {
        tool.visitView(viewElementPane);
        viewElementPane.getElements().forEach(element -> element.accept(tool));
    }

    private void focusedElementChanged(
        ObservableValue<? extends PositionableViewModelElement<?>> observable, PositionableViewModelElement<?> oldValue,
        PositionableViewModelElement<?> newValue) {
        Inspector newInspector = inspectorFactory.createInspector(newValue);
        currentInspector.set((newInspector != null) ? newInspector : emptyInspector);
        if (shortcutHandler != null) {
            currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
        }
    }

    private void selectionChanged(
        ObservableValue<? extends Set<PositionableViewModelElement<?>>> observable,
        Set<PositionableViewModelElement<?>> oldValue, Set<PositionableViewModelElement<?>> newValue) {

        List<PositionableViewModelElement<?>> toRemove = new ArrayList<>();
        for (PositionableViewModelElement<?> element : oldValue) {
            ViewElement<?> viewElement = findViewElement(element);
            if (viewElement == null) {
                toRemove.add(element);
            }
        }
        toRemove.forEach(oldValue::remove);

        oldValue.stream().map(this::findViewElement).forEach(viewElement -> viewElement.setSelected(false));
        newValue.stream().map(this::findViewElement).forEach(viewElement -> viewElement.setSelected(true));
    }

    protected void switchToCursorTool() {
        toolBar.getItems()
            .stream()
            .filter(button -> ((ToggleButton) button).getText().equals("Cursor Tool"))
            .findFirst()
            .ifPresent(cursorButton -> ((ToggleButton) cursorButton).fire());
    }

    public void changeContextMenu(ContextMenu contextMenu) {
        if (this.contextMenu != null) {
            this.contextMenu.hide();
        }
        this.contextMenu = contextMenu;
    }
}
