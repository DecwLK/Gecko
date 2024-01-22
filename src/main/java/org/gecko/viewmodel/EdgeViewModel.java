package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

/**
 * Represents an abstraction of an {@link Edge} model element. An {@link EdgeViewModel} is described by a source- and a
 * destination-{@link StateViewModel}. It is also associated with one of the start-{@link StateViewModel}'s {@link ContractViewModel}s, has a priority
 * and a {@link Kind}, which informs about how the associated {@link ContractViewModel} is handled. Contains methods for managing the afferent data
 * and updating the target-{@link Edge}.
 */
@Getter
@Setter
public class EdgeViewModel extends PositionableViewModelElement<Edge> {

    private final Property<Kind> kindProperty;
    private final IntegerProperty priorityProperty;
    private final Property<ContractViewModel> contractProperty;
    private final Property<StateViewModel> sourceProperty;
    private final Property<StateViewModel> destinationProperty;

    public EdgeViewModel(int id, @NonNull Edge target, @NonNull StateViewModel source, @NonNull StateViewModel destination) {
        super(id, target);
        this.kindProperty = new SimpleObjectProperty<>(target.getKind());
        this.priorityProperty = new SimpleIntegerProperty(target.getPriority());
        this.contractProperty = new SimpleObjectProperty<>();
        this.sourceProperty = new SimpleObjectProperty<>(source);
        this.destinationProperty = new SimpleObjectProperty<>(destination);
    }

    public void setPriority(int priority) {
        priorityProperty.setValue(priority);
    }

    public int getPriority() {
        return priorityProperty.getValue();
    }

    public void setKind(@NonNull Kind kind) {
        kindProperty.setValue(kind);
    }

    public Kind getKind() {
        return kindProperty.getValue();
    }

    public void setContract(@NonNull ContractViewModel contract) {
        contractProperty.setValue(contract);
    }

    public ContractViewModel getContract() {
        return contractProperty.getValue();
    }

    public void setSource(@NonNull StateViewModel source) {
        sourceProperty.setValue(source);
    }

    public StateViewModel getSource() {
        return sourceProperty.getValue();
    }

    public void setDestination(@NonNull StateViewModel destination) {
        destinationProperty.setValue(destination);
    }

    public StateViewModel getDestination() {
        return destinationProperty.getValue();
    }

    @Override
    public void updateTarget() {
        target.setKind(getKind());
        target.setPriority(getPriority());
        target.setContract(contractProperty.getValue().getTarget());
        target.setSource(sourceProperty.getValue().getTarget());
        target.setDestination(destinationProperty.getValue().getTarget());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
