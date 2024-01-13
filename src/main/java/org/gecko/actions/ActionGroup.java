package org.gecko.actions;

import lombok.Data;

import java.util.List;

@Data
public class ActionGroup extends Action {
    private final List<Action> actions;

    public ActionGroup(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    void run() {
        //TODO stub
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        //TODO stub
        return null;
    }
}
