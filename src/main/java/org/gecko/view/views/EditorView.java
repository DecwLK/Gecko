package org.gecko.view.views;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.InspectorFactory;
import org.gecko.view.toolbar.ToolBarBuilder;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;

public class EditorView {

    @Getter
    private final Collection<ViewElement<?>> currentViewElements;
    @Getter
    private final EditorViewModel viewModel;
    @Getter
    private final Tab currentView;
    private final StackPane currentViewPane;
    private final ToolBar toolBar;
    private final ShortcutHandler shortcutHandler;
    private final InspectorFactory inspectorFactory;
    private final ScrollPane viewElementsScrollPane;
    private final Pane viewElementsPaneContainer;
    private final Group viewElementsGroup;

    private Inspector currentInspector;
    private Group inspectorPanel;

    public EditorView(ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel, ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = new ToolBarBuilder(actionManager, this, viewModel).build();
        this.shortcutHandler = shortcutHandler;
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);
        this.currentViewPane = new StackPane();
        this.viewElementsGroup = new Group();
        this.inspectorPanel = new Group();
        currentViewElements = new HashSet<>();
        String baseName = viewModel.getCurrentSystem().getName();
        currentView = new Tab(baseName + (viewModel.isAutomatonEditor() ? " (Automaton)" : " (System)"), currentViewPane);

        // Construct view elements pane container
        viewElementsPaneContainer = new Pane(new Group(viewElementsGroup));
        this.viewElementsScrollPane = new ScrollPane(viewElementsPaneContainer);

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
        floatingUI.setPickOnBounds(false);

        // Build stack pane
        currentViewPane.getChildren().addAll(viewElementsScrollPane, floatingUI);

        // View element creator listener
        viewModel.getContainedPositionableViewModelElementsProperty().addListener((SetChangeListener<PositionableViewModelElement<?>>) change -> {
            onUpdateViewElements(viewFactory, change);
        });

        // Bind view elements pane with zoom scale property
        //viewElementsGroup.scaleXProperty().bind(viewModel.getZoomScaleProperty());
        //viewElementsGroup.scaleYProperty().bind(viewModel.getZoomScaleProperty());
        viewModel.getScaleProperty().addListener((observable, oldValue, newValue) -> {
            viewElementsGroup.getTransforms().setAll(newValue);
        });

        viewElementsScrollPane.hvalueProperty().bindBidirectional(viewModel.getHValueProperty());
        viewElementsScrollPane.vvalueProperty().bindBidirectional(viewModel.getVValueProperty());
        viewElementsPaneContainer.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getWorldSizeProperty().setValue(new Point2D(newValue.getWidth(), newValue.getHeight()));
        });
        viewElementsScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getViewSizeProperty().setValue(new Point2D(newValue.getWidth(), newValue.getHeight()));
            updatePaneContainerSize(newValue.getWidth(), newValue.getHeight());
        });

        //viewElementsScrollPane.setFitToWidth(true);
        //viewElementsScrollPane.setFitToHeight(true);
        viewElementsScrollPane.setPannable(true);
        viewElementsGroup.setAutoSizeChildren(true);
        viewElementsGroup.setManaged(true);

        // Debug label for pivot //////////////////
        /*Label pivotLabel = new Label();
        pivotLabel.textProperty()
                  .bind(Bindings.createStringBinding(
                      () -> "Pivot: " + viewModel.gethValueProperty().getValue() + " : " + viewModel.getvValueProperty().getValue(),
                      viewModel.gethValueProperty(), viewModel.getvValueProperty()));


        AnchorPane.setTopAnchor(pivotLabel, 30.0);
        AnchorPane.setLeftAnchor(pivotLabel, 10.0);
        floatingUI.getChildren().add(pivotLabel);*/
        ///////////////////////////////////////////

        // Inspector creator listener
        viewModel.getFocusedElementProperty().addListener(this::focusedElementChanged);

        viewModel.getSelectionManager().getCurrentSelection().addListener(this::selectionChanged);

        // Set current tool
        viewModel.getCurrentToolProperty().addListener(this::onToolChanged);
    }

    public boolean toggleInspector() {
        if (currentInspector != null) {
            return currentInspector.toggleCollapse();
        }

        return false;
    }

    public Node drawToolbar() {
        return toolBar;
    }

    public Node drawInspector() {
        return inspectorPanel;
    }

    private void onUpdateViewElements(ViewFactory viewFactory, SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            // Create new view element
            PositionableViewModelElementVisitor visitor = new ViewElementCreatorVisitor(viewFactory);
            ViewElement<?> viewElement = (ViewElement<?>) change.getElementAdded().accept(visitor);

            // Add view element to current view elements
            currentViewElements.add(viewElement);
            if (viewModel.getCurrentTool() != null) {
                viewElement.accept(getViewModel().getCurrentTool());
            }
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                currentViewElements.remove(viewElement);
            }
        }
        orderChildren();
        viewElementsScrollPane.requestLayout();
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return currentViewElements.stream().filter(viewElement -> viewElement.getTarget().equals(element)).findFirst().orElse(null);
    }

    private void onToolChanged(ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) {
        newValue.visitView(viewElementsScrollPane);
        currentViewElements.forEach(viewElement -> viewElement.accept(newValue));
    }

    private void focusedElementChanged(ObservableValue<? extends PositionableViewModelElement<?>> observable,
                                       PositionableViewModelElement<?> oldValue, PositionableViewModelElement<?> newValue) {
        if (newValue != null) {
            inspectorPanel.getChildren().clear();
            currentInspector = inspectorFactory.createInspector(newValue);
            inspectorPanel.getChildren().add(currentInspector);
        } else {
            inspectorPanel.getChildren().clear();
            currentInspector = null;
        }
    }

    private void updatePaneContainerSize(double width, double height) {
        viewElementsPaneContainer.setMinSize(width, height);
    }

    private void selectionChanged(SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            ViewElement<?> viewElement = findViewElement(change.getElementAdded());
            if (viewElement != null) {
                viewElement.setSelected(true);
            }
        }
        if (change.wasRemoved()) {
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                viewElement.setSelected(false);
            }
        }
        orderChildren();
    }

    private void orderChildren() {
        viewElementsGroup.getChildren()
                         .setAll(currentViewElements.stream()
                                                    .sorted(Comparator.comparingInt(ViewElement::getZPriority))
                                                    .map(ViewElement::drawElement)
                                                    .toList());
    }
}
