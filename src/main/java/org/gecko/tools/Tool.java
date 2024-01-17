package org.gecko.tools;

import javafx.scene.Node;
import org.gecko.actions.ActionManager;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public abstract class Tool implements ViewElementVisitor {

    private final ActionManager actionManager;

    public Tool(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public abstract String getName();

    public abstract String getIconPath();

    public abstract void visitView(Node view);
}
