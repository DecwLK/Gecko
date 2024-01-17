package org.gecko.tools;

import javafx.scene.Node;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public abstract class Tool implements ViewElementVisitor {
    public abstract String getName();

    public abstract String getIconPath();

    public abstract void visitView(Node view);
}
