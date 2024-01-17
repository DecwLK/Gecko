package org.gecko.view.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.gecko.actions.ActionManager;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.InspectorFactory;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;

public class EditorView {
    private final EditorViewModel viewModel;
    private final ToolBar toolBar;
    private final ShortcutHandler shortcutHandler;
    private final InspectorFactory inspectorFactory;
    private final StackPane currentView;
    private final Pane viewElementsPane;
    private final Collection<ViewElement<?>> currentViewElements;

    private Inspector currentInspector;

    public EditorView(ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel, ToolBar toolBar,
                      ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = toolBar;
        this.shortcutHandler = shortcutHandler;
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);
        this.currentView = new StackPane();
        this.viewElementsPane = new Pane();
        currentViewElements = new HashSet<>();

        // Floating UI
        FloatingUIBuilder floatingUIBuilder = new FloatingUIBuilder(actionManager, viewModel);
        Node zoomButtons = floatingUIBuilder.buildZoomButtons();
        AnchorPane.setBottomAnchor(zoomButtons, 10.0);
        AnchorPane.setRightAnchor(zoomButtons, 10.0);

        Node currentViewLabel = floatingUIBuilder.buildCurrentViewLabel();
        AnchorPane.setTopAnchor(currentViewLabel, 10.0);
        AnchorPane.setLeftAnchor(currentViewLabel, 10.0);

        Node viewSwitchButton = floatingUIBuilder.buildViewSwitchButton();
        AnchorPane.setTopAnchor(viewSwitchButton, 10.0);
        AnchorPane.setRightAnchor(viewSwitchButton, 10.0);

        AnchorPane floatingUI = new AnchorPane();
        floatingUI.getChildren().addAll(zoomButtons, currentViewLabel, viewSwitchButton);

        // Build stack pane
        currentView.getChildren().addAll(viewElementsPane, floatingUI);

        // View element creator listener
        viewModel.getContainedPositionableViewModelElementsProperty().addListener((SetChangeListener<PositionableViewModelElement<?>>) change -> {
            onUpdateViewElements(viewFactory, change);
        });

        // Inspector creator listener
        viewModel.getFocusedElementProperty().addListener((observable, oldValue, newValue) -> {
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
        // Refresh view elements
        viewElementsPane.getChildren().removeAll();
        viewElementsPane.getChildren().setAll(currentViewElements.stream().map(ViewElement::drawElement).collect(Collectors.toList()));

        return currentView;
    }

    public Node drawToolbar() {
        return toolBar;
    }

    public Node drawInspector() {
        return currentInspector;
    }

    private void onUpdateViewElements(ViewFactory viewFactory, SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            // Create new view element
            PositionableViewModelElementVisitor visitor = new ViewElementCreatorVisitor(viewFactory);
            ViewElement<?> viewElement = (ViewElement<?>) change.getElementAdded().accept(visitor);

            // Add view element to current view elements
            currentViewElements.add(viewElement);
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                currentViewElements.remove(viewElement);
            }
        }
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        for (ViewElement<?> viewElement : currentViewElements) {
            if (viewElement.getTarget().equals(element)) {
                return viewElement;
            }
        }
        return null;
    }
}
