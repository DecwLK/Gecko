package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.System;

/**
 * Represents an abstraction of a {@link System} model element. A {@link SystemViewModel} is described by a code snippet
 * and a set of {@link PortViewModel}s. Contains methods for managing the afferent data and updating the
 * target-{@link System}.
 */
@Getter
@Setter
public class SystemViewModel extends BlockViewModelElement<System> {
    private final StringProperty codeProperty;
    private final ListProperty<PortViewModel> portsProperty;
    private StateViewModel startState;

    public SystemViewModel(int id, @NonNull System target) {
        super(id, target);
        this.codeProperty = new SimpleStringProperty(target.getCode());
        this.portsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.sizeProperty.setValue(new Point2D(300, 300));
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
    public void updateTarget() throws ModelException {
        super.updateTarget();
        target.setCode(getCode());
        target.getVariables().clear();
        target.addVariables(portsProperty.stream().map(PortViewModel::getTarget).collect(Collectors.toSet()));
        target.getAutomaton().setStartState(startState != null ? startState.getTarget() : null);
    }

    public void addPort(@NonNull PortViewModel port) {
        // TODO: prior checks
        portsProperty.add(port);
    }

    public void removePort(@NonNull PortViewModel port) {
        portsProperty.remove(port);
    }

    /**
     * Sets the start state of the automaton of the system.
     *
     * @param newStartState the new start state
     */
    public void setStartState(StateViewModel newStartState) {
        if (newStartState != null) {
            newStartState.setStartState(true);
        }
        if (startState != null) {
            startState.setStartState(false);
        }
        startState = newStartState;
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
