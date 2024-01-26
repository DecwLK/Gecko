package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.System;

/**
 * Represents an abstraction of a {@link System} model element. A {@link SystemViewModel} is described by a code snippet
 * (?) and a set of {@link PortViewModel}s. Contains methods for managing the afferent data and updating the
 * target-{@link System}.
 */
@Getter
@Setter
public class SystemViewModel extends BlockViewModelElement<System> {
    private final StringProperty codeProperty;
    private final ListProperty<PortViewModel> portsProperty;

    public SystemViewModel(int id, @NonNull System target) {
        super(id, target);
        this.codeProperty = new SimpleStringProperty(target.getCode());
        this.portsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public List<PortViewModel> getPorts() {
        return new ArrayList<>(portsProperty);
    }

    public String getCode() {
        return codeProperty.getValue();
    }

    public void setCode(@NonNull String code) {
        codeProperty.setValue(code);
    }

    @Override
    public void updateTarget() {
        super.updateTarget();
        target.setCode(getCode());
        target.getVariables().clear();
        target.addVariables(portsProperty.stream().map(PortViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addPort(@NonNull PortViewModel port) {
        // TODO: prior checks
        portsProperty.add(port);
    }

    public void removePort(@NonNull PortViewModel port) {
        portsProperty.remove(port);
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
