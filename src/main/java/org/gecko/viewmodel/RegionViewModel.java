package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Condition;
import org.gecko.model.Region;
import java.util.Random;

@Setter
@Getter
public class RegionViewModel extends BlockViewModelElement<Region> {
    private Property<Color> color;
    private ContractViewModel contract;
    private StringProperty invariant;
    private ObservableList<StateViewModel> states;

    public RegionViewModel(Region target) {
        super(target);
        super.setName(target.getName());
        this.contract = new ContractViewModel(target.getPreAndPostCondition());
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
        // Update name:
        if (super.getName() == null || super.getName().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!super.getName().equals(super.target.getName())) {
            super.target.setName(super.getName());
        }

        // Update contract:
        if (this.contract == null || this.contract.target == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.contract.target.equals(super.target.getPreAndPostCondition())) {
            super.target.setPreAndPostCondition(this.contract.target);
        }

        // Update invariant:
        if (this.invariant == null
                || this.invariant.getValue() == null
                || this.invariant.getValue().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!this.invariant.getValue().equals(super.target.getInvariant().getCondition())) {
            super.target.setInvariant(new Condition(this.invariant.getValue()));
        }

        // TODO: Update states.
    }

    public void addState(StateViewModel state) {
        // TODO: prior checks
        this.states.add(state);
        super.target.addState(state.target);
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
