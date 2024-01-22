package org.gecko.viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.Variable;
import org.gecko.model.Visibility;

/**
 * Represents an abstraction of a {@link Variable} model element. A {@link PortViewModel} is described by a type and a {@link Visibility}. Contains
 * methods for managing the afferent data and updating the target-{@link Variable}.
 */
@Setter
@Getter
public class PortViewModel extends BlockViewModelElement<Variable> {
    private final Property<Visibility> visibilityProperty;
    private final Property<String> typeProperty;

    public PortViewModel(int id, @NonNull Variable target) {
        super(id, target);
        this.visibilityProperty = new SimpleObjectProperty<>(target.getVisibility());
        this.typeProperty = new SimpleStringProperty(target.getType());
    }

    public Visibility getVisibility() {
        return visibilityProperty.getValue();
    }

    public void setVisibility(@NonNull Visibility visibility) {
        visibilityProperty.setValue(visibility);
    }

    public String getType() {
        return typeProperty.getValue();
    }

    public void setType(@NonNull String type) {
        typeProperty.setValue(type);
    }

    @Override
    public void updateTarget() {
        super.updateTarget();
        target.setVisibility(getVisibility());
        target.setType(getType());
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
