package org.gecko.tools;

import javafx.scene.Node;
import org.gecko.view.views.viewelement.ViewElement;

public interface Tool {
    String getName();

    String getIcon();

    void visit(ViewElement<?> viewElement);

    void visitView(Node view);
}
