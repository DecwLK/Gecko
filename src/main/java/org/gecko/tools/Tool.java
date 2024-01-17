package org.gecko.tools;

import javafx.scene.Node;
import org.gecko.view.views.viewelement.ViewElementVisitor;

public interface Tool extends ViewElementVisitor {
    String getName();

    String getIconPath();

    void visitView(Node view);
}
