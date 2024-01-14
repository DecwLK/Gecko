package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

@Setter
@Getter
public class PortViewModel extends BlockViewModelElement<Variable> {
    private Property<Visibility> visibilityProperty;

    public PortViewModel(Variable target) {
        super(target);
        this.visibilityProperty = new SimpleObjectProperty<>(target.getVisibility());
        setName(target.getName());
    }

    @Override
    public void updateTarget() {
        // Update name:
        if (super.getName() == null || this.getName().isEmpty()) {
            // TODO: Throw exception.
            return;
        }

        if (!super.getName().equals(super.target.getName())) {
            super.target.setName(super.getName());
        }

        // Update visibility:
        if (this.visibilityProperty == null || this.visibilityProperty.getValue() == null) {
            // TODO: Throw exception.
            return;
        }

        if (!this.visibilityProperty.getValue().equals(super.target.getVisibility())) {
            super.target.setVisibility(this.visibilityProperty.getValue());
        }
    }

    @Override
    public Object accept(PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
