package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.model.Edge;
import org.gecko.model.System;
import org.gecko.model.SystemConnection;
import org.gecko.viewmodel.EdgeViewModel;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.PositionableViewModelElementVisitor;
import org.gecko.viewmodel.RegionViewModel;
import org.gecko.viewmodel.StateViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;
import org.gecko.viewmodel.SystemViewModel;


/** Follows the visitor pattern, implementing the {@link PositionableViewModelElementVisitor} interface. Determines all necessary delete-{@link Action}s for deleting all "lower level"-dependencies of a given parent-@link SystemViewModel}. */
public class DeleteActionsCreatorVisitor implements PositionableViewModelElementVisitor {

    private final GeckoViewModel geckoViewModel;
    private final SystemViewModel parentSystemViewModel;
    private final Set<AbstractPositionableViewModelElementAction> deleteActions;

    public DeleteActionsCreatorVisitor(GeckoViewModel geckoViewModel, SystemViewModel parentSystemViewModel) {
        this.geckoViewModel = geckoViewModel;
        this.parentSystemViewModel = parentSystemViewModel;
        this.deleteActions = new HashSet<>();
    }

    @Override
    public Set<AbstractPositionableViewModelElementAction> visit(SystemViewModel systemViewModel) {
        DeleteActionsCreatorVisitor deleteActionsCreatorVisitor =
            new DeleteActionsCreatorVisitor(geckoViewModel, systemViewModel);
        for (System childSystem : systemViewModel.getTarget().getChildren()) {
            SystemViewModel childSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(childSystem);
            deleteActions.addAll((Set<AbstractPositionableViewModelElementAction>) childSystemViewModel.accept(
                deleteActionsCreatorVisitor));
        }

        systemViewModel.getPorts().forEach(portViewModel -> {
            deleteActions.addAll((Set<AbstractPositionableViewModelElementAction>) portViewModel.accept(this));
        });

        System system = systemViewModel.getTarget();
        geckoViewModel.getViewModelElements(system.getConnections()).forEach(systemConnectionViewModel -> {
            systemConnectionViewModel.accept(deleteActionsCreatorVisitor);
        });
        geckoViewModel.getViewModelElements(system.getAutomaton().getAllElements()).forEach(element -> {
            element.accept(this);
        });

        deleteActions.add(
            new DeleteSystemViewModelElementAction(geckoViewModel, systemViewModel, parentSystemViewModel.getTarget()));

        return deleteActions;
    }

    @Override
    public Set<AbstractPositionableViewModelElementAction> visit(RegionViewModel regionViewModel) {
        deleteActions.add(new DeleteRegionViewModelElementAction(geckoViewModel, regionViewModel,
            parentSystemViewModel.getTarget().getAutomaton()));

        return deleteActions;
    }

    @Override
    public Set<AbstractPositionableViewModelElementAction> visit(
        SystemConnectionViewModel systemConnectionViewModel) {
        deleteActions.add(new DeleteSystemConnectionViewModelElementAction(geckoViewModel, systemConnectionViewModel,
            parentSystemViewModel.getTarget()));

        return deleteActions;
    }

    @Override
    public Set<AbstractPositionableViewModelElementAction> visit(EdgeViewModel edgeViewModel) {
        deleteActions.add(new DeleteEdgeViewModelElementAction(geckoViewModel, edgeViewModel,
            parentSystemViewModel.getTarget().getAutomaton()));

        return deleteActions;
    }

    @Override
    public Set<AbstractPositionableViewModelElementAction> visit(StateViewModel stateViewModel) {
        Set<Edge> edges = parentSystemViewModel.getTarget()
            .getAutomaton()
            .getEdges()
            .stream()
            .filter(edge -> edge.getSource().equals(stateViewModel.getTarget()) || edge.getDestination()
                .equals(stateViewModel.getTarget()))
            .collect(Collectors.toSet());
        geckoViewModel.getViewModelElements(edges).forEach(edgeViewModel -> {
            edgeViewModel.accept(this);
        });
        deleteActions.add(new DeleteStateViewModelElementAction(geckoViewModel, stateViewModel, parentSystemViewModel));

        return deleteActions;
    }

    @Override
    // parentSystem has to be the system that contains the port
    public Set<AbstractPositionableViewModelElementAction> visit(PortViewModel portViewModel) {
        SystemViewModel actualParentSystemViewModel;
        SystemViewModel containingSystemViewModel;
        if (parentSystemViewModel.getPorts().contains(portViewModel)) {
            actualParentSystemViewModel =
                (SystemViewModel) geckoViewModel.getViewModelElement(parentSystemViewModel.getTarget().getParent());
            containingSystemViewModel = parentSystemViewModel;
        } else {
            actualParentSystemViewModel = parentSystemViewModel;
            containingSystemViewModel = (SystemViewModel) geckoViewModel.getViewModelElement(
                parentSystemViewModel.getTarget()
                    .getChildren()
                    .stream()
                    .filter(system -> system.getVariables().contains(portViewModel.getTarget()))
                    .findFirst()
                    .orElse(null));
        }

        if (actualParentSystemViewModel != null) {
            visitSystemConnections(actualParentSystemViewModel, portViewModel);
        }
        visitSystemConnections(containingSystemViewModel, portViewModel);
        deleteActions.add(
            new DeletePortViewModelElementAction(geckoViewModel, portViewModel, containingSystemViewModel));

        return deleteActions;
    }

    private void visitSystemConnections(SystemViewModel systemViewModel, PortViewModel portViewModel) {
        Set<SystemConnection> systemConnections = systemViewModel.getTarget()
            .getConnections()
            .stream()
            .filter(systemConnection -> systemConnection.getSource().equals(portViewModel.getTarget())
                || systemConnection.getDestination().equals(portViewModel.getTarget()))
            .collect(Collectors.toSet());
        geckoViewModel.getViewModelElements(systemConnections).forEach(systemConnectionViewModel -> {
            DeleteActionsCreatorVisitor deleteActionsCreatorVisitor =
                new DeleteActionsCreatorVisitor(geckoViewModel, systemViewModel);
            deleteActions.addAll((Set<AbstractPositionableViewModelElementAction>) systemConnectionViewModel.accept(
                deleteActionsCreatorVisitor));
        });
    }
}
