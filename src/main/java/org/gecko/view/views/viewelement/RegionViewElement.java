package org.gecko.view.views.viewelement;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class RegionViewElement extends Pane implements ViewElement<RegionViewModel> {

    private final RegionViewModel regionViewModel;
    private final StringProperty nameProperty;
    private final Property<Color> colorProperty;
    private final StringProperty invariantProperty;
    private final List<StateViewModel> states;
    //TODO add more Properties once they get pushed

    public RegionViewElement(RegionViewModel regionViewModel) {
        this.colorProperty = new SimpleObjectProperty<>();
        this.invariantProperty = new SimpleStringProperty();
        this.nameProperty = new SimpleStringProperty();
        this.states = new ArrayList<>();
        this.regionViewModel = regionViewModel;
        bindViewModel();
        constructViewElement();
        //TODO add more Properties once they get pushed
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public List<Point2D> getEdgePoints() {
        return null;
    }

    @Override
    public void setEdgePoint(int index, Point2D point) {

    }

    @Override
    public RegionViewModel getTarget() {
        return regionViewModel;
    }

    @Override
    public Point2D getPosition() {
        return regionViewModel.getPosition();
    }

    private void bindViewModel() {
        nameProperty.bind(regionViewModel.getNameProperty());
        colorProperty.bind(regionViewModel.getColorProperty());
        invariantProperty.bind(regionViewModel.getInvariantProperty());
        ListChangeListener<StateViewModel> listener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    states.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    states.removeAll(change.getRemoved());
                }
            }
        };
        regionViewModel.getStatesProperty().addListener(listener);
        layoutXProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getPosition().getX(), regionViewModel.getPositionProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getPosition().getY(), regionViewModel.getPositionProperty()));
        //TODO is size width or coords?
        prefWidthProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getSize().getX(), regionViewModel.getSizeProperty()));
        prefHeightProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getSize().getY(), regionViewModel.getSizeProperty()));
        //TODO add more binds once they get pushed
    }

    private void constructViewElement() {
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setFill(colorProperty.getValue());
        GridPane gridPane = new GridPane();
        Label name = new Label("Region: " + regionViewModel.getName());
        Bindings.createStringBinding(() -> "Region: " + regionViewModel.getName(), regionViewModel.getNameProperty());
        Label preCondition = new Label("PreCondition: " + regionViewModel.getContract().getPrecondition());
        Bindings.createStringBinding(() -> "PreCondition: " + regionViewModel.getContract().getPrecondition(),
            regionViewModel.getContract().getPreConditionProperty());
        Label postCondition = new Label("PostCondition: " + regionViewModel.getContract().getPostcondition());
        Bindings.createStringBinding(() -> "PostCondition: " + regionViewModel.getContract().getPostcondition(),
            regionViewModel.getContract().getPostConditionProperty());
        Label invariant = new Label("Invariant: " + regionViewModel.getInvariant());
        Bindings.createStringBinding(() -> "Invariant: " + regionViewModel.getInvariant(), regionViewModel.getInvariantProperty());
        gridPane.add(name, 0, 0);
        gridPane.add(preCondition, 0, 1);
        gridPane.add(postCondition, 0, 2);
        gridPane.add(invariant, 0, 3);
        getChildren().addAll(background, gridPane);
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
