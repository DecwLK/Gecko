package org.gecko.view.views;

import java.util.Collection;
import java.util.HashSet;
import javafx.beans.binding.Bindings;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private final ScrollPane viewElementsPaneContainer;
    private final Group viewElementsGroup;

    private Inspector currentInspector;

    public EditorView(ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel, ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = new ToolBarBuilder(actionManager, this, viewModel).build();
        this.shortcutHandler = shortcutHandler;
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);
        this.currentViewPane = new StackPane();
        this.viewElementsGroup = new Group();
        currentViewElements = new HashSet<>();
        currentView = new Tab(viewModel.getCurrentSystem().getName(), currentViewPane);

        // Construct view elements pane container
        VBox viewElementsGroupContainer = new VBox(new Group(viewElementsGroup));
        viewElementsGroupContainer.setAlignment(Pos.CENTER);
        this.viewElementsPaneContainer = new ScrollPane(viewElementsGroupContainer);

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
        currentViewPane.getChildren().addAll(viewElementsPaneContainer, floatingUI);

        // View element creator listener
        viewModel.getContainedPositionableViewModelElementsProperty().addListener((SetChangeListener<PositionableViewModelElement<?>>) change -> {
            onUpdateViewElements(viewFactory, change);
        });

        // Bind view elements pane with zoom scale property
        viewElementsGroup.scaleXProperty().bind(viewModel.getZoomScaleProperty());
        viewElementsGroup.scaleYProperty().bind(viewModel.getZoomScaleProperty());


        viewElementsPaneContainer.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getPivotXProperty()
                     .setValue(newValue.doubleValue() * viewElementsPaneContainer.getContent().getBoundsInLocal().getWidth()
                         - viewElementsPaneContainer.getContent().getBoundsInLocal().getWidth() / 2);
        });

        viewElementsPaneContainer.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getPivotYProperty()
                     .setValue(newValue.doubleValue() * viewElementsPaneContainer.getContent().getBoundsInLocal().getHeight()
                         - viewElementsPaneContainer.getContent().getBoundsInLocal().getHeight() / 2);
        });


        viewElementsPaneContainer.setPannable(true);
        viewElementsPaneContainer.setFitToWidth(true);
        viewElementsPaneContainer.setFitToHeight(true);

        // Debug label for pivot //////////////////
        Label pivotLabel = new Label();
        pivotLabel.textProperty()
                  .bind(Bindings.createStringBinding(
                      () -> "Pivot: " + viewModel.getPivotXProperty().getValue() + " : " + viewModel.getPivotYProperty().getValue(),
                      viewModel.getPivotXProperty(), viewModel.getPivotYProperty()));


        AnchorPane.setTopAnchor(pivotLabel, 30.0);
        AnchorPane.setLeftAnchor(pivotLabel, 10.0);
        floatingUI.getChildren().add(pivotLabel);
        ///////////////////////////////////////////

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
            viewElementsGroup.getChildren().add(viewElement.drawElement());
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                currentViewElements.remove(viewElement);
                viewElementsGroup.getChildren().remove(viewElement.drawElement());
            }
        }
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return currentViewElements.stream().filter(viewElement -> viewElement.getTarget().equals(element)).findFirst().orElse(null);
    }

    public void acceptTool(Tool tool) {
        tool.visitView(viewElementsPaneContainer);
        currentViewElements.forEach(viewElement -> viewElement.accept(tool));
    }
}
