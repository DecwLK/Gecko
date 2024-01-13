package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

@Setter @Getter
public class PortViewModel extends BlockViewModelElement<Variable> {
    private Property<Visibility> visibility;

    public PortViewModel(Variable target) {
        super(target);
        super.setName(target.getName());
        this.visibility = new SimpleObjectProperty<>(target.getVisibility());
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
