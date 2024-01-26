package org.gecko.view.views.viewelement;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

@Getter
public class SystemViewElement extends BlockViewElement implements ViewElement<SystemViewModel> {

    private static final int Z_PRIORITY = 30;

    @Getter(AccessLevel.NONE)
    private final SystemViewModel systemViewModel;
    private final StringProperty nameProperty;
    private final StringProperty codeProperty;
    private final ListProperty<PortViewModel> portsProperty;

    public SystemViewElement(SystemViewModel systemViewModel) {
        super(systemViewModel);
        this.nameProperty = new SimpleStringProperty();
        this.codeProperty = new SimpleStringProperty();
        this.portsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.systemViewModel = systemViewModel;
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
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getZPriority() {
        return Z_PRIORITY;
    }

    private void constructVisualization() {
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setFill(Color.GRAY);
        GridPane gridPane = new GridPane();
        Label name = new Label("System: " + systemViewModel.getName());
        Bindings.createStringBinding(() -> "System: " + systemViewModel.getName(), systemViewModel.getNameProperty());
        Label ports = new Label("Ports: " + systemViewModel.getPortsProperty().size());
        ports.textProperty()
            .bind(Bindings.createStringBinding(() -> "Ports: " + systemViewModel.getPortsProperty().size(),
                systemViewModel.getPortsProperty()));
        gridPane.add(name, 0, 0);
        gridPane.add(ports, 0, 1);
        getChildren().addAll(background, gridPane);
    }
}
