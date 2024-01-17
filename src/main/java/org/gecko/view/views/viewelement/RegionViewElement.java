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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;

@Getter
public class RegionViewElement extends Rectangle implements ViewElement<RegionViewModel> {

    private RegionViewModel regionViewModel;
    private final StringProperty nameProperty;
    private final Property<Color> colorProperty;
    private final StringProperty invariantProperty;
    private final List<StateViewModel> states;
    //TODO add more Properties once they get pushed

    public RegionViewElement() {
        this.colorProperty = new SimpleObjectProperty<>();
        this.invariantProperty = new SimpleStringProperty();
        this.nameProperty = new SimpleStringProperty();
        this.states = new ArrayList<>();
        //TODO add more Properties once they get pushed
    }

    @Override
    public Node drawElement() {
        return this;
    }

    @Override
    public RegionViewModel getTarget() {
        return regionViewModel;
    }

    @Override
    public Point2D getPosition() {
        return regionViewModel.getPosition();
    }

    @Override
    public void bindTo(RegionViewModel target) {
        regionViewModel = target;
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
        xProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getPosition().getX(), regionViewModel.getPositionProperty()));
        yProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getPosition().getY(), regionViewModel.getPositionProperty()));
        //TODO is size width or coords?
        widthProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getSize().getX(), regionViewModel.getSizeProperty()));
        heightProperty().bind(Bindings.createDoubleBinding(() -> regionViewModel.getSize().getY(), regionViewModel.getSizeProperty()));
        //TODO add more binds once they get pushed
    }

    @Override
    public void accept(ViewElementVisitor visitor) {
        visitor.visit(this);
    }
}
