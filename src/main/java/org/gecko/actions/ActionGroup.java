package org.gecko.actions;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActionGroup extends Action {
    private final List<Action> actions;

    public ActionGroup(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    void run() {
        actions.forEach(Action::run);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        if (actions.isEmpty() || actions.stream().anyMatch(action -> action.getUndoAction(actionFactory) == null)) {
            return null;
        }
        return new ActionGroup(actions.stream().map(action -> action.getUndoAction(actionFactory)).toList().reversed());
    }
}
