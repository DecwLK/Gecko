package org.gecko.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.gecko.exceptions.GeckoException;
import org.gecko.model.System;
import org.gecko.model.Visibility;
import org.gecko.viewmodel.GeckoViewModel;
import org.gecko.viewmodel.PortViewModel;
import org.gecko.viewmodel.SystemConnectionViewModel;

/**
 * A concrete representation of an {@link Action} that changes the visibility of a {@link PortViewModel}, which it holds
 * a reference to. Additionally, holds the old and new {@link Visibility Visibilities} of the contract for undo/redo
 * purposes.
 */
public class ChangeVisibilityPortViewModelAction extends Action {

    private final GeckoViewModel geckoViewModel;
    private final Visibility visibility;
    private final Visibility oldVisibility;
    private final PortViewModel portViewModel;
    private Action systemConnectionDeleteActionGroup;

    public ChangeVisibilityPortViewModelAction(
        GeckoViewModel geckoViewModel, PortViewModel portViewModel, Visibility visibility) {
        this.geckoViewModel = geckoViewModel;
        this.portViewModel = portViewModel;
        this.visibility = visibility;
        this.oldVisibility = portViewModel.getVisibility();
    }

    public ChangeVisibilityPortViewModelAction(
        GeckoViewModel geckoViewModel, PortViewModel portViewModel, Visibility visibility,
        Action systemConnectionDeleteActionGroup) {
        this.geckoViewModel = geckoViewModel;
        this.portViewModel = portViewModel;
        this.visibility = visibility;
        this.oldVisibility = portViewModel.getVisibility();
        this.systemConnectionDeleteActionGroup = systemConnectionDeleteActionGroup;
    }

    @Override
    boolean run() throws GeckoException {
        if (visibility == oldVisibility) {
            return false;
        }

        if (systemConnectionDeleteActionGroup == null) {
            systemConnectionDeleteActionGroup = getSystemConnectionDeleteActionGroup();
        }

        systemConnectionDeleteActionGroup.run();
        portViewModel.setVisibility(visibility);
        portViewModel.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createChangeVisibilityPortViewModelAction(portViewModel, oldVisibility,
            systemConnectionDeleteActionGroup.getUndoAction(geckoViewModel.getActionManager().getActionFactory()));
    }

    private ActionGroup getSystemConnectionDeleteActionGroup() {
        System containingSystem = geckoViewModel.getSystemViewModelWithPort(portViewModel).getTarget();
        System parentSystem = containingSystem.getParent();
        List<Action> deleteActions = new ArrayList<>(getSystemConnectionDeleteActions(containingSystem));

        if (parentSystem != null) {
            deleteActions.addAll(getSystemConnectionDeleteActions(parentSystem));
        }
        return new ActionGroup(deleteActions);
    }

    private Set<SystemConnectionViewModel> getSystemConnectionViewModels(System system) {
        return system.getConnections()
            .stream()
            .filter(systemConnection -> systemConnection.getSource().equals(portViewModel.getTarget())
                || systemConnection.getDestination().equals(portViewModel.getTarget()))
            .map(systemConnection -> (SystemConnectionViewModel) geckoViewModel.getViewModelElement(systemConnection))
            .collect(Collectors.toSet());
    }

    private List<Action> getSystemConnectionDeleteActions(System system) {
        return getSystemConnectionViewModels(system).stream()
            .map(systemConnectionViewModel -> (Action) new DeleteSystemConnectionViewModelElementAction(geckoViewModel,
                systemConnectionViewModel, system))
            .toList();
    }
}
