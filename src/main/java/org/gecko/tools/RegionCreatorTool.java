package org.gecko.tools;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.BlockViewModelElement;

/**
 * A concrete representation of a region-creating-{@link AreaTool}, utilized for creating a
 * rectangle-formed-{@link org.gecko.viewmodel.RegionViewModel RegionViewModel}. Holds the {@link Color} of the drawn
 * region.
 */
public class RegionCreatorTool extends AreaTool {
    private Color color;

    public RegionCreatorTool(ActionManager actionManager) {
        super(actionManager, ToolType.REGION_CREATOR, false);
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
    void onAreaCreated(MouseEvent event, Bounds worldBounds) {
        if (worldBounds.getWidth() < BlockViewModelElement.MIN_WIDTH
            || worldBounds.getHeight() < BlockViewModelElement.MIN_HEIGHT) {
            return;
        }
        actionManager.run(actionManager.getActionFactory()
            .createCreateRegionViewModelElementAction(new Point2D(worldBounds.getMinX(), worldBounds.getMinY()),
                new Point2D(worldBounds.getWidth(), worldBounds.getHeight()), color));
    }
}
