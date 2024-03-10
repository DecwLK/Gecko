package org.gecko.tools;

import java.util.Set;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.gecko.actions.ActionManager;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * A concrete representation of a marquee-{@link AreaTool}, utilized for marking a rectangle-formed area in the view and
 * thus selecting the covered {@link org.gecko.view.views.viewelement.ViewElement}s. Holds the current
 * {@link EditorViewModel}..
 */
public class MarqueeTool extends AreaTool {
    private final EditorViewModel editorViewModel;

    private static final double STROKE_WIDTH = 1;
    private static final double STROKE_DASH = 5d;

    public MarqueeTool(ActionManager actionManager, EditorViewModel editorViewModel) {
        super(actionManager, ToolType.MARQUEE_TOOL, true);
        this.editorViewModel = editorViewModel;
    }

    @Override
    Rectangle createNewArea() {
        Rectangle marquee = new Rectangle();
        marquee.setFill(null);
        marquee.setStroke(Color.BLUE);
        marquee.setStrokeWidth(STROKE_WIDTH);
        marquee.getStrokeDashArray().addAll(STROKE_DASH, STROKE_DASH);
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
