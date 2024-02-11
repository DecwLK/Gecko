package org.gecko.actions;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gecko.exceptions.GeckoException;

/** A concrete representation of an {@link Action}, which encapsulates a list of other concrete actions, which are iteratively run or undone. */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActionGroup extends Action {
    private final List<Action> actions;

    public ActionGroup(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    boolean run() throws GeckoException {
        for (Action action : actions) {
            if (!action.run()) {
                return false;
            }
        }
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        if (actions.isEmpty() || actions.stream().anyMatch(action -> action.getUndoAction(actionFactory) == null)) {
            return null;
        }
        return new ActionGroup(actions.stream().map(action -> action.getUndoAction(actionFactory)).toList().reversed());
    }
}
