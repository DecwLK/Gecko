package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

@Setter
@Getter
public class PortViewModel extends BlockViewModelElement<Variable> {
    private final Property<Visibility> visibilityProperty;
    private final Property<String> typeProperty;

    public PortViewModel(Variable target) {
        super(target);
        this.visibilityProperty = new SimpleObjectProperty<>(target.getVisibility());
        this.typeProperty = new SimpleStringProperty(target.getType());
        setName(target.getName());
    }

    public Visibility getVisibility() {
        return this.visibilityProperty.getValue();
    }

    public void setVisibility(Visibility visibility) {
        this.visibilityProperty.setValue(visibility);
    }

    public String getType() {
        return this.typeProperty.getValue();
    }

    public void setType(String type) {
        this.typeProperty.setValue(type);
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
