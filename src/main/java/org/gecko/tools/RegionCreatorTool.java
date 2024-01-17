package org.gecko.tools;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.gecko.actions.Action;
import org.gecko.actions.ActionManager;

public class RegionCreatorTool extends Tool {

    private static final String NAME = "Region Creator Tool";

    public RegionCreatorTool(ActionManager actionManager) {
        super(actionManager);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getIconPath() {
        //TODO stub
        return null;
    }

    @Override
    public void visitView(Node view) {
        super.visitView(view);
        view.setOnMouseClicked(event -> {
            Point2D position = new Point2D(event.getX(), event.getY());
            Action createRegionAction = actionManager.getActionFactory().createCreateRegionViewModelElementAction(position);
            actionManager.run(createRegionAction);
        });
    }
}
