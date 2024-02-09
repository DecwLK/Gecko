package org.gecko.tools;

import java.util.Set;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class MarqueeTool extends AreaTool {
    private final EditorViewModel editorViewModel;

    public MarqueeTool(ActionManager actionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.MARQUEE_TOOL);
        this.editorViewModel = editorViewModel;
    }

    @Override
    Rectangle createNewArea() {
        Rectangle marquee = new Rectangle();
        marquee.setFill(null);
        marquee.setStroke(Color.BLUE);
        marquee.setStrokeWidth(1);
        marquee.getStrokeDashArray().addAll(5d, 5d);
        return marquee;
    }

    @Override
    void onAreaCreated(MouseEvent event, Bounds worldAreaBounds) {
        Set<PositionableViewModelElement<?>> elements = editorViewModel.getElementsInArea(worldAreaBounds);
        if (elements.isEmpty()) {
            return;
        }
        actionManager.run(actionManager.getActionFactory().createSelectAction(elements, !event.isShiftDown()));
    }
}
