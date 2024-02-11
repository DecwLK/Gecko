package org.gecko.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final BooleanProperty isStartStateProperty;
    private final ListProperty<ContractViewModel> contractsProperty;

    private final ObservableList<EdgeViewModel> incomingEdges;
    private final ObservableList<EdgeViewModel> outgoingEdges;

    public StateViewModel(int id, @NonNull State target) {
        super(id, target);
        this.isStartStateProperty = new SimpleBooleanProperty();
        this.contractsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.incomingEdges = FXCollections.observableArrayList();
        this.outgoingEdges = FXCollections.observableArrayList();
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

    /**
     * Returns the offset used to position the provided edge. The offset is a value between 0 and 1, where 0 means the
     * edge should be positioned at the start point of one of the state's edges and 1 means the edge should be
     * positioned at the end point of one of the state's edges. If the given edge is only edge that is connected, -1 is
     * returned.
     *
     * @param edgeViewModel the edge to get the offset for
     * @return the offset used to position the provided edge
     */
    public double getEdgeOffset(EdgeViewModel edgeViewModel) {
        List<EdgeViewModel> edges = new ArrayList<>(incomingEdges.reversed());
        edges.addAll(outgoingEdges);

        double edgeCount = 0;
        double edgeIndex = 0;
        for (int i = 0; i < edges.size(); i++) {
            EdgeViewModel edge = edges.get(i);

            if (edge.equals(edgeViewModel)) {
                edgeIndex = i;
            }

            if ((edge.getSource() == edgeViewModel.getSource()
                && edge.getDestination() == edgeViewModel.getDestination()) || (
                edge.getSource() == edgeViewModel.getDestination()
                    && edge.getDestination() == edgeViewModel.getSource())) {
                edgeCount++;
            }
        }
        return (edgeCount <= 1) ? -1 : (edgeIndex / edgeCount);
    }

    /**
     * Returns the offset used to position the provided loop. This offset represents the second point of the loop, where
     * 0 means the loop should be positioned at the start point of the state and 1 means the loop should be positioned
     * at the end point of the state.
     *
     * @param edgeViewModel the loop to get the offset for
     * @return the offset used to position the provided loop
     */
    public int getLoopOffset(EdgeViewModel edgeViewModel) {
        List<EdgeViewModel> loops =
            incomingEdges.stream().filter(edge -> edge.getSource() == this && edge.getDestination() == this).toList();

        return loops.indexOf(edgeViewModel) + 1;
    }

    @Override
    public Object accept(@NonNull PositionableViewModelElementVisitor visitor) {
        return visitor.visit(this);
    }
}
