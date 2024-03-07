package org.gecko.view.views;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import lombok.Getter;
import org.gecko.view.views.viewelement.ViewElement;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class ViewElementPane {

    private final Property<Point2D> offset;
    private final EditorViewModel evm;
    private final DoubleProperty widthPadding;
    private final DoubleProperty heightPadding;
    private Point2D minWorldPosition;
    private Point2D maxWorldPosition;

    private final ScrollPane pane;
    @Getter
    private final Pane world;

    @Getter
    private final Set<ViewElement<?>> elements;

    public ViewElementPane(EditorViewModel evm) {
        this.world = new Pane();
        this.pane = new ScrollPane();
        this.offset = new SimpleObjectProperty<>(new Point2D(0, 0));
        this.widthPadding = new SimpleDoubleProperty(0);
        this.heightPadding = new SimpleDoubleProperty(0);
        this.minWorldPosition = new Point2D(0, 0);
        this.maxWorldPosition = new Point2D(0, 0);
        this.elements = new HashSet<>();
        this.evm = evm;

        setupListeners();

        widthPadding.bind(pane.widthProperty());
        heightPadding.bind(pane.heightProperty());

        pane.setContent(world);
    }

    public ScrollPane draw() {
        return pane;
    }

    public void onSelectionChanged() {
        orderChildren();
    }

    public void addElement(ViewElement<?> element) {
        // Save the pivot to refocus after adding the element because changing the world size will change the pivot
        Point2D oldPivot = evm.getPivot();
        PositionableViewModelElement<?> target = element.getTarget();
        world.getChildren().add(element.drawElement());
        elements.add(element);

        Node node = element.drawElement();
        node.setLayoutX(worldTolocalCoordinates(target.getPosition()).getX());
        node.setLayoutY(worldTolocalCoordinates(target.getPosition()).getY());
        node.layoutXProperty()
            .bind(Bindings.createDoubleBinding(() -> worldTolocalCoordinates(target.getPosition()).getX(),
                target.getPositionProperty(), offset));
        node.layoutYProperty()
            .bind(Bindings.createDoubleBinding(() -> worldTolocalCoordinates(target.getPosition()).getY(),
                target.getPositionProperty(), offset));

        node.getTransforms().setAll(new Scale(evm.getZoomScale(), evm.getZoomScale(), 0, 0));
        evm.getZoomScaleProperty().addListener((obs, oldV, newV) -> {
            //Can't bind to zoomScale because the pivot of (0, 0) is important to keep world coordinates consistent with
            //visual position
            node.getTransforms().setAll(new Scale(newV.doubleValue(), newV.doubleValue(), 0, 0));
        });
        target.getPositionProperty().addListener((obs, oldV, newV) -> {
            if (target.isCurrentlyModified()) {
                return;
            }
            updateWorldSize(evm.getPivot());
        });
        orderChildren();
        updateWorldSize(oldPivot);
    }

    public void removeElement(ViewElement<?> element) {
        elements.remove(element);
        orderChildren();
    }

    public ViewElement<?> findViewElement(PositionableViewModelElement<?> element) {
        return elements.stream().filter(e -> e.getTarget().equals(element)).findFirst().orElse(null);
    }

    public void focusWorldCoordinates(Point2D worldCoords) {
        if (Double.isNaN(worldCoords.getX()) || Double.isNaN(worldCoords.getY())) {
            return;
        }
        focusLocalCoordinates(worldTolocalCoordinates(worldCoords));
    }

    private Point2D localToWorldCoordinates(Point2D screenCoords) {
        return screenCoords.subtract(offset.getValue()).multiply(1 / evm.getZoomScale());
    }

    public Point2D screenToLocalCoordinates(Point2D screenCoords) {
        return pane.screenToLocal(screenCoords).add(localViewPortPosition());
    }

    public Point2D screenToLocalCoordinates(double x, double y) {
        return screenToLocalCoordinates(new Point2D(x, y));
    }

    private Point2D localToScreenCoordinates(Point2D localCoords) {
        return pane.localToScreen(localCoords.subtract(localViewPortPosition()));
    }

    @SuppressWarnings("unused")
    public Point2D worldToScreenCoordinates(Point2D worldCoords) {
        return localToScreenCoordinates(worldTolocalCoordinates(worldCoords));
    }

    @SuppressWarnings("unused")
    public Point2D worldToScreenCoordinates(double x, double y) {
        return worldToScreenCoordinates(new Point2D(x, y));
    }

    public Point2D screenToWorldCoordinates(Point2D screenCoords) {
        return localToWorldCoordinates(screenToLocalCoordinates(screenCoords));
    }

    public Point2D screenToWorldCoordinates(double x, double y) {
        return screenToWorldCoordinates(new Point2D(x, y));
    }

    private void focusLocalCoordinates(Point2D localCoords) {
        double h = (localCoords.getX() - pane.getViewportBounds().getWidth() / 2) / (world.getWidth()
            - pane.getViewportBounds().getWidth());
        double v = (localCoords.getY() - pane.getViewportBounds().getHeight() / 2) / (world.getHeight()
            - pane.getViewportBounds().getHeight());
        pane.setHvalue(h);
        pane.setVvalue(v);
    }

    private void updateWorldSize(Point2D oldPivot) {
        updateWorldSize();
        evm.setPivot(oldPivot);
    }

    private void updateWorldSize() {
        updateMinAndMaxWorldPosition();
        Point2D localMin = worldTolocalCoordinates(minWorldPosition);
        Point2D localMax = worldTolocalCoordinates(maxWorldPosition);
        double newWidth =
            Math.max(pane.getViewportBounds().getWidth(), localMax.getX() - localMin.getX() + widthPadding.get() * 2);
        double newHeight =
            Math.max(pane.getViewportBounds().getHeight(), localMax.getY() - localMin.getY() + heightPadding.get() * 2);
        world.setMinSize(newWidth, newHeight);
        pane.layout();
        updateOffset();
    }

    private Point2D worldTolocalCoordinates(Point2D worldCoords) {
        return worldCoords.multiply(evm.getZoomScale()).add(offset.getValue());
    }

    private Point2D localViewPortPosition() {
        double h = Double.isNaN(pane.getHvalue()) ? 0 : pane.getHvalue();
        double v = Double.isNaN(pane.getVvalue()) ? 0 : pane.getVvalue();
        double x = h * (world.getWidth() - pane.getViewportBounds().getWidth());
        double y = v * (world.getHeight() - pane.getViewportBounds().getHeight());
        return new Point2D(x, y);
    }

    private Point2D screenCenterWorldCoords() {
        //Can't use screenToLocal because we don't want the pane.localToScreen() offset
        Point2D screenCenter =
            new Point2D(pane.getViewportBounds().getWidth() / 2, pane.getViewportBounds().getHeight() / 2);
        Point2D localScreenCenter = screenCenter.add(localViewPortPosition());
        return localToWorldCoordinates(localScreenCenter);
    }

    private void updateOffset() {
        double x = Math.max(0, -minWorldPosition.getX()) + widthPadding.get();
        double y = Math.max(0, -minWorldPosition.getY()) + heightPadding.get();
        offset.setValue(new Point2D(x, y));
    }

    private void setupListeners() {
        evm.getNeedsRefocusProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                focusWorldCoordinates(evm.getPivot());
                evm.getNeedsRefocusProperty().set(false);
            }
        });
        evm.getZoomScaleProperty().addListener((obs, oldV, newV) -> {
            updateWorldSize(evm.getPivot());
        });
        pane.hvalueProperty().addListener((obs, oldH, newH) -> {
            evm.updatePivot(screenCenterWorldCoords());
        });
        pane.vvalueProperty().addListener((obs, oldV, newV) -> {
            evm.updatePivot(screenCenterWorldCoords());
        });
        widthPadding.addListener((obs, oldV, newV) -> {
            updateWorldSize(evm.getPivot());
        });
        heightPadding.addListener((obs, oldV, newV) -> {
            updateWorldSize(evm.getPivot());
        });
    }

    private void updateMinAndMaxWorldPosition() {
        if (elements.isEmpty() || elements.stream().allMatch(e -> e.getTarget().isCurrentlyModified())) {
            return;
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (ViewElement<?> element : elements) {
            PositionableViewModelElement<?> target = element.getTarget();
            if (target.getPosition().getX() < minX) {
                minX = target.getPosition().getX();
            }
            if (target.getPosition().getY() < minY) {
                minY = target.getPosition().getY();
            }
            if (target.getPosition().getX() + target.getSize().getX() > maxX) {
                maxX = target.getPosition().getX() + target.getSize().getX();
            }
            if (target.getPosition().getY() + target.getSize().getY() > maxY) {
                maxY = target.getPosition().getY() + target.getSize().getY();
            }
        }
        minWorldPosition = new Point2D(minX, minY);
        maxWorldPosition = new Point2D(maxX, maxY);
    }

    private void orderChildren() {
        List<Node> sortedElements = elements.stream()
            .sorted(Comparator.comparingInt(ViewElement::getZPriority))
            .map(ViewElement::drawElement)
            .toList();
        world.getChildren().setAll(sortedElements);
    }
}
