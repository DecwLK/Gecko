/*
 * Copyright (c) 2024.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gecko.view.views;

import java.util.HashSet;
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
    private final Pane world;

    private final Pane origin;

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

        widthPadding.bind(
            Bindings.createDoubleBinding(() -> pane.getViewportBounds().getWidth(), pane.viewportBoundsProperty()));
        heightPadding.bind(
            Bindings.createDoubleBinding(() -> pane.getViewportBounds().getHeight(), pane.viewportBoundsProperty()));

        origin = new Pane();
        origin.setPrefSize(10, 10);
        origin.setStyle("-fx-background-color: #ff000088;");
        origin.layoutXProperty()
            .bind(Bindings.createDoubleBinding(() -> worldTolocalCoordinates(new Point2D(0, 0)).getX(), offset,
                evm.getZoomScaleProperty()));
        origin.layoutYProperty()
            .bind(Bindings.createDoubleBinding(() -> worldTolocalCoordinates(new Point2D(0, 0)).getY(), offset,
                evm.getZoomScaleProperty()));
        world.getChildren().add(origin);

        pane.setContent(world);
    }

    public ScrollPane draw() {
        return pane;
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
        updateWorldSize(oldPivot);
    }

    public void removeElement(ViewElement<?> element) {
        world.getChildren().remove(element.drawElement());
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

    private void focusLocalCoordinates(Point2D localCoords) {
        double x = (localCoords.getX() - pane.getWidth() / 2) / (world.getWidth() - pane.getWidth());
        double y = (localCoords.getY() - pane.getHeight() / 2) / (world.getHeight() - pane.getHeight());
        pane.setHvalue(x);
        pane.setVvalue(y);
    }

    private void updateWorldSize(Point2D oldPivot) {
        updateWorldSize();
        evm.updatePivot(oldPivot);
        focusWorldCoordinates(evm.getPivot());
    }

    private void updateWorldSize() {
        updateMinAndMaxWorldPosition();
        double newMinX = Math.min(minWorldPosition.getX(), 0) - widthPadding.get();
        double newMinY = Math.min(minWorldPosition.getY(), 0) - heightPadding.get();
        double newMaxX = Math.max(maxWorldPosition.getX(), 0) + widthPadding.get();
        double newMaxY = Math.max(maxWorldPosition.getY(), 0) + heightPadding.get();
        double newWidth = newMaxX - newMinX;
        double newHeight = newMaxY - newMinY;
        world.setMinSize(newWidth, newHeight);
        pane.layout();
        updateOffset();
    }

    private Point2D worldTolocalCoordinates(Point2D worldCoords) {
        return worldCoords.multiply(evm.getZoomScale()).add(offset.getValue());
    }

    private Point2D localToWorldCoordinates(Point2D screenCoords) {
        return screenCoords.subtract(offset.getValue()).multiply(1 / evm.getZoomScale());
    }

    private Point2D screenToLocalCoordinates(Point2D screenCoords) {
        return screenCoords.add(localViewPortPosition());
    }

    private Point2D localToScreenCoordinates(Point2D localCoords) {
        return localCoords.subtract(localViewPortPosition());
    }

    @SuppressWarnings("unused")
    public Point2D worldToScreenCoordinates(Point2D worldCoords) {
        return localToScreenCoordinates(worldTolocalCoordinates(worldCoords));
    }

    public Point2D screenToWorldCoordinates(Point2D screenCoords) {
        return localToWorldCoordinates(screenToLocalCoordinates(screenCoords));
    }

    private Point2D localViewPortPosition() {
        double h = Double.isNaN(pane.getHvalue()) ? 0 : pane.getHvalue();
        double v = Double.isNaN(pane.getVvalue()) ? 0 : pane.getVvalue();
        double x = h * (world.getWidth() - pane.getWidth());
        double y = v * (world.getHeight() - pane.getHeight());
        return new Point2D(x, y);
    }

    private void updateOffset() {
        double x = Math.max(0, -minWorldPosition.getX()) + widthPadding.get();
        double y = Math.max(0, -minWorldPosition.getY()) + heightPadding.get();
        offset.setValue(new Point2D(x, y));
    }

    public Point2D screenCenterWorldCoords() {
        double x = pane.getWidth() / 2.0;
        double y = pane.getHeight() / 2.0;
        return screenToWorldCoordinates(new Point2D(x, y));
    }

    private void setupListeners() {
        evm.getNeedsRefocusProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                focusWorldCoordinates(evm.getPivot());
                evm.getNeedsRefocusProperty().set(false);
            }
        });
        evm.getZoomScaleProperty().addListener((obs, oldV, newV) -> {
            updateWorldSize();
        });
        pane.hvalueProperty().addListener((obs, oldH, newH) -> {
            evm.updatePivot(screenCenterWorldCoords());
        });
        pane.vvalueProperty().addListener((obs, oldV, newV) -> {
            evm.updatePivot(screenCenterWorldCoords());
        });
        widthPadding.addListener((obs, oldV, newV) -> {
            updateWorldSize();
        });
        heightPadding.addListener((obs, oldV, newV) -> {
            updateWorldSize();
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
}
