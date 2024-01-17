package org.gecko.viewmodel;

import java.util.Random;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Region;

@Setter
@Getter
public class RegionViewModel extends BlockViewModelElement<Region> {
    private final Property<Color> colorProperty;
    private final StringProperty invariantProperty;
    private final ObservableList<StateViewModel> statesProperty; //TODO should this be called property?
    private final ContractViewModel contract;

    public RegionViewModel(int id, @NonNull Region target, @NonNull ContractViewModel contract) {
        super(id, target);
        this.contract = contract;
        this.invariantProperty = new SimpleStringProperty(target.getInvariant().getCondition());
        this.statesProperty = FXCollections.observableArrayList();

        // TODO Alternatives: Fixed default color or random color from given palette.
        Random random = new Random(java.lang.System.currentTimeMillis());
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        this.colorProperty = new SimpleObjectProperty<>(Color.rgb(red, green, blue));
    }

    @Override
    public void updateTarget() {
        super.updateTarget();
        target.getInvariant().setCondition(invariantProperty.getValue());
        target.getPreAndPostCondition().getPreCondition().setCondition(contract.getPrecondition());
        target.getPreAndPostCondition().getPostCondition().setCondition(contract.getPostcondition());
        target.getStates().clear();
        target.addStates(statesProperty.stream().map(StateViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addState(@NonNull StateViewModel state) {
        // TODO: prior checks
        statesProperty.add(state);
    }

    public void removeState(@NonNull StateViewModel state) {
        statesProperty.remove(state);
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }

    public void setInvariant(@NonNull String invariant) {
        invariantProperty.setValue(invariant);
    }

    public String getInvariant() {
        return invariantProperty.getValue();
    }

    public void setColor(@NonNull Color color) {
        colorProperty.setValue(color);
    }

    public Color getColor() {
        return colorProperty.getValue();
    }
}
