package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gecko.exceptions.ModelException;
import org.gecko.model.State;

/**
 * Represents an abstraction of a {@link State} model element. A {@link StateViewModel} is described by a set of
 * {@link ContractViewModel}s and can target either a regular or a start-{@link State}. Contains methods for managing
 * the afferent data and updating the target-{@link State}.
 */
@Setter
@Getter
public class StateViewModel extends BlockViewModelElement<State> {
    private static final double EDGE_DISTANCE = 20;

    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    private final Set<EdgeViewModel> outgoingEdges;
    private final Set<EdgeViewModel> incomingEdges;

    public StateViewModel(int id, @NonNull State target) {
        super(id, target);
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

        this.outgoingEdges = new HashSet<>();
        this.incomingEdges = new HashSet<>();
    }

    public boolean getIsStartState() {
        return isStartStateProperty.getValue();
    }

    public void setStartState(boolean isStartState) {
        isStartStateProperty.setValue(isStartState);
    }

    @Override
    public void updateTarget() throws ModelException {
        super.updateTarget();
        // TODO: not possible because of missing reference to parent system
        /*Automaton automaton = parentSystem.getAutomaton();
        if (getIsStartState()) {
            automaton.setStartState(target);
        }*/
        target.getContracts().clear();
        target.addContracts(contractsProperty.stream().map(ContractViewModel::getTarget).collect(Collectors.toSet()));
    }

    public void addContract(@NonNull ContractViewModel contract) {
        // TODO: prior checks
        contractsProperty.add(contract);
    }

    public void removeContract(@NonNull ContractViewModel contract) {
        contractsProperty.remove(contract);
    }

    public List<ContractViewModel> getContracts() {
        return new ArrayList<>(contractsProperty);
    }

    public double getEdgeOffset(EdgeViewModel edgeViewModel) {
        List<EdgeViewModel> edges = new ArrayList<>(outgoingEdges);
        edges.addAll(incomingEdges);

        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).equals(edgeViewModel)) {
                return EDGE_DISTANCE * i * (i < incomingEdges.size() ? -1 : 1);
            }
        }

        return 0;
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
