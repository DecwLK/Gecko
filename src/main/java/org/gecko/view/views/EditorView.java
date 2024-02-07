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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import lombok.Getter;
import org.gecko.actions.ActionManager;
import org.gecko.tools.Tool;
import org.gecko.view.ResourceHandler;
import org.gecko.view.inspector.Inspector;
import org.gecko.view.inspector.InspectorFactory;
import org.gecko.view.toolbar.ToolBarBuilder;
import org.gecko.view.views.shortcuts.ShortcutHandler;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;

public class EditorView {
    private static final double RELATIVE_BORDER_SIZE = 0.25;
    private static final double MIN_WORLD_SIZE = 1000;
    private static final double WORLD_SIZE_DELTA = 1000;

    @Getter
    private final Collection<ViewElement<?>> currentViewElements;
    private final ViewFactory viewFactory;
    @Getter
    private final EditorViewModel viewModel;
    @Getter
    private final Tab currentView;
    private final StackPane currentViewPane;
    private final ToolBar toolBar;
    private final ToolBarBuilder toolBarBuilder;
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

    private ShortcutHandler shortcutHandler;

    public EditorView(
        ViewFactory viewFactory, ActionManager actionManager, EditorViewModel viewModel) {
        this.viewFactory = viewFactory;
        this.viewModel = viewModel;
        this.toolBarBuilder = new ToolBarBuilder(actionManager, this, viewModel);
        this.toolBar = toolBarBuilder.build();
        this.inspectorFactory = new InspectorFactory(actionManager, this, viewModel);

        this.viewElementsGroup = new Group();
        this.viewElementsGroupContainer = new Group(viewElementsGroup);
        this.viewElementsVBoxContainer = new VBox(viewElementsGroupContainer);
        this.viewElementsScrollPane = new ScrollPane(viewElementsVBoxContainer);
        this.placeholder = new Pane();
        this.currentViewPane = new StackPane();
        this.viewPortPositionViewProperty = new SimpleObjectProperty<>(new Point2D(0, 0));

        this.currentInspector = new SimpleObjectProperty<>(null);

        this.emptyInspector = new Inspector(new ArrayList<>(), actionManager);
        this.currentInspector = new SimpleObjectProperty<>(emptyInspector);
        this.currentViewElements = new HashSet<>();
        String baseName = viewModel.getCurrentSystem().getName();
        this.currentView = new Tab(
            baseName + (viewModel.isAutomatonEditor() ? " (" + ResourceHandler.getString("View", "automaton") + ")" :
                " (" + ResourceHandler.getString("View", "system") + ")"), currentViewPane);

        this.worldSizeUpdateListener = (observable, oldValue, newValue) -> {
            updateWorldSize();
        };

        // Construct view elements pane container
        viewElementsVBoxContainer.setAlignment(Pos.CENTER);
        viewElementsGroup.getChildren().add(placeholder);

        viewElementsScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();
            placeholder.setMinSize(Math.max(width, MIN_WORLD_SIZE) * 1 / viewModel.getZoomScale(),
                Math.max(height, MIN_WORLD_SIZE) * 1 / viewModel.getZoomScale());
            viewElementsScrollPane.layout();
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
        viewModel.getContainedPositionableViewModelElementsProperty().addListener(this::onUpdateViewElements);


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

        initializeViewElements();
        acceptTool(viewModel.getCurrentTool());
        focus();
    }

    public void setShortcutHandler(ShortcutHandler shortcutHandler) {
        this.shortcutHandler = shortcutHandler;
        currentViewPane.addEventHandler(KeyEvent.ANY, shortcutHandler);
        toolBar.addEventHandler(KeyEvent.ANY, shortcutHandler);
        if (currentInspector.get() != null) {
            currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
        }
    }

    public void focus() {
        currentViewPane.requestFocus();
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
        toolBar.addEventHandler(KeyEvent.ANY, shortcutHandler);
        return toolBar;
    }

    public Node drawInspector() {
        currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
        return currentInspector.get();
    }

    private void onUpdateViewElements(SetChangeListener.Change<? extends PositionableViewModelElement<?>> change) {
        if (change.wasAdded()) {
            addElement(change.getElementAdded());
        } else if (change.wasRemoved()) {
            // Find corresponding view element and remove it
            ViewElement<?> viewElement = findViewElement(change.getElementRemoved());
            if (viewElement != null) {
                currentViewElements.remove(viewElement);
                updateWorldSize();
            }
        }
        postUpdate();
    }

    private void postUpdate() {
        orderChildren();
        viewElementsScrollPane.layout();
        calculateViewPortPosition();
        if (viewModel.getCurrentToolType() != null) {
            for (ViewElement<?> viewElement : currentViewElements) {
                viewElement.accept(getViewModel().getCurrentTool());
            }
        }
        viewElementsScrollPane.requestLayout();
    }

    private void initializeViewElements() {
        viewModel.getContainedPositionableViewModelElementsProperty().forEach(this::addElement);
        postUpdate();
    }

    private void addElement(PositionableViewModelElement<?> element) {
        PositionableViewModelElementVisitor visitor = new ViewElementCreatorVisitor(viewFactory);
        ViewElement<?> viewElement = (ViewElement<?>) element.accept(visitor);

        // Add view element to current view elements
        currentViewElements.add(viewElement);
        if (viewModel.getCurrentToolType() != null) {
            viewElement.accept(getViewModel().getCurrentTool());
        }

        // TODO: not center
        viewElement.getTarget().getPositionProperty().addListener(worldSizeUpdateListener);
    }

    private ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return currentViewElements.stream()
            .filter(viewElement -> viewElement.getTarget().equals(element))
            .findFirst()
            .orElse(null);
    }

    private void onToolChanged(ObservableValue<? extends Tool> observable, Tool oldValue, Tool newValue) {
        acceptTool(newValue);
    }

    private void acceptTool(Tool tool) {
        tool.visitView(viewElementsVBoxContainer, viewElementsScrollPane, viewElementsGroup,
            viewElementsGroupContainer);
        currentViewElements.forEach(viewElement -> viewElement.accept(tool));
    }

    private void focusedElementChanged(
        ObservableValue<? extends PositionableViewModelElement<?>> observable, PositionableViewModelElement<?> oldValue,
        PositionableViewModelElement<?> newValue) {
        currentInspector.set((newValue != null) ? inspectorFactory.createInspector(newValue) : emptyInspector);
        if (shortcutHandler != null) {
            currentInspector.get().addEventHandler(KeyEvent.ANY, shortcutHandler);
        }
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

    public void updateWorldSize() {
        if (currentViewElements.stream().anyMatch(viewElement -> viewElement.getTarget().isCurrentlyModified())) {
            return;
        }

        boolean elementInBorder = currentViewElements.stream().anyMatch(viewElement -> {
            Point2D position = viewElement.getTarget().getPosition();
            Point2D size = viewElement.getTarget().getSize();
            return isElementInGroupBorder(position, size);
        });
        if (elementInBorder) {
            changeWorldSize(WORLD_SIZE_DELTA);
            updateWorldSize();
            return;
        }

        boolean wouldElementBeInBorder = currentViewElements.stream().anyMatch(viewElement -> {
            Point2D position = viewElement.getTarget().getPosition();
            Point2D size = viewElement.getTarget().getSize();
            return wouldElementBeInBorder(position, size);
        });
        if (!wouldElementBeInBorder && worldWouldHaveMinSize()) {
            changeWorldSize(-WORLD_SIZE_DELTA);
            updateWorldSize();
        }
    }

    private boolean worldWouldHaveMinSize() {
        Bounds viewportBounds = viewElementsScrollPane.getViewportBounds();
        return viewElementsGroup.getLayoutBounds().getWidth() - WORLD_SIZE_DELTA >= Math.max(MIN_WORLD_SIZE,
            viewportBounds.getWidth())
            && viewElementsGroup.getLayoutBounds().getHeight() - WORLD_SIZE_DELTA >= Math.max(MIN_WORLD_SIZE,
            viewportBounds.getHeight());
    }

    private void changeWorldSize(double delta) {
        if (delta >= 0) {
            placeholder.setPrefSize(viewElementsGroup.getLayoutBounds().getWidth() + delta,
                viewElementsGroup.getLayoutBounds().getHeight() + delta);
            viewElementsScrollPane.layout();
            setViewPortPosition(new Point2D(viewPortPositionViewProperty.getValue().getX() + delta / 2,
                viewPortPositionViewProperty.getValue().getY() + delta / 2));
        } else {
            setViewPortPosition(new Point2D(viewPortPositionViewProperty.getValue().getX() + delta / 2,
                viewPortPositionViewProperty.getValue().getY() + delta / 2));
            placeholder.setPrefSize(viewElementsGroup.getLayoutBounds().getWidth() + delta,
                viewElementsGroup.getLayoutBounds().getHeight() + delta);
            viewElementsScrollPane.layout();
        }
        moveAllElements(new Point2D(delta, delta).multiply(0.5));
    }

    private void moveAllElements(Point2D delta) {
        currentViewElements.forEach(
            viewElement -> viewElement.getTarget().getPositionProperty().removeListener(worldSizeUpdateListener));
        currentViewElements.forEach(viewElement -> {
            Point2D position = viewElement.getTarget().getPosition();
            viewElement.getTarget()
                .setPosition(new Point2D(position.getX() + delta.getX(), position.getY() + delta.getY()));
        });
        currentViewElements.forEach(
            viewElement -> viewElement.getTarget().getPositionProperty().addListener(worldSizeUpdateListener));
    }

    private boolean wouldElementBeInBorder(Point2D position, Point2D size) {
        Bounds bound = viewElementsGroup.getLayoutBounds();
        Point2D minPoint = new Point2D(bound.getMinX() + WORLD_SIZE_DELTA / 2, bound.getMinY() + WORLD_SIZE_DELTA / 2);
        Point2D maxPoint = new Point2D(bound.getMaxX() - WORLD_SIZE_DELTA / 2, bound.getMaxY() - WORLD_SIZE_DELTA / 2);
        double widthBorder = viewElementsScrollPane.getViewportBounds().getWidth() * RELATIVE_BORDER_SIZE;
        double heightBorder = viewElementsScrollPane.getViewportBounds().getHeight() * RELATIVE_BORDER_SIZE;
        return isElementInBorder(position, size, minPoint, maxPoint, new Point2D(widthBorder, heightBorder));
    }

    private boolean isElementInGroupBorder(Point2D position, Point2D size) {
        Bounds bound = viewElementsGroup.getLayoutBounds();
        Point2D minPoint = new Point2D(bound.getMinX(), bound.getMinY());
        Point2D maxPoint = new Point2D(bound.getMaxX(), bound.getMaxY());
        double widthBorder = viewElementsScrollPane.getViewportBounds().getWidth() * RELATIVE_BORDER_SIZE;
        double heightBorder = viewElementsScrollPane.getViewportBounds().getHeight() * RELATIVE_BORDER_SIZE;
        return isElementInBorder(position, size, minPoint, maxPoint, new Point2D(widthBorder, heightBorder));
    }

    private boolean isElementInBorder(
        Point2D position, Point2D size, Point2D minPoint, Point2D maxPoint, Point2D borderSize) {
        return position.getX() <= minPoint.getX() + borderSize.getX()
            || position.getX() + size.getX() >= maxPoint.getX() - borderSize.getX()
            || position.getY() <= minPoint.getY() + borderSize.getY()
            || position.getY() + size.getY() >= maxPoint.getY() - borderSize.getY();
    }
}
