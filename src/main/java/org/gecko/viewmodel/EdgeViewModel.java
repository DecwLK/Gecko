package org.gecko.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Edge;
import org.gecko.model.Kind;

@Setter @Getter
public class EdgeViewModel extends PositionableViewModelElement<Edge> {
    private Property<Kind> kind;

    private IntegerProperty priority;

    private ContractViewModel contract;

    private StateViewModel source;

    private StateViewModel destination;

    public EdgeViewModel(Edge target) {
        super(target);
        this.kind = new SimpleObjectProperty<>(target.getKind());
        this.priority = new SimpleIntegerProperty(target.getPriority());
        this.contract = new ContractViewModel(target.getContract());
        // TODO: this.source = new StateViewModel(target.getSource());
        // TODO: this.destination = new StateViewModel(target.getDestination());
    }

    @Override
    public void updateTarget() {
        // TODO
    }

    @Override
    public void accept(PositionableViewModelElementVisitor visitor) {
        visitor.visit(this);
    }
}
