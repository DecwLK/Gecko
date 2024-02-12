package org.gecko.tools;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.BlockViewModelElement;
import org.gecko.viewmodel.PositionableViewModelElement;

public class RegionCreatorTool extends AreaTool {
    private Color color;

    public RegionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.REGION_CREATOR);
    }

    @Override
    Rectangle createNewArea() {
        Rectangle region = new Rectangle();
        color = Color.color(Math.random(), Math.random(), Math.random());
        region.setFill(color);
        region.setOpacity(0.5);
        return region;
    }

    @Override
    void onAreaCreated(MouseEvent event, Bounds worldAreaBounds) {
        if (worldAreaBounds.getWidth() < BlockViewModelElement.MIN_WIDTH
            || worldAreaBounds.getHeight() < BlockViewModelElement.MIN_HEIGHT) {
            return;
        }
        actionManager.run(actionManager.getActionFactory()
            .createCreateRegionViewModelElementAction(new Point2D(worldAreaBounds.getMinX(), worldAreaBounds.getMinY()),
                new Point2D(worldAreaBounds.getWidth(), worldAreaBounds.getHeight()), color));
    }
}
