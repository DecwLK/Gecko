package org.gecko.view.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
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
    private final VBox viewElementsVBoxContainer;
    private final Group viewElementsGroupContainer;
    private final Group viewElementsGroup;
    private final Pane placeholder;
    private final Property<Point2D> viewPortPositionViewProperty;
    private final ChangeListener<Point2D> worldSizeUpdateListener;

    private final Inspector emptyInspector;

    @Getter
    private ObjectProperty<Inspector> currentInspector;

    public EditorView(
        ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel,
        ShortcutHandler shortcutHandler) {
        this.viewModel = viewModel;
        this.toolBar = new ToolBarBuilder(actionManager, this, viewModel).build();
        this.shortcutHandler = shortcutHandler;
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);

        this.viewElementsGroup = new Group();
        this.viewElementsGroupContainer = new Group(viewElementsGroup);
        this.viewElementsVBoxContainer = new VBox(viewElementsGroupContainer);
        this.viewElementsScrollPane = new ScrollPane(viewElementsVBoxContainer);
        this.placeholder = new Pane();
        this.currentViewPane = new StackPane();
        this.viewPortPositionViewProperty = new SimpleObjectProperty<>(new Point2D(0, 0));

        this.currentInspector = new SimpleObjectProperty<>(null);

        emptyInspector = new Inspector(new ArrayList<>(), actionManager);
        this.currentInspector = new SimpleObjectProperty<>(emptyInspector);
        currentViewElements = new HashSet<>();
        String baseName = viewModel.getCurrentSystem().getName();
        currentView =
            new Tab(baseName + (viewModel.isAutomatonEditor() ? " (Automaton)" : " (System)"), currentViewPane);

        this.worldSizeUpdateListener = (observable, oldValue, newValue) -> {
            if (currentViewElements.stream().anyMatch(viewElement -> viewElement.getTarget().isCurrentlyModified())) {
                return;
            }
            updateWorldSize(newValue);
        };

        // Construct view elements pane container
        viewElementsVBoxContainer.setAlignment(Pos.CENTER);
        viewElementsGroup.getChildren().add(placeholder);

        viewElementsScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            placeholder.setMinSize(viewElementsScrollPane.getViewportBounds().getWidth(),
                viewElementsScrollPane.getViewportBounds().getHeight());
        });
        placeholder.setMouseTransparent(true);
        viewElementsScrollPane.layout();


        // Floating UI
        FloatingUIBuilder floatingUIBuilder = new FloatingUIBuilder(actionManager, viewModel);
        Node zoomButtons = floatingUIBuilder.buildZoomButtons();
        AnchorPane.setBottomAnchor(zoomButtons, 18.0);
        AnchorPane.setRightAnchor(zoomButtons, 18.0);

        Node currentViewLabel = floatingUIBuilder.buildCurrentViewLabel();
        AnchorPane.setTopAnchor(currentViewLabel, 18.0);
        AnchorPane.setLeftAnchor(currentViewLabel, 15.0);

        Node viewSwitchButton = floatingUIBuilder.buildViewSwitchButtons();
        AnchorPane.setTopAnchor(viewSwitchButton, 18.0);
        AnchorPane.setRightAnchor(viewSwitchButton, 18.0);

        AnchorPane floatingUI = new AnchorPane();
        floatingUI.getChildren().addAll(zoomButtons, currentViewLabel, viewSwitchButton);
        floatingUI.setPickOnBounds(false);

        // Build stack pane
        currentViewPane.getChildren().addAll(viewElementsScrollPane, floatingUI);

        // View element creator listener
        viewModel.getContainedPositionableViewModelElementsProperty()
            .addListener((SetChangeListener<PositionableViewModelElement<?>>) change -> {
                onUpdateViewElements(viewFactory, change);
            });


        // Inspector creator listener
        viewModel.getFocusedElementProperty().addListener(this::focusedElementChanged);
        viewModel.getSelectionManager().getCurrentSelectionProperty().addListener(this::selectionChanged);

        // Set current tool
        viewModel.getCurrentToolProperty().addListener(this::onToolChanged);


        // Viewport position listener
        viewModel.getRequestedViewPortPositionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            setViewPortPosition(newValue);
            viewPortPositionViewProperty.setValue(newValue);
            viewModel.getRequestedViewPortPositionProperty().setValue(null);
        });
        viewElementsScrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
            calculateViewPortPosition();
        });
        viewElementsScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            calculateViewPortPosition();
        });
        viewModel.getViewPortPositionProperty().bindBidirectional(viewPortPositionViewProperty);

        viewModel.getZoomScaleProperty().addListener((observable, oldValue, newValue) -> {
            Scale scale = new Scale(newValue.doubleValue(), newValue.doubleValue(), 0, 0);
            viewElementsGroup.getTransforms().setAll(scale);
            viewElementsScrollPane.layout();
        });

        viewElementsScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getViewPortSizeProperty().setValue(new Point2D(newValue.getWidth(), newValue.getHeight()));
        });

        viewElementsGroup.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.getWorldSizeProperty().setValue(new Point2D(newValue.getWidth(), newValue.getHeight()));
        });
    }

    private void setViewPortPosition(Point2D point) {
        Point2D viewPortSize = convertParentToLocal(new Point2D(viewElementsScrollPane.getViewportBounds().getWidth(),
            viewElementsScrollPane.getViewportBounds().getHeight()));
        Point2D worldSize = new Point2D(viewElementsGroup.getLayoutBounds().getWidth(),
            viewElementsGroup.getLayoutBounds().getHeight());
        Point2D worldOffset = getWorldOffset();
        point = point.subtract(worldOffset);
        double hValue = point.getX() / (worldSize.getX() + worldOffset.getX() - viewPortSize.getX());
        double vValue = point.getY() / (worldSize.getY() + worldOffset.getY() - viewPortSize.getY());
        viewElementsScrollPane.setHvalue(hValue);
        viewElementsScrollPane.setVvalue(vValue);
        viewElementsScrollPane.layout();
    }

    private void calculateViewPortPosition() {
        Point2D viewPortSize = convertParentToLocal(new Point2D(viewElementsScrollPane.getViewportBounds().getWidth(),
            viewElementsScrollPane.getViewportBounds().getHeight()));
        Point2D worldSize = new Point2D(viewElementsGroup.getLayoutBounds().getWidth(),
            viewElementsGroup.getLayoutBounds().getHeight());
        Point2D worldOffset = getWorldOffset();
        double hValue = viewElementsScrollPane.getHvalue();
        double vValue = viewElementsScrollPane.getVvalue();
        viewPortPositionViewProperty.setValue(
            new Point2D(hValue * (worldSize.getX() + worldOffset.getX() - viewPortSize.getX()),
                vValue * (worldSize.getY() + worldOffset.getY() - viewPortSize.getY())).add(worldOffset));
    }

    private Point2D getWorldOffset() {
        return viewElementsGroup.parentToLocal(viewElementsGroupContainer.parentToLocal(new Point2D(0, 0)));
    }

    public Node drawToolbar() {
        return toolBar;
    }

    public Node drawInspector() {
        return currentInspector.get();
    }

    private void onUpdateViewElements(
        ViewFactory viewFactory, SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            // Create new view element
            PositionableViewModelElementVisitor visitor = new ViewElementCreatorVisitor(viewFactory);
            ViewElement<?> viewElement = (ViewElement<?>) change.getElementAdded().accept(visitor);

            // Add view element to current view elements
            currentViewElements.add(viewElement);
            if (viewModel.getCurrentTool() != null) {
                viewElement.accept(getViewModel().getCurrentTool());
            }

            // TODO: not center
            viewElement.getTarget().getPositionProperty().addListener(worldSizeUpdateListener);
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                currentViewElements.remove(viewElement);
            }
        }
        orderChildren();
        viewElementsScrollPane.layout();
        calculateViewPortPosition();

        if (viewModel.getCurrentTool() != null) {
            for (ViewElement<?> viewElement : currentViewElements) {
                viewElement.accept(getViewModel().getCurrentTool());
            }
        }

        viewElementsScrollPane.requestLayout();
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return currentViewElements.stream()
            .filter(viewElement -> viewElement.getTarget().equals(element))
            .findFirst()
            .orElse(null);
    }

    private void onToolChanged(ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) {
        newValue.visitView(viewElementsScrollPane);
        currentViewElements.forEach(viewElement -> viewElement.accept(newValue));
    }

    private void focusedElementChanged(
        ObservableValue<? extends PositionableViewModelElement<?>> observable, PositionableViewModelElement<?> oldValue,
        PositionableViewModelElement<?> newValue) {
        currentInspector.set((newValue != null) ? inspectorFactory.createInspector(newValue) : emptyInspector);
    }

    private void selectionChanged(
        ObservableValue<? extends Set<PositionableViewModelElement<?>>> observable,
        Set<PositionableViewModelElement<?>> oldValue, Set<PositionableViewModelElement<?>> newValue) {

        for (PositionableViewModelElement<?> element : oldValue) {
            ViewElement<?> viewElement = findViewElement(element);
            if (viewElement == null) {
                oldValue.remove(element);
            }
        }

        oldValue.stream().map(this::findViewElement).forEach(viewElement -> viewElement.setSelected(false));
        newValue.stream().map(this::findViewElement).forEach(viewElement -> viewElement.setSelected(true));
        orderChildren();
    }

    private void orderChildren() {
        List<Node> currentElements = currentViewElements.stream()
            .sorted(Comparator.comparingInt(ViewElement::getZPriority))
            .map(ViewElement::drawElement)
            .collect(Collectors.toList());

        currentElements.add(placeholder);
        viewElementsGroup.getChildren().setAll(currentElements);
        viewElementsScrollPane.layout();
    }

    private Point2D convertParentToLocal(Point2D point) {
        return viewElementsGroup.parentToLocal(
            viewElementsGroupContainer.parentToLocal(viewElementsVBoxContainer.parentToLocal(point)));
    }

    private void updateWorldSize(Point2D newElementPosition) {

        Bounds bound = viewElementsGroup.getLayoutBounds();
        double widthBorder = viewElementsScrollPane.getViewportBounds().getWidth() / 4;
        double heightBorder = viewElementsScrollPane.getViewportBounds().getHeight() / 4;

        if (newElementPosition.getX() <= bound.getMinX() + widthBorder
            || newElementPosition.getX() >= bound.getMaxX() - widthBorder
            || newElementPosition.getY() <= bound.getMinY() + heightBorder
            || newElementPosition.getY() >= bound.getMaxY() - heightBorder) {
            double increment = Math.max(viewElementsScrollPane.getViewportBounds().getHeight(),
                viewElementsScrollPane.getViewportBounds().getWidth());
            placeholder.setPrefSize(viewElementsGroup.getLayoutBounds().getWidth() + increment,
                viewElementsGroup.getLayoutBounds().getHeight() + increment);
            viewElementsScrollPane.layout();
            currentViewElements.forEach(viewElement -> {
                viewElement.getTarget().getPositionProperty().removeListener(worldSizeUpdateListener);
            });
            currentViewElements.forEach(viewElement -> {
                Point2D position = viewElement.getPosition();
                viewElement.getTarget()
                    .getPositionProperty()
                    .setValue(new Point2D(position.getX() + increment / 2, position.getY() + increment / 2));
            });
            currentViewElements.forEach(viewElement -> {
                viewElement.getTarget().getPositionProperty().addListener(worldSizeUpdateListener);
            });
            setViewPortPosition(new Point2D(viewPortPositionViewProperty.getValue().getX() + increment / 2,
                viewPortPositionViewProperty.getValue().getY() + increment / 2));
            viewElementsScrollPane.layout();
        }
    }
}
