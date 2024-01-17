package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class SystemViewElement extends Pane implements ViewElement<SystemViewModel> {

    private SystemViewModel systemViewModel;
    private final StringProperty nameProperty;
    private final StringProperty codeProperty;
    private final List<PortViewModel> ports;

    public SystemViewElement() {
        this.nameProperty = new SimpleStringProperty();
        this.codeProperty = new SimpleStringProperty();
        this.ports = new ArrayList<>();
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
    public void bindTo(SystemViewModel target) {
        systemViewModel = target;
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
}
