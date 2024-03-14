package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;

/**
 * Represents a type of {@link BlockViewElement} implementing the {@link ViewElement} interface, which encapsulates an
 * {@link SystemViewModel}.
 */
public class SystemViewElement extends BlockViewElement implements ViewElement<SystemViewModel> {

    private static final int Z_PRIORITY = 30;
    private static final int PORT_SPACING = 10;

    private final SystemViewModel systemViewModel;
    @Getter
    private final StringProperty nameProperty;
    @Getter
    private final StringProperty codeProperty;
    @Getter
    private final ListProperty<PortViewModel> portsProperty;

    private final VBox inputPortsAligner;
    private final VBox outputPortsAligner;
    private final ListProperty<PortViewElement> portViewElements;
    private final ChangeListener<Point2D> positionListener = (observable, oldValue, newValue) -> reorderPorts();

    public SystemViewElement(SystemViewModel systemViewModel) {
        super(systemViewModel);
        this.nameProperty = new SimpleStringProperty();
        this.codeProperty = new SimpleStringProperty();
        this.portsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.portViewElements = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.systemViewModel = systemViewModel;
        this.inputPortsAligner = new VBox();
        this.outputPortsAligner = new VBox();

        bindViewModel();
        addPortPositionListeners();
        constructVisualization();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public SystemViewModel getTarget() {
        return systemViewModel;
    }

    @Override
    public Point2D getPosition() {
        return systemViewModel.getPosition();
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
        portViewElements.forEach(visitor::visit);
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void bindViewModel() {
        nameProperty.bind(systemViewModel.getNameProperty());
        codeProperty.bind(systemViewModel.getCodeProperty());
        prefWidthProperty().bind(
            Bindings.createDoubleBinding(() -> systemViewModel.getSize().getX(), systemViewModel.getSizeProperty()));
        prefHeightProperty().bind(
            Bindings.createDoubleBinding(() -> systemViewModel.getSize().getY(), systemViewModel.getSizeProperty()));
        portsProperty.bind(systemViewModel.getPortsProperty());

        systemViewModel.getPositionProperty().addListener((observable, oldValue, newValue) -> updatePortViewModels());
        updatePortViewModels();
    }

    private void constructVisualization() {
        HBox container = new HBox();
        container.setPrefSize(getPrefWidth(), getPrefHeight());
        List<HBox> portContainers = setupPortContainers();
        container.getChildren().addAll(portContainers.getFirst(), getCenteredNameLabel(), portContainers.getLast());
        getChildren().addAll(getBackgroundRectangle(), container);
        portsProperty.forEach(this::addPort);
        portsProperty.addListener(this::onPortsChanged);
    }

    private void onPortsChanged(ListChangeListener.Change<? extends PortViewModel> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addPort);
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removePort);
            }
        }
    }

    private void updatePortViewModels() {
        for (PortViewElement portViewElement : portViewElements) {
            portViewElement.getViewModel().setSystemPortSize(portViewElement.getViewSize());

            Point2D portViewElementPositionInScene = portViewElement.localToScene(portViewElement.getViewPosition());

            // translate the port position to the world coordinate system
            Point2D calculatedWorldPosition = sceneToLocal(portViewElementPositionInScene).add(getPosition())
                .subtract(portViewElement.getViewPosition());
            portViewElement.getViewModel().setSystemPortPosition(calculatedWorldPosition);
        }
    }

    private void addPort(PortViewModel portViewModel) {
        portViewModel.getVisibilityProperty().addListener(this::onVisibilityChanged);
        PortViewElement portViewElement = new PortViewElement(portViewModel);
        portViewElement.layoutYProperty().addListener((observable, oldValue, newValue) -> updatePortViewModels());
        portViewElements.add(portViewElement);
        if (portViewModel.getVisibility() == Visibility.INPUT) {
            inputPortsAligner.getChildren().add(portViewElement);
        } else if (portViewModel.getVisibility() == Visibility.OUTPUT) {
            outputPortsAligner.getChildren().add(portViewElement);
        }
        portViewModel.getIncomingConnections().addListener(this::onConnectionChanged);
        portViewModel.getOutgoingConnections().addListener(this::onConnectionChanged);
        reorderPorts();
    }

    private void removePort(PortViewModel portViewModel) {
        // This is safe, since the portViewElement should be present in the list
        PortViewElement portViewElement =
            portViewElements.stream().filter(pve -> pve.getViewModel().equals(portViewModel)).findFirst().orElseThrow();
        portViewElements.remove(portViewElement);
        if (portViewModel.getVisibility() == Visibility.INPUT) {
            inputPortsAligner.getChildren().remove(portViewElement);
        } else if (portViewModel.getVisibility() == Visibility.OUTPUT) {
            outputPortsAligner.getChildren().remove(portViewElement);
        } else {
            inputPortsAligner.getChildren().remove(portViewElement);
            outputPortsAligner.getChildren().remove(portViewElement);
        }
        portViewModel.getIncomingConnections().removeListener(this::onConnectionChanged);
        portViewModel.getOutgoingConnections().removeListener(this::onConnectionChanged);
        reorderPorts();
    }

    private void onVisibilityChanged(
        ObservableValue<? extends Visibility> observable, Visibility oldValue, Visibility newValue) {
        if (oldValue == newValue) {
            return;
        }
        PortViewModel portViewModel =
            portsProperty.stream().filter(pvm -> pvm.getVisibilityProperty() == observable).findFirst().orElseThrow();
        PortViewElement portViewElement =
            portViewElements.stream().filter(pve -> pve.getViewModel().equals(portViewModel)).findFirst().orElseThrow();
        if (newValue == Visibility.INPUT) {
            outputPortsAligner.getChildren().remove(portViewElement);
            inputPortsAligner.getChildren().add(portViewElement);
        } else if (newValue == Visibility.OUTPUT) {
            inputPortsAligner.getChildren().remove(portViewElement);
            outputPortsAligner.getChildren().add(portViewElement);
        } else {
            inputPortsAligner.getChildren().remove(portViewElement);
            outputPortsAligner.getChildren().remove(portViewElement);
        }
        reorderPorts();
    }

    private List<HBox> setupPortContainers() {
        List<HBox> result = new ArrayList<>();
        for (VBox aligner : List.of(inputPortsAligner, outputPortsAligner)) {
            //HBox container to ensure horizontal alignment
            HBox container = new HBox();
            //Center names vertically and space them out
            VBox.setVgrow(aligner, Priority.ALWAYS);
            aligner.setAlignment(Pos.CENTER);
            aligner.setSpacing(PORT_SPACING);
            container.getChildren().add(aligner);
            //Width isn't set yet, so we need to listen to it
            widthProperty().addListener((observable, oldValue, newValue) -> {
                // x/3 to evenly divide into left, center, right
                container.setPrefWidth(newValue.doubleValue() / 3);
            });
            result.add(container);
        }
        result.getLast().setAlignment(Pos.CENTER_RIGHT);
        return result;
    }

    private Rectangle getBackgroundRectangle() {
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setArcWidth(BACKGROUND_ROUNDING);
        background.setArcHeight(BACKGROUND_ROUNDING);
        background.setFill(Color.LIGHTGRAY);
        return background;
    }

    private Node getCenteredNameLabel() {
        Label nameLabel = new Label();
        nameLabel.textProperty().bind(nameProperty);
        //Center name vertically
        VBox nameContainer = new VBox();
        nameContainer.getChildren().add(nameLabel);
        nameContainer.setAlignment(Pos.CENTER);
        //Center name horizontally. Assumes that the port containers are of equal width and always present
        HBox spacer = new HBox();
        spacer.setAlignment(Pos.CENTER);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.getChildren().add(nameContainer);
        return spacer;
    }

    private void reorderPorts() {
        List<PortViewElement> inputs =
            new ArrayList<>(inputPortsAligner.getChildren().stream().map(PortViewElement.class::cast).toList());
        List<PortViewElement> outputs =
            new ArrayList<>(outputPortsAligner.getChildren().stream().map(PortViewElement.class::cast).toList());
        inputs.sort(this::compare);
        outputs.sort(this::compare);
        inputPortsAligner.getChildren().setAll(inputs);
        outputPortsAligner.getChildren().setAll(outputs);
    }

    private int compare(PortViewElement p1, PortViewElement p2) {
        int compare = Double.compare(getOtherPortY(p1.getViewModel()), getOtherPortY(p2.getViewModel()));
        if (compare == 0) {
            compare = Integer.compare(p1.getViewModel().getId(), p2.getViewModel().getId());
        }
        return compare;
    }

    private Point2D getSortPosition(PortViewModel portViewModel) {
        if (isVariableBlock(portViewModel)) {
            return portViewModel.getCenter();
        }
        return portViewModel.getSystemPositionProperty()
            .getValue()
            .add(portViewModel.getSystemPortOffsetProperty().getValue());
    }

    private double getOtherPortY(PortViewModel portViewModel) {
        List<SystemConnectionViewModel> connections =
            portViewModel.getVisibility() == Visibility.INPUT ? portViewModel.getIncomingConnections()
                : portViewModel.getOutgoingConnections();
        double minY = Double.MAX_VALUE;
        for (SystemConnectionViewModel connection : connections) {
            if (connection.getSource().equals(portViewModel)) {
                minY = Math.min(minY, getSortPosition(connection.getDestination()).getY());
            } else {
                minY = Math.min(minY, getSortPosition(connection.getSource()).getY());
            }
        }
        return minY;
    }

    private boolean isVariableBlock(PortViewModel portViewModel) {
        return systemViewModel.getTarget().getParent().getVariables().contains(portViewModel.getTarget());
    }

    private void onConnectionChanged(ListChangeListener.Change<? extends SystemConnectionViewModel> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList()
                    .forEach(connection -> connection.getEdgePoints().addListener(this::onEdgePointsChanged));
            } else if (change.wasRemoved()) {
                change.getRemoved()
                    .forEach(connection -> connection.getEdgePoints().removeListener(this::onEdgePointsChanged));
            }
        }
        reorderPorts();
    }

    private void onEdgePointsChanged(ListChangeListener.Change<? extends Property<Point2D>> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(point -> point.addListener(positionListener));
            } else if (change.wasRemoved()) {
                change.getRemoved().forEach(point -> point.removeListener(positionListener));
            }
        }
    }

    private void addPortPositionListeners() {
        for (PortViewModel portViewModel : portsProperty) {
            List<SystemConnectionViewModel> connections = new ArrayList<>(portViewModel.getIncomingConnections());
            connections.addAll(portViewModel.getOutgoingConnections());
            for (SystemConnectionViewModel connection : connections) {
                connection.getEdgePoints().addListener(this::onEdgePointsChanged);
                connection.getEdgePoints().forEach(point -> point.addListener(positionListener));
            }
        }
    }
}
