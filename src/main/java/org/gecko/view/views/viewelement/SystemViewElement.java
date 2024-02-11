package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElement extends BlockViewElement implements ViewElement<SystemViewModel> {

    private static final int Z_PRIORITY = 30;

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
        constructVisualization();
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {
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
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getPosition().getX(),
            systemViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getPosition().getY(),
            systemViewModel.getPositionProperty()));
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
        updatePortViewModels();
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
        portViewElements.add(portViewElement);
        if (portViewModel.getVisibility() == Visibility.INPUT) {
            inputPortsAligner.getChildren().add(portViewElement);
        } else if (portViewModel.getVisibility() == Visibility.OUTPUT) {
            outputPortsAligner.getChildren().add(portViewElement);
        }
    }

    private void removePort(PortViewModel portViewModel) {
        // This is safe, since the portViewElement should be present in the list
        PortViewElement portViewElement =
            portViewElements.stream().filter(pve -> pve.getViewModel().equals(portViewModel)).findFirst().orElseThrow();
        portViewElements.remove(portViewElement);
        if (portViewModel.getVisibility() == Visibility.INPUT) {
            inputPortsAligner.getChildren().remove(portViewElement);
        } else {
            outputPortsAligner.getChildren().remove(portViewElement);
        }
    }

    private void onVisibilityChanged(
        ObservableValue<? extends Visibility> observable, Visibility oldValue, Visibility newValue) {
        if (oldValue == newValue) {
            return;
        }
        //TODO this is ugly
        PortViewModel portViewModel =
            portsProperty.stream().filter(pvm -> pvm.getVisibilityProperty() == observable).findFirst().orElseThrow();
        PortViewElement portViewElement =
            portViewElements.stream().filter(pve -> pve.getViewModel().equals(portViewModel)).findFirst().orElseThrow();
        if (newValue == Visibility.INPUT) {
            outputPortsAligner.getChildren().remove(portViewElement);
            inputPortsAligner.getChildren().add(portViewElement);
        } else {
            inputPortsAligner.getChildren().remove(portViewElement);
            outputPortsAligner.getChildren().add(portViewElement);
        }
    }

    private List<HBox> setupPortContainers() {
        List<HBox> result = new ArrayList<>();
        for (VBox aligner : List.of(inputPortsAligner, outputPortsAligner)) {
            //HBox container to ensure horizontal alignment
            HBox container = new HBox();
            //Center names vertically and space them out
            VBox.setVgrow(aligner, Priority.ALWAYS);
            aligner.setAlignment(Pos.CENTER);
            aligner.setSpacing(10);
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
}
