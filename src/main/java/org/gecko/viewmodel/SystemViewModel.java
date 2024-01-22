package org.gecko.viewmodel;

import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.model.System;

/**
 * Represents an abstraction of a {@link System} model element. A {@link SystemViewModel} is described by a code snippet (?) and a set of
 * {@link PortViewModel}s. Contains methods for managing the afferent data and updating the target-{@link System}.
 */
@Getter
@Setter
public class SystemViewModel extends BlockViewModelElement<System> {
    private final StringProperty codeProperty;
    private final ObservableList<PortViewModel> portsProperty; //TODO should this be called property?

    public SystemViewModel(int id, @NonNull System target) {
        super(id, target);
        this.codeProperty = new SimpleStringProperty(target.getCode());
        this.portsProperty = FXCollections.observableArrayList();
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

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
