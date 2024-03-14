package org.gecko.viewmodel;

import java.util.Random;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.Region;

/**
 * Represents an abstraction of a {@link Region} model element. A {@link RegionViewModel} is described by a
 * {@link Color}, a set of {@link StateViewModel}s, a {@link ContractViewModel} and an invariant. Contains methods for
 * managing the afferent data and updating the target-{@link Region}.
 */
@Setter
@Getter
public class RegionViewModel extends BlockViewModelElement<Region> {
    private final Property<Color> colorProperty;
    private final StringProperty invariantProperty;
    private final ObservableList<StateViewModel> statesProperty;
    private final ContractViewModel contract;

    private static final int MAXIMUM_RGB_COLOR_VALUE = 255;

    public RegionViewModel(
        int id, @NonNull Region target, @NonNull ContractViewModel contract) {
        super(id, target);
        this.contract = contract;
        this.invariantProperty = new SimpleStringProperty(target.getInvariant().getCondition());
        this.statesProperty = FXCollections.observableArrayList();

        Random random = new Random(java.lang.System.currentTimeMillis());
        int red = random.nextInt(MAXIMUM_RGB_COLOR_VALUE);
        int green = random.nextInt(MAXIMUM_RGB_COLOR_VALUE);
        int blue = random.nextInt(MAXIMUM_RGB_COLOR_VALUE);
        this.colorProperty = new SimpleObjectProperty<>(Color.rgb(red, green, blue, 0.5));
    }

    @Override
    public void updateTarget() throws ModelException {
        super.updateTarget();
        target.getInvariant().setCondition(invariantProperty.getValue());
        target.getPreAndPostCondition().getPreCondition().setCondition(contract.getPrecondition());
        target.getPreAndPostCondition().getPostCondition().setCondition(contract.getPostcondition());
        target.getStates().clear();
        target.addStates(statesProperty.stream().map(StateViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addState(@NonNull StateViewModel state) {
        statesProperty.add(state);
    }

    public void removeState(@NonNull StateViewModel state) {
        statesProperty.remove(state);
    }

    public void clearStates() {
        statesProperty.clear();
    }

    @Override
    public <S> S accept(@NonNull PositionableViewModelElementVisitor<S> visitor) {
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

    /**
     * Checks if the given state is in the region and adds it to the region if it is.
     *
     * @param state the state to check
     */
    public void checkStateInRegion(StateViewModel state) {
        Bounds regionBound =
            new BoundingBox(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
        Bounds stateBound =
            new BoundingBox(state.getPosition().getX(), state.getPosition().getY(), state.getSize().getX(),
                state.getSize().getY());
        if (regionBound.intersects(stateBound)) {
            addState(state);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionViewModel region)) {
            return false;
        }
        return id == region.id;
    }
}
