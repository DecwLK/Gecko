package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javafx.scene.paint.Color;
import org.gecko.model.Region;
import javafx.collections.FXCollections;

import java.util.Random;

@Getter @Setter
public class RegionViewModel extends BlockViewModelElement<Region> {
    private Property<Color> color;
    private ContractViewModel contract;
    private StringProperty invariant;
    private ObservableList<StateViewModel> states;

    public RegionViewModel(Region target) {
        super(target);
        super.setName(target.getName());
        // TODO: this.contract = new ContractViewModel(target.getContract());
        this.invariant = new SimpleStringProperty(target.getInvariant().getCondition());
        this.states = FXCollections.observableArrayList();

        // TODO Alternatives: Fixed default color or random color from given palette.
        Random random = new Random(System.currentTimeMillis());
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        this.color = new SimpleObjectProperty<>(Color.rgb(red, green, blue));
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    public void addState(StateViewModel state) {
        // TODO: prior checks
        this.states.add(state);
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
