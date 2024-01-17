package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElement extends Pane implements ViewElement<SystemViewModel> {

    private final SystemViewModel systemViewModel;
    private final StringProperty nameProperty;
    private final StringProperty codeProperty;
    private final List<PortViewModel> ports;

    public SystemViewElement(SystemViewModel systemViewModel) {
        this.nameProperty = new SimpleStringProperty();
        this.codeProperty = new SimpleStringProperty();
        this.ports = new ArrayList<>();
        this.systemViewModel = systemViewModel;
        bindViewModel();
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

    private void bindViewModel() {
        nameProperty.bind(systemViewModel.getNameProperty());
        codeProperty.bind(systemViewModel.getCodeProperty());
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getPosition().getX(), systemViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getPosition().getY(), systemViewModel.getPositionProperty()));
        prefWidthProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getSize().getX(), systemViewModel.getSizeProperty()));
        prefHeightProperty().bind(Bindings.createDoubleBinding(() -> systemViewModel.getSize().getY(), systemViewModel.getSizeProperty()));
        //TODO add more binds once they get pushed
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
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
        Bindings.createStringBinding(() -> "Ports: " + systemViewModel.getPortsProperty().size(), systemViewModel.getPortsProperty());
        gridPane.add(name, 0, 0);
        gridPane.add(ports, 0, 1);
        getChildren().addAll(background, gridPane);
    }
}
